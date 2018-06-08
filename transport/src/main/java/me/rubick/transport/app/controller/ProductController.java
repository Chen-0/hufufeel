package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.response.RestResponse;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.DateUtils;
import me.rubick.common.app.utils.FormUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.DistributionChannel;
import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.model.Warehouse;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.service.UserService;
import me.rubick.transport.app.vo.ProductContainer;
import me.rubick.transport.app.vo.ProductFormVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@SessionAttributes("productContainer")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private UserService userService;

    @ModelAttribute("productContainer")
    public ProductContainer productController() {
        return new ProductContainer();
    }

    @RequestMapping(value = "/product/index")
    public String indexProduct(
            Model model,
            @PageableDefault(size = 10, sort = {"updatedAt"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int status) throws BusinessException {
        User user = userService.getByLogin();
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("");
        }
        Page<Product> products = productService.findProduct(user, keyword, status, pageable);

        model.addAttribute("elements", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("_STATUS", status);

        log.info("{}", products.getNumberOfElements());

        return "product/index";
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.GET)
    public String getCreateProduct(Model model) {

        if (ObjectUtils.isEmpty(model.asMap().get("felements"))) {
            model.asMap().put("felements", new ProductFormVo());
        }

        return "product/create";
    }

    @RequestMapping(value = "/product/post_create", method = RequestMethod.POST)
    public String postCreateProduct(
            @Valid ProductFormVo productFormVo,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws CommonException {

        if (bindingResult.hasErrors()) {

            log.info("-------------------- form has error ----------------");
            log.info("{}", JSONMapper.toJSON(productFormVo));
            redirectAttributes.addFlashAttribute("felements", productFormVo);
            redirectAttributes.addFlashAttribute("errors", FormUtils.toMap(bindingResult));

            return "redirect:/product/create";
        }

        //store the product
        Product product = BeanMapperUtils.map(productFormVo, Product.class);
        product.setDeadline(DateUtils.stringToDate(productFormVo.getDeadline()));
        productService.createProduct(product);
        redirectAttributes.addFlashAttribute("SUCCESS", "添加货品成功！");
        return "redirect:/product/index";
    }

    @RequestMapping("/product/{id}/update")
    public String updateProduct(@PathVariable("id") long id, Model model) {
        return "product/update";
    }

    @RequestMapping(value = "/product/{id}/post_update", method = RequestMethod.POST)
    public String postUpdateProduct(@Valid ProductFormVo productFormVo, RedirectAttributes redirectAttributes) {
        return "";
    }

    /**
     * 删除商品
     *
     * @param id
     * @param redirectAttributes
     * @return
     */
    @RequestMapping("/product/{id}/remove")
    public String deleteProduct(
            @PathVariable("id") long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("SUCCESS", "删除商品成功！");
            return "redirect:/product/index";
        } catch (BusinessException e) {
            log.error("", e);
            redirectAttributes.addFlashAttribute("ERROR", e.getMessage());
            return "redirect:/product/index";
        }
    }



    /**
     * 添加商品至发货清单中
     * @param trackingNumbers
     * @param productContainer
     * @param redirectAttributes
     * @param model
     * @return
     */
    @RequestMapping(value = "/product/select", method = RequestMethod.POST)
    public String selectProduct(
            @RequestParam("trackingNumber[]") List<Long> trackingNumbers,
            @ModelAttribute("productContainer") ProductContainer productContainer,
            RedirectAttributes redirectAttributes,
            Model model) {

        productContainer.getProducts().addAll(trackingNumbers);
        redirectAttributes.addFlashAttribute("SUCCESS", "商品已经成功添加至发货清单");
        return "redirect:/product/ready_to_send";
    }

    /**
     * 发货清单
     * @param productContainer
     * @return
     */
    @RequestMapping(value = "/product/ready_to_send", method = RequestMethod.GET)
    public String readyToSend(
            @ModelAttribute("productContainer") ProductContainer productContainer,
            Model model
    ) {
        List<Product> products = productService.findProducts(productContainer.getProducts());

        List<Warehouse> warehouses = warehouseRepository.findAll();

        model.addAttribute("warehouses", warehouses);
        model.addAttribute("elements", products);
        return "/product/ready_to_send";
    }

    @RequestMapping(value = "/product/package/{id}/remove", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> removeProductFromPackage(
            @PathVariable("id") long id,
            @ModelAttribute("productContainer") ProductContainer productContainer,
            RedirectAttributes redirectAttributes
    ) {
        if (productContainer.getProducts().contains(id)) {
            productContainer.getProducts().remove(id);
        }

        return new RestResponse<>();
    }

    @RequestMapping("/product/package/remove_all")
    public String removeAllProductFromPackage(
            @ModelAttribute("productContainer") ProductContainer productContainer
    ) {
        productContainer.getProducts().clear();

        return "redirect:/product/ready_to_send";
    }

    /**
     * 根据仓库筛选渠道
     * @param id
     * @return
     */
    @RequestMapping("/channel/select")
    @ResponseBody
    public RestResponse<List<DistributionChannel>> getChannelByWarehouse(
            @RequestParam long id
    ) {
        Warehouse warehouse = warehouseRepository.findOne(id);

        if (ObjectUtils.isEmpty(warehouse)) {
            return new RestResponse<>(Collections.emptyList());
        }

        return new RestResponse<>(warehouse.getDistributionChannels());
    }
}

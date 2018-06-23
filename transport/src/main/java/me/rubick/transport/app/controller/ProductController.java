package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.response.RestResponse;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.DateUtils;
import me.rubick.common.app.utils.FormUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.ProductRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.vo.ProductContainer;
import me.rubick.transport.app.vo.ProductFormVo;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@SessionAttributes("productContainer")
public class ProductController extends AbstractController {

    @Resource
    private ProductService productService;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private ProductRepository productRepository;

    @ModelAttribute("productContainer")
    public ProductContainer productController() {
        return new ProductContainer();
    }

    @RequestMapping(value = "/product/index")
    public String indexProduct(
            Model model,
            @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status) throws BusinessException {
        User user = userService.getByLogin();
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("");
        }
        Page<Product> products = productService.findProduct(user, keyword, status, pageable);

        model.addAttribute("elements", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("_STATUS", status);
        model.addAttribute("MENU", "HUOPINGUANLI");

        log.info("{}", products.getNumberOfElements());

        return "product/index";
    }

    @RequestMapping(value = "/product/{id}/show", method = RequestMethod.GET)
    public String showProduct(
            @PathVariable("id") long id,
            Model model
    ) throws BusinessException {
        Product product = productRepository.findOne(id);

        if (ObjectUtils.isEmpty(product)) {
            //TODO no found
            throw new BusinessException("");
        }

        model.addAttribute("ele", product);
        model.addAttribute("MENU", "HUOPINGUANLI");
        return "/product/show";
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.GET)
    public String getCreateProduct(Model model) {
        model.addAttribute("bts", ProductBusinessTypeEnum.values());
        if (ObjectUtils.isEmpty(model.asMap().get("felements"))) {
            model.asMap().put("felements", new ProductFormVo());
        }

        return "product/create";
    }

    @RequestMapping(value = "/product/post_create", method = RequestMethod.POST)
    public String postCreateProduct(
            @Valid ProductFormVo productFormVo,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "p_file", required = false) MultipartFile image
    ) throws CommonException {
        if (bindingResult.hasErrors()) {

            log.info("-------------------- form has error ----------------");
            log.info("{}", JSONMapper.toJSON(productFormVo));
            redirectAttributes.addFlashAttribute("felements", productFormVo);
            Map<String, String> map = FormUtils.toMap(bindingResult);

            if (ObjectUtils.isEmpty(image) || image.isEmpty()) {
                map.put("p_file", "请上传货品图片");
            }
            redirectAttributes.addFlashAttribute("errors", map);

            return "redirect:/product/create";
        }

        if (ObjectUtils.isEmpty(image) || image.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("p_file", "请上传货品图片");
            redirectAttributes.addFlashAttribute("felements", productFormVo);
            redirectAttributes.addFlashAttribute("errors", map);

            return "redirect:/product/create";
        }

        //store the product
        Product product = BeanMapperUtils.map(productFormVo, Product.class);

        //更新图片
        if (!ObjectUtils.isEmpty(image) && !image.isEmpty()) {
            try {
                Document document = documentService.uploadProductImage(image);
                if (!ObjectUtils.isEmpty(document)) {
                    product.setImageId(document.getId());
                }
            } catch (BusinessException e) {
                Map<String, String> map = new HashMap<>();
                map.put("p_file", e.getMessage());
                redirectAttributes.addFlashAttribute("felements", productFormVo);
                redirectAttributes.addFlashAttribute("errors", map);
                return "redirect:/product/create";
            }
        }

        if (StringUtils.hasText(productFormVo.getDeadline())) {
            product.setDeadline(DateUtils.stringToDate(productFormVo.getDeadline()));
        }
        productService.createProduct(product);
        redirectAttributes.addFlashAttribute("SUCCESS", "添加货品成功！");
        return "redirect:/product/index";
    }

    /**
     * 更新
     *
     * @param model
     * @param id
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "/product/{id}/update", method = RequestMethod.GET)
    public String getUpdateProduct(
            Model model,
            @PathVariable("id") long id) throws BusinessException {
        Product product = productRepository.findOne(id);

        if (ObjectUtils.isEmpty(product)) {
            // TODO 错误提示
            throw new BusinessException("");
        }

        model.addAttribute("felements", product);

        return "/product/update";
    }


    @RequestMapping(value = "/product/{id}/update", method = RequestMethod.POST)
    public String postUpdateProduct(
            @Valid ProductFormVo productFormVo,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            @PathVariable("id") long id,
            @RequestParam(name = "p_file", required = false) MultipartFile image
    ) throws BusinessException {
        Product product = productRepository.findOne(id);

        if (ObjectUtils.isEmpty(product)) {
            // TODO 错误提示
            throw new BusinessException("");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("felements", productFormVo);
            redirectAttributes.addFlashAttribute("errors", FormUtils.toMap(bindingResult));

            return MessageFormat.format("redirect:/product/{0}/update", id);
        }

        BeanMapperUtils.copy(productFormVo, product);
        //设置时间
        try {
            product.setDeadline(DateUtils.stringToDate(productFormVo.getDeadline()));
        } catch (CommonException e) {
            redirectAttributes.addFlashAttribute("felements", productFormVo);
            Map<String, String> map = new HashMap<>();
            map.put("deadline", "时间格式错误");
            redirectAttributes.addFlashAttribute("errors", map);
        }

        //更新图片
        if (!image.isEmpty()) {
            Document document = documentService.uploadProductImage(image);
            if (!ObjectUtils.isEmpty(document)) {
                product.setImageId(document.getId());
            }
        }

        productService.createProduct(product);

        redirectAttributes.addFlashAttribute("SUCCESS", "修改货品成功！");
        return "redirect:/product/index";
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
     *
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
     *
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

//    /**
//     * 根据仓库筛选渠道
//     *
//     * @param id
//     * @return
//     */
//    @RequestMapping("/channel/select")
//    @ResponseBody
//    public RestResponse<List<DistributionChannel>> getChannelByWarehouse(
//            @RequestParam long id
//    ) {
//        Warehouse warehouse = warehouseRepository.findOne(id);
//
//        if (ObjectUtils.isEmpty(warehouse)) {
//            return new RestResponse<>(Collections.emptyList());
//        }
//
//        return new RestResponse<>(warehouse.getDistributionChannels());
//    }
}

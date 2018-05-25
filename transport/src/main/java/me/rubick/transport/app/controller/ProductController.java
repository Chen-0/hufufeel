package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.DateUtils;
import me.rubick.common.app.utils.FormUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.vo.ProductFormVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@Slf4j
public class ProductController {

    @Resource
    private ProductService productService;

    @RequestMapping(value = "/product/index")
    public String indexProduct(
            Model model,
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int status) {
        Page<Product> products = productService.findProduct(keyword, status, pageable);

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
        return "Success";
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
}

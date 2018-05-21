package me.rubick.transport.app.controller;

import me.rubick.hufu.logistics.app.utils.BeanMapperUtils;
import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.vo.ProductFormVo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class ProductController {

    @Resource
    private ProductService productService;

    @RequestMapping(value = "/product/create", method = RequestMethod.GET)
    public String getCreateProduct() {
        return "";
    }

    @RequestMapping(value = "/product/postCreate")
    @ResponseBody
    public String postCreateProduct(@Valid ProductFormVo productFormVo, BindingResult bindingResult) {

        bindingResult.getFieldErrors();


        BindingResultUtils.getBindingResult()
        Product product = BeanMapperUtils.map(productFormVo, Product.class);
        productService.createProduct(product);
        return "Success";
    }
}

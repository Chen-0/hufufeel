package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.model.ProductStatus;
import me.rubick.transport.app.repository.ProductRepository;
import me.rubick.transport.app.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin/")
@Slf4j
@SessionAttributes("productStatus")
public class AdminProductController {

    @Resource
    private ProductService productService;

    @Resource
    private ProductRepository productRepository;

    @ModelAttribute("productStatus")
    public Integer productStatus() {
        return -1;
    }

    @RequestMapping("/product/index")
    public String indexProduct(
            Model model,
            @PageableDefault(size = 25, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status,
            @ModelAttribute("productStatus") Integer productStatus
    ) {
        Page<Product> products = productService.findProduct(null, keyword, status, pageable);

        model.addAttribute("elements", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        if (!ObjectUtils.isEmpty(status)) {
            model.addAttribute("productStatus", status);
        }
//        productStatus = status;

        log.info("productStatus - {}", productStatus);

        log.info("{}", products.getNumberOfElements());

        return "/admin/product/index";
    }

    @RequestMapping("/product/{id}/change_status")
    public String changeProductStatus(
            @PathVariable("id") long id,
            @RequestParam String name,
            @RequestParam(required = false) String comment,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("productStatus") Integer status
    ) {
        Product product = productRepository.findOne(id);

        if (ObjectUtils.isEmpty(product)) {
            return "redirect:/admin/product/index";
        }

        if (name.equals("success")) {
            product.setStatus(ProductStatus.READY_CHECK);
        } else if (name.equals("fail")) {
            product.setStatus(ProductStatus.FAIL_CHECK);
            product.setComment(comment);
        } else {
            return "redirect:/admin/product/index";
        }

        productRepository.save(product);
        redirectAttributes.addFlashAttribute("success", "更新货品审核状态！");
        if (ObjectUtils.isEmpty(status) || status == -1) {
            return "redirect:/admin/product/index";
        } else {
            return "redirect:/admin/product/index?status=" + status;
        }
    }
}

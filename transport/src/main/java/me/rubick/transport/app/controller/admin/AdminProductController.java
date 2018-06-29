package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.exception.FormException;
import me.rubick.common.app.exception.NotFoundException;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.DateUtils;
import me.rubick.common.app.utils.FormUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.*;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.repository.ProductRepository;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.vo.ProductFormVo;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

@Controller
@RequestMapping("/admin/")
@Slf4j
@SessionAttributes("productStatus")
public class AdminProductController extends AbstractController {

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
        Page<Product> products = productService.findProduct(null, keyword, status, -1, pageable);

        model.addAttribute("elements", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        if (!ObjectUtils.isEmpty(status)) {
            model.addAttribute("productStatus", status);
        }

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
            product.setStatus(ProductStatusEnum.READY_CHECK);
        } else if (name.equals("fail")) {
            product.setStatus(ProductStatusEnum.FAIL_CHECK);
            product.setReason(comment);
        } else {
            return "redirect:/admin/product/index";
        }

        productRepository.save(product);
        redirectAttributes.addFlashAttribute("success", "更新货品审核状态！");
        messageService.send(
                product.getUserId(),
                "/product/index?status=1",
                MessageFormat.format("货品：{0} 审核成功！", product.getProductName())
        );

        if (ObjectUtils.isEmpty(status) || status == -1) {
            return "redirect:/admin/product/index";
        } else {
            return "redirect:/admin/product/index?status=" + status;
        }
    }

    @RequestMapping(value = "/product/{id}/update", method = RequestMethod.GET)
    public String getUpdateProduct(
            @PathVariable long id,
            Model model
    ) throws BusinessException {
        Product product = productService.findOne(id);

        if (ObjectUtils.isEmpty(product)) {
            // TODO 错误提示
            throw new BusinessException("");
        }

        model.addAttribute("bts", ProductBusinessTypeEnum.values());
        model.addAttribute("ibs", ProductBatteryTypeEnum.values());
        model.addAttribute("ids", ProductDangerTypeEnum.values());

        if (ObjectUtils.isEmpty(model.asMap().get("fele"))) {
            ProductFormVo productFormVo = BeanMapperUtils.map(product, ProductFormVo.class);
            productFormVo.setImageId(productFormVo.getImageId());
            if (!ObjectUtils.isEmpty(product.getDeadline())) {
                productFormVo.setDeadline(DateUtils.date2String1(product.getDeadline()));
            } else {
                productFormVo.setDeadline("");
            }
            model.addAttribute("fele", productFormVo.toMap());
        }

        model.addAttribute("id", product.getId());

        return "/admin/product/update";
    }

    @RequestMapping(value = "/product/{id}/update", method = RequestMethod.POST)
    public String postUpdateProduct(
            @PathVariable long id,
            @ModelAttribute("productStatus") Integer status,
            @RequestParam String json,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {
        Product product = productService.findOne(id);
        if (ObjectUtils.isEmpty(product)) {
            // TODO 错误提示
            throw new BusinessException("");
        }

        ProductTypeEnum tp = product.getType();

        ProductFormVo productFormVo = JSONMapper.fromJson(json, ProductFormVo.class);

        BeanMapperUtils.copy(productFormVo, product);

        if (StringUtils.hasText(productFormVo.getDeadline())) {
            product.setDeadline(DateUtils.stringToDate(productFormVo.getDeadline()));
        } else {
            product.setDeadline(null);
        }


        product.setType(tp);
        productRepository.save(product);

        redirectAttributes.addFlashAttribute("success", "修改货品成功！");

        if (ObjectUtils.isEmpty(status) || status == -1) {
            return "redirect:/admin/product/index";
        } else {
            return "redirect:/admin/product/index?status=" + status;
        }
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
        return "/admin/product/show";
    }
}

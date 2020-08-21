package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelRow;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.exception.FormException;
import me.rubick.common.app.helper.FormHelper;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.DateUtils;
import me.rubick.common.app.utils.ExcelHelper;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.*;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.ProductRepository;
import me.rubick.transport.app.repository.UserRepository;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

@Controller
@RequestMapping("/admin/")
@Slf4j
@SessionAttributes("productStatus")
public class AdminProductController extends AbstractController {

    @Resource
    private ProductService productService;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private UserRepository userRepository;

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

    @RequestMapping(value = "/product/import", method = RequestMethod.GET)
    public String getImportProduct() {
        return "/admin/product/import";
    }

    /**
     * 导入
     */
    @RequestMapping(value = "/product/import", method = RequestMethod.POST)
    public String postImportProduct(
            @RequestParam MultipartFile file,
            RedirectAttributes redirectAttributes
    ) throws BusinessException, CommonException {
        FormHelper formHelper = FormHelper.getInstance();
        if (file.isEmpty()) {
            formHelper.addError("file", "请上传文件！");
        }
        File tempFile = documentService.multipartFile2File(file);

        List<ExcelRow> excelRows = ExcelHelper.read(tempFile);
        log.info("{}", JSONMapper.toJSON(excelRows));

        for (ExcelRow row : excelRows) {
            User user = userRepository.findTopByHwcSn(row.getA());

            if (ObjectUtils.isEmpty(user)) {
                formHelper.addError("file", "客户编号：" + row.getA() + " 不存在，请检查！");
                break;
            }

            try {
                ProductBusinessTypeEnum.valOf(row.getB());
            }catch (BusinessException e) {
                formHelper.addError("file", "业务类型：" + row.getB() + " 不是有效的值，请检查！");
            }

            try {
                ProductBatteryTypeEnum.valOf(row.getE());
            }catch (BusinessException e) {
                formHelper.addError("file", "电池类型：" + row.getE() + " 不是有效的值，请检查！");
            }

            try {
                ProductDangerTypeEnum.valOf(row.getL());
            }catch (BusinessException e) {
                formHelper.addError("file", "是否危险品：" + row.getL() + " 不是有效的值，请检查！");
            }

            formHelper.notNull(row.getG(), "file", "【重量】不能为空！");
            formHelper.notNull(row.getH(), "file", "【长】不能为空！");
            formHelper.notNull(row.getI(), "file", "【宽】不能为空！");
            formHelper.notNull(row.getJ(), "file", "【高】不能为空！");
            formHelper.notNull(row.getM(), "file", "【申报名称】不能为空！");
            formHelper.notNull(row.getN(), "file", "【申报价值】不能为空！");

            if (! ObjectUtils.isEmpty(productRepository.findTopByProductSku(row.getD()))) {
                formHelper.addError("file", "SKU：" + row.getD() + " 已存在，请检查！");
            }
        }

        try {
            formHelper.hasError();
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), e.getForm());
            return "redirect:/admin/product/import";
        }

        for (ExcelRow row : excelRows) {
            User user = userRepository.findTopByHwcSn(row.getA());


            Product product = new Product();
            product.setUserId(user.getId());
            product.setImageId(1L);
            product.setType(ProductTypeEnum.NORMAL);
            product.setDeadline(DateUtils.stringToDate(row.getK()));
            product.setProductName(row.getC());
            product.setBusinessType(ProductBusinessTypeEnum.valOf(row.getB()).ordinal() == 1);
            product.setIsDanger(ProductDangerTypeEnum.valOf(row.getL()).ordinal() == 1);
            product.setIsBattery(ProductBatteryTypeEnum.valOf(row.getE()).ordinal() == 0);
            product.setProductSku(row.getD());
            product.setOrigin(row.getF());
            product.setWeight(new BigDecimal(row.getG()));
            product.setLength(new BigDecimal(row.getH()));
            product.setWidth(new BigDecimal(row.getI()));
            product.setHeight(new BigDecimal(row.getJ()));
            product.setQuotedName(row.getM());
            product.setQuotedPrice(row.getN());
            product.setComment(row.getO());

            log.info("{}", JSONMapper.toJSON(product));
            productService.createProduct(product);
        }

        redirectAttributes.addFlashAttribute("success", "出库单导入成功！");

        return "redirect:/admin/product/index";
    }
}

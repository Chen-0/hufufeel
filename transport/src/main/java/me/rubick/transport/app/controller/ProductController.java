package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelRow;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.exception.FormException;
import me.rubick.common.app.exception.NotFoundException;
import me.rubick.common.app.helper.FormHelper;
import me.rubick.common.app.response.RestResponse;
import me.rubick.common.app.utils.*;
import me.rubick.transport.app.constants.ProductBatteryTypeEnum;
import me.rubick.transport.app.constants.ProductBusinessTypeEnum;
import me.rubick.transport.app.constants.ProductDangerTypeEnum;
import me.rubick.transport.app.constants.ProductTypeEnum;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.ProductRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.service.cache.SimpleCacheService;
import me.rubick.transport.app.vo.DocumentVo;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ProductController extends AbstractController {

    @Resource
    private ProductService productService;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private SimpleCacheService simpleCacheService;

    @RequestMapping(value = "/product/index")
    public String indexProduct(
            Model model,
            @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false, defaultValue = "0") int type
    ) throws BusinessException {
        User user = userService.getByLogin();
        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("");
        }
        Page<Product> products = productService.findProduct(user, keyword, status, type, pageable);

        model.addAttribute("elements", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("_STATUS", status);
        model.addAttribute("TYPE", type);

        if (type == 0) {
            model.addAttribute("MENU", "HUOPINGUANLI");
        }

        return "product/index";
    }

    @RequestMapping(value = "/product/{id}/show", method = RequestMethod.GET)
    public String showProduct(
            @PathVariable("id") long id,
            Model model
    ) throws BusinessException {
        Product product = productService.findOne(id);

        if (ObjectUtils.isEmpty(product)) {
            //TODO no found
            throw new BusinessException("");
        }

        model.addAttribute("ele", product);
        model.addAttribute("MENU", "HUOPINGUANLI");
        return "/product/show";
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.GET)
    public String getCreateProduct(
            Model model
    ) throws BusinessException {
        model.addAttribute("bts", ProductBusinessTypeEnum.values());
        model.addAttribute("ibs", ProductBatteryTypeEnum.values());
        model.addAttribute("ids", ProductDangerTypeEnum.values());

        if (ObjectUtils.isEmpty(model.asMap().get("fele"))) {
            model.asMap().put("fele", new ProductFormVo());
        }

        return "product/create";
    }

    @RequestMapping(value = "/product/post_create", method = RequestMethod.POST)
    public String postCreateProduct(
            @RequestParam String json,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {
        ProductFormVo productFormVo = JSONMapper.fromJson(json, ProductFormVo.class);

        try {
            validateProduct(productFormVo);
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), productFormVo);
            return "redirect:/product/create";
        }

        //store the product
        Product product = BeanMapperUtils.map(productFormVo, Product.class);

        if (StringUtils.hasText(productFormVo.getDeadline())) {
            product.setDeadline(DateUtils.stringToDate(productFormVo.getDeadline()));
        }

        productService.createProduct(product);
        redirectAttributes.addFlashAttribute("SUCCESS", "添加货品成功！");
        return "redirect:/product/index";
    }

    private void validateProduct(ProductFormVo productFormVo) throws FormException {
        FormHelper formHelper = FormHelper.getInstance();
        formHelper.validateDefault0("businessType", productFormVo.getBusinessType());
        formHelper.validateDefault0("productName", productFormVo.getProductName());
        formHelper.validate("productSku", productFormVo.getProductSku()).notNull().maxLength(20);
        formHelper.validateDefault0("isBattery", productFormVo.getIsBattery());
        formHelper.validateDefault0("weight", productFormVo.getWeight()).mustDecimal0();
        formHelper.validateDefault0("length", productFormVo.getLength()).mustDecimal0();
        formHelper.validateDefault0("width", productFormVo.getWidth()).mustDecimal0();
        formHelper.validateDefault0("height", productFormVo.getHeight()).mustDecimal0();
        formHelper.validateDefault0("isDanger", productFormVo.getIsDanger());
        formHelper.validateDefault0("quotedName", productFormVo.getQuotedName());
        formHelper.validate("quotedPrice", productFormVo.getQuotedPrice()).notNull().mustDecimal0();

        try {
            productFormVo.setImageName(documentService.findOne(productFormVo.getImageId()).getOriginalFilename());
        } catch (NotFoundException e) {
            formHelper.addError("image", "请上传货品图片");
        }

        if (!ObjectUtils.isEmpty(productFormVo.getDeadline())) {
            formHelper.mustDate0("deadline", productFormVo.getDeadline());
        }

        formHelper.hasError();
    }

    private void validateRejectProduct(ProductFormVo productFormVo) throws FormException {
        FormHelper formHelper = FormHelper.getInstance();
        formHelper.validateDefault0("productName", productFormVo.getProductName());
        formHelper.validate("productSku", productFormVo.getProductSku()).notNull().maxLength(32);
        if (!ObjectUtils.isEmpty(productFormVo.getDeadline())) {
            formHelper.mustDate0("deadline", productFormVo.getDeadline());
        }

        formHelper.hasError();
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
            @PathVariable long id
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
            try {
                productFormVo.setImageName(documentService.findOne(productFormVo.getImageId()).getOriginalFilename());
                productFormVo.setImageId(productFormVo.getImageId());

                System.out.println(product.getDeadline());
                System.out.println(productFormVo.getDeadline());
                if (!ObjectUtils.isEmpty(product.getDeadline())) {
                    productFormVo.setDeadline(DateUtils.date2String1(product.getDeadline()));
                } else {
                    productFormVo.setDeadline("");
                }
                productFormVo.setProductSku(productFormVo.getProductSku().substring(7));
            } catch (NotFoundException e) {
                ;
            }
            System.out.println(JSONMapper.toJSON(productFormVo.toMap()));
            model.addAttribute("fele", productFormVo.toMap());
        }

        model.addAttribute("id", product.getId());

        return "/product/update";
    }


    @RequestMapping(value = "/product/{id}/post_update", method = RequestMethod.POST)
    public String postUpdateProduct(
            @PathVariable long id,
            @RequestParam String json,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {
        Product product = productService.findOne(id);
        if (ObjectUtils.isEmpty(product)) {
            // TODO 错误提示
            throw new BusinessException("");
        }

        ProductFormVo productFormVo = JSONMapper.fromJson(json, ProductFormVo.class);

        try {
            validateProduct(productFormVo);
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), productFormVo);
            return "redirect:/product/" + id + "/update";
        }

        //store the product
        BeanMapperUtils.copy(productFormVo, product);

        if (StringUtils.hasText(productFormVo.getDeadline())) {
            product.setDeadline(DateUtils.stringToDate(productFormVo.getDeadline()));
        } else {
            product.setDeadline(null);
        }

        log.info("{}", JSONMapper.toJSON(product));

        productService.createProduct(product);
        redirectAttributes.addFlashAttribute("SUCCESS", "修改货品成功！");
        return "redirect:/product/index";
    }

    /**
     * 删除商品
     */
    @RequestMapping("/product/{id}/remove")
    public String deleteProduct(
            @RequestParam(defaultValue = "0", required = false) String type,
            @PathVariable("id") long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("SUCCESS", "删除商品成功！");
            return "redirect:/product/index?type=" + type;
        } catch (BusinessException e) {
            log.error("", e);
            redirectAttributes.addFlashAttribute("ERROR", e.getMessage());
            return "redirect:/product/index?type=" + type;
        }
    }


    /**
     * 添加商品至发货清单中
     */
    @RequestMapping(value = "/product/select", method = RequestMethod.POST)
    public String selectProduct(
            @RequestParam("trackingNumber[]") List<Long> trackingNumbers,
            @RequestParam int type,
            RedirectAttributes redirectAttributes,
            Model model) throws BusinessException {
        if (!(type == 0 || type == 1)) {
            throw new BusinessException("无效的参数");
        }
        User user = userService.getByLogin();
        simpleCacheService.get(user.getId())[type].getProducts().addAll(trackingNumbers);

        if (type == 0) {
            redirectAttributes.addFlashAttribute("SUCCESS", "货品已经成功添加至发货单中");
        } else if (type == 1) {
            redirectAttributes.addFlashAttribute("SUCCESS", "退货货品已经成功添加至退货单中");
        }

        return MessageFormat.format("redirect:/product/ready_to_send?type={0}", type);
    }

    /**
     * 发货清单
     */
    @RequestMapping(value = "/product/ready_to_send")
    public String readyToSend(
            @RequestParam(required = false, defaultValue = "0") int type,
            Model model
    ) throws BusinessException {
        if (!(type == 0 || type == 1)) {
            throw new BusinessException("无效的参数");
        }
        User user = userService.getByLogin();
        log.info("{}", JSONMapper.toJSON(simpleCacheService.get(user.getId())));
        List<Product> products = productService.findProducts(simpleCacheService.get(user.getId())[type].getProducts());
        List<Warehouse> warehouses = warehouseRepository.findAll();
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("elements", products);
        model.addAttribute("TYPE", type);
        return "/product/ready_to_send";
    }

    @RequestMapping(value = "/product/package/{id}/remove", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> removeProductFromPackage(
            @PathVariable("id") long id,
            @RequestParam int type,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {
        if (!(type == 0 || type == 1)) {
            throw new BusinessException("无效的参数");
        }

        User user = userService.getByLogin();
        if (simpleCacheService.get(user.getId())[type].getProducts().contains(id)) {
            simpleCacheService.get(user.getId())[type].getProducts().remove(id);
        }

        return new RestResponse<>();
    }

    @RequestMapping("/product/package/remove_all")
    public String removeAllProductFromPackage(
            @RequestParam int type
    ) throws BusinessException {
        if (!(type == 0 || type == 1)) {
            throw new BusinessException("无效的参数");
        }

        User user = userService.getByLogin();
        simpleCacheService.get(user.getId())[type].getProducts().clear();

        return MessageFormat.format("redirect:/product/ready_to_send?type={0}", type);
    }

    @RequestMapping("/product/image/upload")
    @ResponseBody
    public RestResponse<DocumentVo> uploadImage(
            @RequestParam MultipartFile image
    ) {
        try {
            Document document = documentService.uploadProductImage(image);
            return new RestResponse<>(BeanMapperUtils.map(document, DocumentVo.class));
        } catch (BusinessException e) {
            e.printStackTrace();
            log.error("", e);
            return new RestResponse<>(e.getMessage());
        }

//        return new RestResponse<>("error");
    }

    @RequestMapping("/product/reject/create")
    public String rejectProductCreate(Model model) {
        model.addAttribute("bts", ProductBusinessTypeEnum.values());
        model.addAttribute("ibs", ProductBatteryTypeEnum.values());
        model.addAttribute("ids", ProductDangerTypeEnum.values());

        if (ObjectUtils.isEmpty(model.asMap().get("fele"))) {
            model.asMap().put("fele", new ProductFormVo());
        }

        return "product/reject/create";
    }

    @RequestMapping(value = "/product/reject/post_create")
    public String rejectProductCreatePost(
            @RequestParam String json,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {
        ProductFormVo productFormVo = JSONMapper.fromJson(json, ProductFormVo.class);

        try {
            validateRejectProduct(productFormVo);
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), productFormVo);
            return "redirect:/product/reject/create";
        }

        //store the product
        Product product = BeanMapperUtils.map(productFormVo, Product.class);
        BeanMapperUtils.setDefault(product, "weight");
        BeanMapperUtils.setDefault(product, "length");
        BeanMapperUtils.setDefault(product, "width");
        BeanMapperUtils.setDefault(product, "height");
        BeanMapperUtils.setDefault(product, "quotedName");
        BeanMapperUtils.setDefault(product, "quotedPrice");

        if (ObjectUtils.isEmpty(product.getImageId()) || product.getImageId() == 0) {
            product.setImageId(1L);
        }

        if (StringUtils.hasText(productFormVo.getDeadline())) {
            product.setDeadline(DateUtils.stringToDate(productFormVo.getDeadline()));
        }

        productService.createRejectProduct(product);
        redirectAttributes.addFlashAttribute("SUCCESS", "添加退货货品成功！");
        return "redirect:/product/index?type=1";
    }

    @RequestMapping(value = "/product/import", method = RequestMethod.GET)
    public String getImportProduct() {
        return "/product/import";
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

        User user = userService.getByLogin();

        for (ExcelRow row : excelRows) {
            try {
                ProductBusinessTypeEnum.valOf(row.getA());
            }catch (BusinessException e) {
                formHelper.addError("file", "业务类型：" + row.getA() + " 不是有效的值，请检查！");
            }

            try {
                ProductBatteryTypeEnum.valOf(row.getD());
            }catch (BusinessException e) {
                formHelper.addError("file", "电池类型：" + row.getD() + " 不是有效的值，请检查！");
            }

            try {
                ProductDangerTypeEnum.valOf(row.getK());
            }catch (BusinessException e) {
                formHelper.addError("file", "是否危险品：" + row.getK() + " 不是有效的值，请检查！");
            }

            formHelper.notNull(row.getF(), "file", "【重量】不能为空！");
            formHelper.notNull(row.getG(), "file", "【长】不能为空！");
            formHelper.notNull(row.getH(), "file", "【宽】不能为空！");
            formHelper.notNull(row.getI(), "file", "【高】不能为空！");
            formHelper.notNull(row.getL(), "file", "【申报名称】不能为空！");
            formHelper.notNull(row.getM(), "file", "【申报价值】不能为空！");

            String sku = user.getHwcSn() + "-" + row.getC();
            if (! ObjectUtils.isEmpty(productRepository.findTopByProductSku(sku))) {
                formHelper.addError("file", "SKU：" + sku + " 已存在，请检查！");
            }
        }

        try {
            formHelper.hasError();
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), e.getForm());
            return "redirect:/admin/product/import";
        }

        for (ExcelRow row : excelRows) {
            Product product = new Product();
            product.setUserId(user.getId());
            product.setImageId(1L);
            product.setType(ProductTypeEnum.NORMAL);
            product.setDeadline(DateUtils.stringToDate(row.getJ()));
            product.setProductName(row.getB());
            product.setBusinessType(ProductBusinessTypeEnum.valOf(row.getA()).ordinal() == 1);
            product.setIsDanger(ProductDangerTypeEnum.valOf(row.getK()).ordinal() == 1);
            product.setIsBattery(ProductBatteryTypeEnum.valOf(row.getD()).ordinal() == 0);
            product.setProductSku(row.getC());
            product.setOrigin(row.getE());
            product.setWeight(new BigDecimal(row.getF()));
            product.setLength(new BigDecimal(row.getG()));
            product.setWidth(new BigDecimal(row.getH()));
            product.setHeight(new BigDecimal(row.getI()));
            product.setQuotedName(row.getL());
            product.setQuotedPrice(row.getM());
            product.setComment(row.getN());

            log.info("{}", JSONMapper.toJSON(product));
            productService.createProduct(product);
        }

        redirectAttributes.addFlashAttribute("success", "货品导入成功！");

        return "redirect:/product/index";
    }
}

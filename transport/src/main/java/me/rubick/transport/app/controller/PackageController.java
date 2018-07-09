package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelConverter;
import me.rubick.common.app.exception.*;
import me.rubick.common.app.helper.FormHelper;
import me.rubick.common.app.response.RestResponse;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.ExcelHelper;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.PackageStatusEnum;
import me.rubick.transport.app.constants.PackageTypeEnum;
import me.rubick.transport.app.constants.StatementTypeEnum;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.repository.SwitchSkuRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.*;
import me.rubick.transport.app.vo.PackageExcelVo;
import me.rubick.transport.app.vo.ProductWarehouseVo;
import me.rubick.transport.app.vo.SwitchSkuFormVo;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Controller
@Slf4j
public class PackageController extends AbstractController {

    @Resource
    private PackageRepository packageRepository;

    @Resource
    private PackageService packageService;

    @Resource
    private ProductService productService;

    @Resource
    private PayService payService;

    @RequestMapping(value = "/package/index", method = RequestMethod.GET)
    public String packageIndex(
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false, defaultValue = "0") int type,
            Model model
    ) {
        User user = userService.getByLogin();
        Page<Package> packages = packageService.searchPackage(keyword, user, status, type, pageable);
        model.addAttribute("elements", packages);
        model.addAttribute("_STATUS", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("MENU", "RUKUGUANLI");
        model.addAttribute("TYPE", type);


//        if (status != null && status == PackageStatusEnum.FREEZE.ordinal()) {
        Map<String, Statements> map = payService.findUnpayStatementsByUserIdAndType(user.getId(), Arrays.asList(StatementTypeEnum.SJ, StatementTypeEnum.RK));
        model.addAttribute("smap", map);
//        }
        return "/package/index";
    }

    @RequestMapping("/package/{id}/show")
    public String showPackage(
            @PathVariable long id,
            Model model
    ) throws HttpNoFoundException {
        Package p = packageRepository.findOne(id);
        User user = userService.getByLogin();

        if (p.getUserId() != user.getId()) {
            throw new HttpNoFoundException();
        }

        List<Statements> statements = payService.findByUserIdAndTypeIn(
                p.getId(), Arrays.asList(StatementTypeEnum.SJ, StatementTypeEnum.RK)
        );
        model.addAttribute("statements", statements);
        model.addAttribute("ele", p);
        return "/package/show";
    }

    /**
     * 提交发货清单
     */
    @RequestMapping(value = "/package/create", method = RequestMethod.POST)
    public String createPackage(
            @RequestParam("w") long wId,
            @RequestParam(required = false, defaultValue = "") String contact,
            @RequestParam(required = false, defaultValue = "") String referenceNumber,
            @RequestParam(required = false, defaultValue = "") String comment,
            @RequestParam("qty[]") List<Integer> qtys,
            @RequestParam("p[]") List<Long> pids,
            @RequestParam int type,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {
        if (!(type == 0 || type == 1)) {
            throw new BusinessException("无效的参数");
        }

        Warehouse warehouse = warehouseRepository.findOne(wId);

        if (ObjectUtils.isEmpty(warehouse)) {
            throw new BusinessException("[A001] 禁止访问");
        }

        User user = userService.getByLogin();

        List<Product> products = productService.findProducts(pids);

        if (products.size() != qtys.size()) {
            throw new BusinessException("[A001] 禁止访问");
        }

        packageService.create(user, warehouse, referenceNumber, contact, comment, qtys, pids, PackageTypeEnum.valueOf(type));

        redirectAttributes.addFlashAttribute("SUCCESS", "入库单创建成功！");

        if (type == 1) {
            return "redirect:/package/index?type=1";
        }
        return "redirect:/package/index";
    }

    @RequestMapping(value = "/package/{id}/cancel")
    public String cancelPackage(
            @PathVariable("id") long id,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {
        User user = userService.getByLogin();

        Package p = packageRepository.getOne(id);

        if (ObjectUtils.isEmpty(p)) {
            throw new BusinessException("");
        }

        if (user.getId() != p.getUserId()) {
            throw new BusinessException("");
        }

        if (!p.getStatus().equals(PackageStatusEnum.READY)) {
            throw new BusinessException("");
        }

        p.setStatus(PackageStatusEnum.CANCEL);
        packageRepository.save(p);

        redirectAttributes.addFlashAttribute("SUCCESS", "取消入库单成功！");
        return "redirect:/package/index";
    }


    /* ------------------------------------------------------------------------------ */
    @Resource
    private StockService stockService;

    @Resource
    private WarehouseRepository warehouseRepository;

    /**
     * 库存管理
     *
     * @param model
     * @param pageable
     * @param keyword
     * @param wIds
     * @return
     */
    @RequestMapping(value = "/stock/index", method = RequestMethod.GET)
    public String index(
            Model model,
            @PageableDefault(size = 15, direction = Sort.Direction.DESC, sort = {"updatedAt", "id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(value = "w[]", required = false) List<Long> wIds
    ) {
        User user = userService.getByLogin();

        List<Warehouse> warehouses = warehouseRepository.findAll();
        Page<ProductWarehouse> productWarehouses = stockService.findAvailableStockByUser(user, pageable, keyword, wIds);

        model.addAttribute("elements", productWarehouses);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("keyword", keyword);
        model.addAttribute("ws", wIds);
        return "/package/stock";
    }

    @RequestMapping("/ajax/stock/get_available")
    @ResponseBody
    public RestResponse<List<ProductWarehouseVo>> ajaxGetStockByWarehouse(
            @RequestParam long wid
    ) {
        User user = userService.getByLogin();
        Warehouse warehouse = warehouseRepository.getOne(wid);

        List<ProductWarehouse> warehouses = stockService.findAvailableStockByUser(user, warehouse);
        List<ProductWarehouseVo> productWarehouseVos = BeanMapperUtils.mapList(warehouses, ProductWarehouseVo.class);

        int max = warehouses.size();
        for (int i = 0; i < max; i++) {
            productWarehouseVos.get(i).setProductName(warehouses.get(i).getProduct().getProductName());
            productWarehouseVos.get(i).setProductSku(warehouses.get(i).getProduct().getProductSku());
        }

        return new RestResponse<>(productWarehouseVos);
    }

    @RequestMapping("/ajax/stock/get_stock")
    @ResponseBody
    public RestResponse<ProductWarehouseVo> ajaxGetStockByProductSku(
            @RequestParam String productSku,
            @RequestParam int qty,
            @RequestParam long wid
    ) {
        productSku = productSku.trim();
        User user = userService.getByLogin();
        ProductWarehouse productWarehouse = stockService.findAvailableStockByProductSku(productSku, user, wid);
        if (ObjectUtils.isEmpty(productWarehouse)) {
            return new RestResponse<>("货品不存在");
        }

        if (productWarehouse.getQuantity() < qty) {
            return new RestResponse<>("库存不足");
        }
        ProductWarehouseVo productWarehouseVo = BeanMapperUtils.map(productWarehouse, ProductWarehouseVo.class);

        productWarehouseVo.setProductName(productWarehouse.getProduct().getProductName());
        productWarehouseVo.setProductSku(productWarehouse.getProduct().getProductSku());

        return new RestResponse<>(productWarehouseVo);
    }

    @RequestMapping(value = "/package/import", method = RequestMethod.GET)
    public String getImportPackage(Model model) {
        List<Warehouse> warehouses = warehouseRepository.findAll();

        model.addAttribute("warehouses", warehouses);
        return "package/import";
    }

    @RequestMapping(value = "/package/import", method = RequestMethod.POST)
    public String importPackage(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam long wid,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User user = userService.getByLogin();
            File file = documentService.multipartFile2File(multipartFile);
            ExcelHelper<PackageExcelVo> excelHepler = new ExcelHelper<>();
            List<PackageExcelVo> packageExcelVos = excelHepler.readToObject(file, new ExcelConverter<PackageExcelVo>() {
                @Override
                public PackageExcelVo read(Row row) throws BusinessException {
                    PackageExcelVo packageExcelVo = new PackageExcelVo();
                    packageExcelVo.setSKU(ExcelHelper.getValue(row, 0, false));
                    try {
                        packageExcelVo.setQuantity(new BigDecimal(ExcelHelper.getValue(row, 1, false)).intValue());
                    } catch (NumberFormatException e) {
                        throw new BusinessException("请检查SKU:" + packageExcelVo.getSKU() + "的数量，必须为整数！");
                    }

                    return packageExcelVo;
                }
            });

            List<Integer> qtys = new ArrayList<>();
            List<String> skus = new ArrayList<>();
            Set<String> set = new HashSet<>();

            for (PackageExcelVo p : packageExcelVos) {
                qtys.add(p.getQuantity());
                skus.add(p.getSKU());

                if (set.contains(p.getSKU())) {
                    throw new BusinessException("错误！SKU：" + p.getSKU() + "含有一个或多个！");
                }
                set.add(p.getSKU());
            }

            packageService.create(user, warehouseRepository.findOne(wid), qtys, skus);

        } catch (BusinessException e) {
            log.warn("", e);
            redirectAttributes.addFlashAttribute("warn", e.getMessage());
            return "redirect:/package/import";
        } catch (CommonException e) {
            log.warn("", e);
            redirectAttributes.addFlashAttribute("warn", e.getMessage());
            return "redirect:/package/import";
        }

        redirectAttributes.addFlashAttribute("SUCCESS", "导入入库单成功！");
        return "redirect:/package/import";
    }

    @RequestMapping("/package/{id}/print")
    public String printCode(
            @PathVariable long id,
            @RequestParam String type,
            Model model
    ) throws BusinessException {
        if (type.equals("sku")) {
            Package p = packageRepository.findOne(id);

            model.addAttribute("items", p.getPackageProducts());

            return "/package/print";
        } else {
            throw new BusinessException("");
        }
    }

    @RequestMapping("/package/{id}/print_package")
    public String printPackage(
            @PathVariable long id,
            Model model
    ) throws HttpNoFoundException {
        Package p = packageService.findOne(id);
        User user = userService.getByLogin();

        if (p.getUserId() != user.getId()) {
            throw new HttpNoFoundException();
        }

        model.addAttribute("ele", p);

        return "/package/print_package";
    }

    @RequestMapping(value = "/stock/switch_sku", method = RequestMethod.GET)
    public String switchSku(Model model) {

        if (ObjectUtils.isEmpty(model.asMap().get("fele"))) {
            model.addAttribute("fele", new SwitchSkuFormVo());
        }

        return "/stock/switch_sku";
    }

    @Resource
    private SwitchSkuRepository switchSkuRepository;


    @RequestMapping(value = "/stock/switch_sku", method = RequestMethod.POST)
    public String postSwitchSku(
            @RequestParam String json,
            RedirectAttributes redirectAttributes
    ) {
        SwitchSkuFormVo formVo = JSONMapper.fromJson(json, SwitchSkuFormVo.class);
        User user = userService.getByLogin();

        try {
            FormHelper formHelper = FormHelper.getInstance();
            formHelper.validateDefault0("sku", formVo.getSku());

            try {
                formVo.setUploadfileName(documentService.findOne(formVo.getUploadfileId()).getOriginalFilename());
            } catch (NotFoundException e) {
                formHelper.addError("uploadfile", "请上传货品图片");
            }

            formHelper.hasError();
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), formVo);
            return "redirect:/stock/switch_sku";
        }

        SwitchSku switchSku = BeanMapperUtils.map(formVo, SwitchSku.class);
        switchSku.setDocId(formVo.getUploadfileId());
        switchSku.setUserId(user.getId());
        switchSkuRepository.save(switchSku);

        redirectAttributes.addFlashAttribute("SUCCESS", "提交换标信息成功！");
        return "redirect:/switch_sku/index";
    }

    @RequestMapping(value = "/switch_sku/index", method = RequestMethod.GET)
    public String indexSwitchSku(Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        List<SwitchSku> switchSkus = switchSkuRepository.findAll(sort);

        model.addAttribute("elements", switchSkus);

        return "/stock/switch_sku_index";
    }
}


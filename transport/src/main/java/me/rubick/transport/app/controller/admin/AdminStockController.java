package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelRow;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.exception.FormException;
import me.rubick.common.app.helper.FormHelper;
import me.rubick.common.app.utils.ExcelHelper;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.OrderStatusEnum;
import me.rubick.transport.app.constants.ProductStatusEnum;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.*;
import me.rubick.transport.app.service.StockService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class AdminStockController extends AbstractController {

    @Resource
    private SwitchSkuRepository switchSkuRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private StockService stockService;

    @Resource
    private ProductWarehouseRepository productWarehouseRepository;

    @RequestMapping(value = "/admin/stock/{id}/update", method = RequestMethod.GET)
    public String getUpdateStock(
            @PathVariable long id,
            Model model
    ) {
        ProductWarehouse productWarehouse = productWarehouseRepository.findOne(id);

        Map<String, String> map = new HashMap<>();
        map.put("quantity", String.valueOf(productWarehouse.getQuantity()));
        map.put("location", productWarehouse.getProduct().getLocation());

        model.addAttribute("ele", productWarehouse);
        model.addAttribute("fele", map);
        return "/admin/store/update";
    }

    @RequestMapping(value = "/admin/stock/{id}/update", method = RequestMethod.POST)
    public String postUpdateStock(
            @PathVariable long id,
            @RequestParam int quantity,
            @RequestParam(defaultValue = "", required = false) String location,
            RedirectAttributes redirectAttributes
    ) {
        ProductWarehouse productWarehouse = productWarehouseRepository.findOne(id);
        productWarehouse.setQuantity(quantity);
        productWarehouseRepository.save(productWarehouse);

        Product product = productWarehouse.getProduct();
        product.setLocation(location);
        productRepository.save(product);

        redirectAttributes.addFlashAttribute("success", "修改成功！");

        return "redirect:/admin/stock/index";
    }

    @RequestMapping(value = "/admin/stock/import", method = RequestMethod.GET)
    public String importStock() {
        return "/admin/store/import";
    }

    /**
     * 导入库存
     */
    @RequestMapping(value = "/admin/stock/import", method = RequestMethod.POST)
    public String postImportStock(
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

            Warehouse warehouse = warehouseRepository.findTopByNameAndVisible(row.getC(), true);

            if (ObjectUtils.isEmpty(warehouse)) {
                formHelper.addError("file", "仓库：" + row.getC() + " 不存在，请检查！");
                break;
            }

            Product product = productRepository.findTopByProductSku(row.getB());

            if (ObjectUtils.isEmpty(product)) {
                formHelper.addError("file", "SKU：" + row.getB() + " 不存在！");
                break;
            }

            if (! product.getStatus().equals(ProductStatusEnum.READY_CHECK)) {
                formHelper.addError("file", "SKU：" + row.getB() + " 未审核成功！");
                break;
            }

            if (product.getUserId() != user.getId()) {
                formHelper.addError("file", "SKU：" + row.getB() + " 不属于客户"+row.getA()+"！");
                break;
            }

            try {
                BigDecimal b = new BigDecimal(row.getD());
            } catch (NumberFormatException e) {
                formHelper.addError("file", "SKU：" + row.getB() + " 中，【数量】不合法！");
                break;
            }
        }

        try {
            formHelper.hasError();
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), e.getForm());
            return "redirect:/admin/stock/import";
        }

        for (ExcelRow row : excelRows) {
            User user = userRepository.findTopByHwcSn(row.getA());
            Product product = productRepository.findTopByProductSku(row.getB());
            Warehouse warehouse = warehouseRepository.findTopByNameAndVisible(row.getC(), true);
            int qty = new BigDecimal(row.getD()).intValue();

            stockService.addStock(user.getId(), product.getId(), warehouse.getId(), qty);
        }

        redirectAttributes.addFlashAttribute("success", "出库单导入成功！");
        return "redirect:/admin/stock/index";
    }

    // -----------------------------------------------------------------------------

    @RequestMapping(value = "/admin/switch_sku/index", method = RequestMethod.GET)
    public String indexSwitchSku(Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        List<SwitchSku> switchSkus = switchSkuRepository.findAll(sort);

        model.addAttribute("elements", switchSkus);

        return "/admin/store/switch_sku_index";
    }

    @RequestMapping(value = "/admin/switch_sku/{id}/update", method = RequestMethod.GET)
    public String getUpdateSwitchSku(
            @PathVariable long id,
            Model model
    ) {
        SwitchSku switchSku = switchSkuRepository.findOne(id);
        model.addAttribute("ele", switchSku);
        model.addAttribute("fele", switchSku);
        return "/admin/store/update_sku";
    }

    @RequestMapping(value = "/admin/switch_sku/{id}/update", method = RequestMethod.POST)
    public String postUpdateSwitchSku(
            @PathVariable long id,
            @RequestParam int qty,
            @RequestParam String size,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        SwitchSku switchSku = switchSkuRepository.findOne(id);
        switchSku.setSize(size);
        switchSku.setQty(qty);

        switchSkuRepository.save(switchSku);
        model.addAttribute("ele", switchSku);
        redirectAttributes.addFlashAttribute("success", "修改换标信息成功！");
        return "redirect:/admin/switch_sku/index";
    }
}

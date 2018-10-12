package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelRow;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.response.RestResponse;
import me.rubick.common.app.utils.ExcelHelper;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.OrderStatusEnum;
import me.rubick.transport.app.constants.PackageStatusEnum;
import me.rubick.transport.app.constants.StatementTypeEnum;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.PackageBoxRepository;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.repository.UserRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.PackageService;
import me.rubick.transport.app.service.PayService;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.service.StockService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminPackageController extends AbstractController {

    @Resource
    private PackageService packageService;

    @Resource
    private PackageRepository packageRepository;

    @Resource
    private ProductService productService;

    @Resource
    private StockService stockService;

    @Resource
    private PayService payService;

    @Resource
    private UserRepository userRepository;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private PackageBoxRepository packageBoxRepository;


    @RequestMapping("/package/index")
    public String adminPackageIndex(
            Model model,
            @PageableDefault(size = 25, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status
    ) {
        Page<Package> packages = packageService.searchPackage(keyword, null, status, -1, pageable);
        model.addAttribute("elements", packages);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);
        return "/admin/package/index";
    }

    @RequestMapping("/package/_index")
    public String admin_PackageIndex(Model model) {
        List<Package> packages = packageRepository.findAllByIsDelete(true);

        model.addAttribute("elements", packages);
        model.addAttribute("status", -1);
        model.addAttribute("keyword", "");
        return "/admin/package/_index";
    }

    @RequestMapping("/package/{id}/recover")
    public String recover(
            @PathVariable long id,
            RedirectAttributes redirectAttributes
    ) {
        Package p = packageService.findOne(id);

        if (ObjectUtils.isEmpty(p)) {
            return "redirect:/admin/package/_index";
        }

        p.setIsDelete(false);
        packageRepository.save(p);

        redirectAttributes.addFlashAttribute("success", "恢复成功！");

        return "redirect:/admin/package/_index";
    }

    @RequestMapping(value = "/package/{id}/show", method = RequestMethod.GET)
    public String adminPackageShow(
            @PathVariable long id,
            Model model
    ) {
        Package p = packageRepository.findOne(id);
        List<Statements> statements = payService.findByUserIdAndTypeIn(
                p.getId(), Arrays.asList(StatementTypeEnum.SJ, StatementTypeEnum.RK)
        );
        model.addAttribute("statements", statements);
        model.addAttribute("ele", p);
        return "/admin/package/show";
    }

    //--------------------------- 查看库存 ---------------------------------------
    @RequestMapping("/stock/index")
    public String indexStore(
            @PageableDefault(size = 25, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(value = "w[]", required = false) List<Long> wIds,
            @RequestParam(required = false, name = "uid") Long userId,
            Model model
    ) {
        User user = null;
        if (!ObjectUtils.isEmpty(userId)) {
            user = userRepository.findOne(userId);
        }
        List<Warehouse> warehouses = warehouseRepository.findAll();
        Page<ProductWarehouse> productWarehouses = stockService.findAvailableStockByUser(user, pageable, keyword, wIds);
        List<User> users = userService.findAll("ROLE_HWC", null);

        model.addAttribute("users", users);
        model.addAttribute("elements", productWarehouses);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("keyword", keyword);
        model.addAttribute("ws", wIds);
        return "/admin/store/index";
    }

    //--------------------------- 查看库存 ---------------------------------------

    /**
     * 入库
     */
    @RequestMapping(value = "/package/{id}/inbound", method = RequestMethod.GET)
    public String getPackageInbound(
            Model model,
            @PathVariable("id") long id

    ) {
        Package p = packageRepository.findOne(id);
        model.addAttribute("o", p);
        return "/admin/package/inbound";
    }

    @RequestMapping(value = "/package/{id}/inbound", method = RequestMethod.POST)
    public String changePackageStatus(
            @PathVariable("id") long id,
            @RequestParam("qty[]") List<BigDecimal> qty,
            @RequestParam("p[]") List<Long> pIds,
            @RequestParam(required = false, name = "total_fee") BigDecimal total,
            RedirectAttributes redirectAttributes
    ) {
        List<Product> products = productService.findProducts(pIds);

        int count = products.size();
        if (count != 0 && count == pIds.size() && qty.size() == count) {
            Package p = packageService.inbound(id);
            redirectAttributes.addFlashAttribute("success", "操作成功！");

            BigDecimal tWeight = BigDecimal.ZERO;

            for (BigDecimal b : qty) {
                tWeight = tWeight.add(b);
            }

            //////////////////////  收入库费  /////////////////////////////////
            Statements statements = payService.saveStatements(payService.calcCK(p, tWeight), total);
            boolean flag = payService.payStatements(statements.getId());

            if (flag) {
                messageService.send(
                        p.getUserId(),
                        "/package/" + p.getId() + "/show",
                        MessageFormat.format("单号：{0}在{1}入库成功！", p.getSn(), p.getWarehouseName())
                );
            } else {
                p.setNextStatus(p.getStatus());
                p.setStatus(PackageStatusEnum.FREEZE);
                packageRepository.save(p);

                messageService.send(
                        p.getUserId(),
                        "/user/statements/index",
                        MessageFormat.format("单号：{0}，扣费失败，请充值账号并重新缴费。", p.getSn(), p.getWarehouseName())
                );
            }
        }


        return "redirect:/admin/package/index";
    }

    @RequestMapping(value = "/package/{id}/publish", method = RequestMethod.GET)
    public String getPackagePublish(
            Model model,
            @PathVariable("id") long id

    ) {
        Package p = packageRepository.findOne(id);
        model.addAttribute("o", p);
        return "/admin/package/publish";
    }

    @RequestMapping(value = "/package/{id}/publish", method = RequestMethod.POST)
    public String postPackagePublish(
            Model model,
            @PathVariable("id") long id,
            @RequestParam("qty[]") List<Integer> qty,
            @RequestParam("p[]") List<Long> pIds,
            @RequestParam(required = false) BigDecimal total,
            @RequestParam(required = false, defaultValue = "") String location,
            RedirectAttributes redirectAttributes

    ) {

        List<Product> products = productService.findProducts(pIds);
        Package p = packageRepository.findOne(id);

        if (ObjectUtils.isEmpty(p)) {
            return "redirect:/admin/package/index";
        }

        if (! p.getStatus().equals(PackageStatusEnum.RECEIVED)) {
            return "redirect:/admin/package/index";
        }

        int count = products.size();
        if (!(count != 0 && count == pIds.size() && qty.size() == count) || ObjectUtils.isEmpty(p)) {
            redirectAttributes.addFlashAttribute("error", "上架失败！");
            return "redirect:/admin/package/index";
        }

        packageService.inbound(p.getId(), products, qty);
        packageService.saveLocation(p, location);

        Statements statements = payService.saveStatements(payService.calcSJ(p), total);
        boolean flag = payService.payStatements(statements.getId());

        if (flag) {
            //支付成功
            messageService.send(
                    p.getUserId(),
                    "/package/" + p.getId() + "/show",
                    MessageFormat.format("入库单上架成功，单号：{0}，上架成功！费用已从您的账户里扣除。", p.getSn(), p.getWarehouseName())
            );
            stockService.addStock(p);
        } else {
            p.setNextStatus(PackageStatusEnum.FINISH);
            p.setStatus(PackageStatusEnum.FREEZE);
            packageRepository.save(p);

            messageService.send(
                    p.getUserId(),
                    "/user/statements/index",
                    MessageFormat.format("入库单上架失败，单号：{0}，扣费失败，请充值账号并重新缴费。", p.getSn(), p.getWarehouseName())
            );
        }

        redirectAttributes.addFlashAttribute("success", "上架成功！");
        return "redirect:/admin/package/index";
    }

    @RequestMapping("/package/{id}/remove")
    public String deletePackage(
            @PathVariable long id,
            RedirectAttributes redirectAttributes
    ) {
        packageService.deletePackage(id);
        redirectAttributes.addFlashAttribute("success", "删除成功！");
        return "redirect:/admin/package/index";
    }


    @RequestMapping(value = "/package/{id}/update_search_no", method = RequestMethod.GET)
    public String updatePackageSearchNo(
            @PathVariable long id,
            Model model
    ) {
        Package p = packageRepository.findOne(id);
        List<Statements> statements = payService.findByUserIdAndTypeIn(
                p.getId(), Arrays.asList(StatementTypeEnum.SJ, StatementTypeEnum.RK)
        );
        model.addAttribute("statements", statements);
        model.addAttribute("ele", p);

        return "/admin/package/update_search_no";
    }

    @RequestMapping(value = "/package/{id}/update_search_no", method = RequestMethod.POST)
    public String postUpdatePackageSearchNo(
            @PathVariable long id,
            @RequestParam(defaultValue = "") String searchNo,
            RedirectAttributes redirectAttributes
    ) {
        Package p = packageService.findOne(id);
        p.setSearchNo(searchNo);

        packageRepository.save(p);

        redirectAttributes.addFlashAttribute("success", "修改成功！");

        return "redirect:/admin/package/index";
    }

    @RequestMapping(value = "/package/{id}/inbound_reject", method = RequestMethod.GET)
    public String getInboundReject(
            @PathVariable long id,
            Model model
    ) {

        Package p = packageRepository.findOne(id);
        if (ObjectUtils.isEmpty(p)) {
            return "redirect:/admin/package/index";
        }
        Statements statements = payService.calcTHRK(p);
        List<PackageBox> packageBoxes = packageBoxRepository.findAllByPackageId(p.getId());

        model.addAttribute("pb", packageBoxes);
        model.addAttribute("o", p);
        model.addAttribute("s", statements);
        return "/admin/package/inbound_reject";
    }

    /**
     * 退货入库
     *
     * @param id
     * @param qty
     * @param pIds
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/package/{id}/inbound_reject", method = RequestMethod.POST)
    public String postInboundReject(
            @PathVariable("id") long id,
            @RequestParam("qty[]") List<Integer> qty,
            @RequestParam("p[]") List<Long> pIds,
            @RequestParam(required = false) MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        List<Product> products = productService.findProducts(pIds);
        Package p = packageRepository.findOne(id);

        int count = products.size();
        if (count != 0 && count == pIds.size() && qty.size() == count) {
            packageService.inboundReject(id, products, qty);

            redirectAttributes.addFlashAttribute("success", "操作成功！");
        }

        if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
            try {
                File tempFile = documentService.multipartFile2File(file);
                List<PackageBox> packageBoxes = new ArrayList<>();
                List<ExcelRow> excelRows = ExcelHelper.read(tempFile);

                log.info("{}", JSONMapper.toJSON(excelRows));

                for (ExcelRow row : excelRows) {
                    String text = row.getA();
                    if (StringUtils.hasText(text)) {
                        PackageBox packageBox = new PackageBox();
                        packageBox.setBoxNo(text);
                        packageBox.setPackageId(p.getId());
                        packageBoxes.add(packageBox);
                    }
                }

                if (!ObjectUtils.isEmpty(packageBoxes)) {
                    packageBoxRepository.save(packageBoxes);
                }


            } catch (Exception e) {
                log.error("", e);
            }

            redirectAttributes.addFlashAttribute("success", "操作成功！");
        }

        return "redirect:/admin/package/" + p.getId() + "/inbound_reject";
    }

    @RequestMapping(value = "/package/{id}/finish_inbound_reject", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<Integer> finishInboundReject(
            @PathVariable("id") long id,
            @RequestParam(required = false, name = "total_fee") BigDecimal total,
            RedirectAttributes redirectAttributes
    ) {

        Package p = packageRepository.findOne(id);
        if (ObjectUtils.isEmpty(p)) {
            return new RestResponse<>("入库单不存在");
        }

        if (! p.getStatus().equals(PackageStatusEnum.READY)) {
            return new RestResponse<>("入库单已经入库了");
        }

        packageService.inbound(p.getId());
        //////////////////////  收入库费  /////////////////////////////////
        Statements statements = payService.saveStatements(payService.calcTHRK(p), total);
        boolean flag = payService.payStatements(statements.getId());

        if (flag) {
            messageService.send(
                    p.getUserId(),
                    "/package/" + p.getId() + "/show",
                    MessageFormat.format("单号：{0}在{1}入库成功！", p.getSn(), p.getWarehouseName())
            );
        } else {
            p.setNextStatus(p.getStatus());
            p.setStatus(PackageStatusEnum.FREEZE);
            packageRepository.save(p);

            messageService.send(
                    p.getUserId(),
                    "/user/statements/index",
                    MessageFormat.format("单号：{0}，扣费失败，请充值账号并重新缴费。", p.getSn(), p.getWarehouseName())
            );
        }
        redirectAttributes.addFlashAttribute("success", "操作成功！");
        return new RestResponse<>();
    }
}

package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.common.app.utils.TextUtils;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
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


    @RequestMapping("/package/index")
    public String adminPackageIndex(
            Model model,
            @PageableDefault(size = 25, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status
    ) {
        Page<Package> packages = packageService.searchPackage(keyword, null, status, pageable);
        model.addAttribute("elements", packages);
        model.addAttribute("status", status);
        return "/admin/package/index";
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
        List<User> users = userService.findAll("ROLE_HWC");

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
     *
     * @param model
     * @param id
     * @return
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
            @RequestParam("qty[]") List<Integer> qty,
            @RequestParam("p[]") List<Long> pIds,
            @RequestParam(required = false, name = "total_fee") BigDecimal total,
            RedirectAttributes redirectAttributes
    ) {
        List<Product> products = productService.findProducts(pIds);

        int count = products.size();
        if (count != 0 && count == pIds.size() && qty.size() == count) {
            Package p = packageService.inbound(id, products, qty);

            redirectAttributes.addFlashAttribute("success", "操作成功！");


            //////////////////////  增加库存  /////////////////////////////////
//            stockService.addStock(p);

            //////////////////////  收入库费  /////////////////////////////////
            Statements statements = payService.saveStatements(payService.calcCK(p), total);
            boolean flag = payService.payStatements(statements.getId());

            if (flag) {
                messageService.send(
                        p.getUserId(),
                        "/package/" + p.getId() + "/show",
                        MessageFormat.format("单号：{0}在{1}入库成功！", p.getSn(), p.getWarehouseName())
                );
            } else {
                p.setNextStatus(p.getStatus());
                p.setStatus(PackageStatus.FREEZE);
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
        model.addAttribute("total", payService.calcSJ(p).getTotal());
        return "/admin/package/publish";
    }

    @RequestMapping(value = "/package/{id}/publish", method = RequestMethod.POST)
    public String postPackagePublish(
            Model model,
            @PathVariable("id") long id,
            @RequestParam(required = false) BigDecimal total,
            RedirectAttributes redirectAttributes

    ) {
        Package p = packageRepository.findOne(id);


        Statements statements = payService.saveStatements(payService.calcSJ(p), total);
        boolean flag = payService.payStatements(statements.getId());

        if (flag) {
            //支付成功
            messageService.send(
                    p.getUserId(),
                    "/package/"+p.getId()+"/show",
                    MessageFormat.format("入库单上架成功，单号：{0}，上架成功！费用已从您的账户里扣除。", p.getSn(), p.getWarehouseName())
            );
            stockService.addStock(p);
        } else {
            p.setNextStatus(PackageStatus.FINISH);
            p.setStatus(PackageStatus.FREEZE);
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
}

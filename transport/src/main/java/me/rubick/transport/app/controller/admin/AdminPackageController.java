package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.common.app.utils.TextUtils;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.model.Product;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.service.PackageService;
import me.rubick.transport.app.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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


    @RequestMapping("/package/index")
    public String adminPackageIndex(
            Model model,
            @PageableDefault(size = 25, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status
    ) {
        Page<Package> packages = packageService.searchPackage(keyword, null, status, pageable);
        model.addAttribute("elements", packages);
        return "/admin/package/index";
    }

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
            @RequestParam("weight[]") List<BigDecimal> weight,
            @RequestParam("qty[]") List<Integer> qty,
            @RequestParam("p[]") List<Long> pIds,
            RedirectAttributes redirectAttributes
    ) {
        List<Product> products = productService.findProducts(pIds);

        int count = products.size();
        if (count != 0 && count == pIds.size() && qty.size() == count && weight.size() == count) {
            log.info("{}", JSONMapper.toJSON(weight));
            Package p = packageService.inbound(id, pIds, weight, qty);

            redirectAttributes.addFlashAttribute("SUCCESS", "操作成功！");
            messageService.send(
                    p.getUserId(),
                    "/package/index?status=1",
                    MessageFormat.format("入库单：{0}在{1}入库成功！{2}", p.getReferenceNumber(), p.getWarehouseName())
            );
        }


        return "redirect:/admin/package/index";
    }
}

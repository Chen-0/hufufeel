package me.rubick.transport.app.controller;

import me.rubick.common.app.utils.HashUtils;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.service.PackageService;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/package")
public class PackageController {

    @Resource
    private PackageRepository packageRepository;

    @Resource
    private PackageService packageService;

    @Resource
    private UserService userService;

    @Resource
    private ProductService productService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String packageIndex(
            @PageableDefault(size = 10) Pageable pageable,
            Model model,
            @RequestParam String keyword,
            @RequestParam int status
    ) {
        User user = userService.getByLogin();
        Page<Package> packages = packageService.searchPackage(keyword, user, status, pageable);
        model.addAttribute("elements", packages);
        return "/package/index";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public String createPackage(
            @RequestParam("w") long wId,
            @RequestParam("dc") long dcId,
            @RequestParam("weight[]") List<BigDecimal> weights,
            @RequestParam("qty[]") List<Integer> qtys,
            @RequestParam("p[]") List<Long> pids,
            RedirectAttributes redirectAttributes
    ) {
        Package p = new Package();
        p.setUserId(userService.getByLogin().getId());
        p.setWarehouseId(wId);
        p.setDistributionChannelId(dcId);
        p.setStatus(PackageStatus.READY);
        p.setReferenceNumber(HashUtils.generateString());

        // TODO validate
        List<Product> products = productService.findProducts(pids);

        if (products.size() != weights.size() || products.size() != qtys.size()) {
            //error
        }

        List<PackageProduct> packageProducts = new ArrayList<>(weights.size());

        for (int i = 0; i < pids.size(); i++) {
            PackageProduct packageProduct = new PackageProduct();
            packageProduct.setProductId(pids.get(i));
            packageProduct.setQty(qtys.get(i));
            packageProduct.setWeight(weights.get(i));
            packageProduct.setRealQty(0);
            packageProduct.setRealWeight(new BigDecimal(0));

            packageProducts.add(packageProduct);
        }

        packageService.store(p, packageProducts);

        return "SUCCESS";
    }

//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    public String postCreatePackage() {
//        return "redirect:/package/index";
//    }
}

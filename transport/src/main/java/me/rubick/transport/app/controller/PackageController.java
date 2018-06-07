package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.response.RestResponse;
import me.rubick.common.app.utils.HashUtils;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.DistributionChannelRepository;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.PackageService;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.service.StockService;
import me.rubick.transport.app.service.UserService;
import me.rubick.transport.app.vo.ProductContainer;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@SessionAttributes("stockContainer")
public class PackageController {

    @Resource
    private PackageRepository packageRepository;

    @Resource
    private PackageService packageService;

    @Resource
    private UserService userService;

    @Resource
    private ProductService productService;

    @Resource
    private DistributionChannelRepository distributionChannelRepository;

    @ModelAttribute("stockContainer")
    public ProductContainer productController() {
        return new ProductContainer();
    }

    @RequestMapping(value = "/package/index", method = RequestMethod.GET)
    public String packageIndex(
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status
    ) {
        User user = userService.getByLogin();
        Page<Package> packages = packageService.searchPackage(keyword, user, status, pageable);
        model.addAttribute("elements", packages);
        model.addAttribute("_STATUS", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("MENU", "RUKUGUANLI");
        return "/package/index";
    }

    /**
     * 提交发货清单
     * @param wId
     * @param dcId
     * @param weights
     * @param qtys
     * @param pids
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/package/create", method = RequestMethod.POST)
    public String createPackage(
            @RequestParam("w") long wId,
            @RequestParam("dc") long dcId,
            @RequestParam("weight[]") List<BigDecimal> weights,
            @RequestParam("qty[]") List<Integer> qtys,
            @RequestParam("p[]") List<Long> pids,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {
        //check out
        DistributionChannel distributionChannel = distributionChannelRepository.findOne(dcId);

        if (ObjectUtils.isEmpty(distributionChannel)) {
            throw  new BusinessException("[A001] 禁止访问");
        }

        Warehouse warehouse = warehouseRepository.findOne(wId);

        if (ObjectUtils.isEmpty(warehouse)) {
            throw  new BusinessException("[A001] 禁止访问");
        }

        User user = userService.getByLogin();

        Package p = new Package();
        p.setUserId(user.getId());
        p.setWarehouseId(wId);
        p.setDistributionChannelId(dcId);
        p.setStatus(PackageStatus.READY);
        p.setReferenceNumber(HashUtils.generateString());
        p.setWarehouseName(warehouse.getName());
        p.setDistributionChannelName(distributionChannel.getName());
        p.setNickname(user.getName());

        // TODO validate
        List<Product> products = productService.findProducts(pids);

        if (products.size() != weights.size() || products.size() != qtys.size()) {
            //error
        }

        List<PackageProduct> packageProducts = new ArrayList<>(weights.size());

        int _qty = 0;
        BigDecimal _weight = new BigDecimal(0);

        for (int i = 0; i < pids.size(); i++) {
            PackageProduct packageProduct = new PackageProduct();
            packageProduct.setProductId(pids.get(i));
            packageProduct.setQty(qtys.get(i));
            packageProduct.setWeight(weights.get(i));
            packageProduct.setRealQty(0);
            packageProduct.setRealWeight(new BigDecimal(0));

            packageProducts.add(packageProduct);

            _qty += packageProduct.getQty();
            _weight = _weight.add(packageProduct.getWeight());
        }

        p.setQty(_qty);
        p.setWeight(_weight);
        packageService.store(p, packageProducts);

        redirectAttributes.addFlashAttribute("SUCCESS", "入库单创建成功！");
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

        if (! p.getStatus().equals(PackageStatus.READY)) {
            throw new BusinessException("");
        }

        p.setStatus(PackageStatus.CANCEL);
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

    /**
     * 添加到发货清单
     * @param productContainer
     * @param trackingNumbers
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/stock/select")
    public String selectStock(
            @ModelAttribute("stockContainer") ProductContainer productContainer,
            @RequestParam("trackingNumber[]") List<Long> trackingNumbers,
            RedirectAttributes redirectAttributes
    ) {
        productContainer.getProducts().addAll(trackingNumbers);
        redirectAttributes.addFlashAttribute("SUCCESS", "添加入库单成功！");
        return "redirect:/stock/send";
    }

    /**
     * 从发货清单中移除
     * @param id
     * @param productContainer
     * @return
     */
    @RequestMapping("/stock/send/{id}/remove")
    public RestResponse<String> removeStock(
            @PathVariable("id") long id,
            @ModelAttribute("stockContainer") ProductContainer productContainer
    ) {
        if (productContainer.getProducts().contains(id)) {
            productContainer.getProducts().remove(id);
        }

        return new RestResponse<>();
    }

    @RequestMapping("/stock/send/remove/all")
    public String removeAllStock(
            @PathVariable("id") long id,
            @ModelAttribute("stockContainer") ProductContainer productContainer
    ) {
        productContainer.getProducts().clear();
        return "redirect:/stock/send";
    }

    /**
     * 发货清单
     * @param productContainer
     * @param model
     * @return
     */
    @RequestMapping(value = "/stock/send", method = RequestMethod.GET)
    public String sendStock(
            @ModelAttribute("stockContainer") ProductContainer productContainer,
            Model model
    ) {
        User user = userService.getByLogin();
        List<ProductWarehouse> productWarehouses = stockService.findAll(user, productContainer.getProducts());
        model.addAttribute("elements", productWarehouses);
        return "/package/send";
    }
}


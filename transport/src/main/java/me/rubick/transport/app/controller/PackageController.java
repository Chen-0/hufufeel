package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.response.RestResponse;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.HashUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.StatementTypeEnum;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.DistributionChannelRepository;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.*;
import me.rubick.transport.app.vo.ProductContainer;
import me.rubick.transport.app.vo.ProductWarehouseVo;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
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

    @Resource
    private PayService payService;

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


        if (status!= null && status == 5) {
            Map<String, Statements> map= payService.findUnpayStatementsByUserIdAndType(user.getId(), Arrays.asList(StatementTypeEnum.SJ, StatementTypeEnum.RK));
            log.info("{}", JSONMapper.toJSON(map));
            model.addAttribute("smap", map);
        }
        return "/package/index";
    }

    @RequestMapping("/package/{id}/show")
    public String showPackage(
            @PathVariable long id,
            Model model
    ) {
        Package p = packageRepository.findOne(id);
        User user = userService.getByLogin();

        List<Statements> statements = payService.findByUserIdAndTypeIn(
                p.getId(), Arrays.asList(StatementTypeEnum.SJ, StatementTypeEnum.RK)
        );
        model.addAttribute("statements", statements);
        model.addAttribute("ele", p);
        return "/package/show";
    }

    /**
     * 提交发货清单
     * @param wId
     * @param qtys
     * @param pids
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/package/create", method = RequestMethod.POST)
    public String createPackage(
            @RequestParam("w") long wId,
            @RequestParam String referenceNumber,
            @RequestParam(required = false, defaultValue = "") String comment,
            @RequestParam("qty[]") List<Integer> qtys,
            @RequestParam("p[]") List<Long> pids,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {
        Warehouse warehouse = warehouseRepository.findOne(wId);

        if (ObjectUtils.isEmpty(warehouse)) {
            throw  new BusinessException("[A001] 禁止访问");
        }

        User user = userService.getByLogin();

        Package p = new Package();
        p.setUserId(user.getId());
        p.setWarehouseId(wId);
        p.setStatus(PackageStatus.READY);
        p.setReferenceNumber(referenceNumber);
        p.setWarehouseName(warehouse.getName());
        p.setNickname(user.getName());
        p.setComment(comment);

        // TODO validate
        List<Product> products = productService.findProducts(pids);

        if (products.size() != qtys.size()) {
            throw  new BusinessException("[A001] 禁止访问");
        }

        List<PackageProduct> packageProducts = new ArrayList<>(qtys.size());

        int _qty = 0;

        for (int i = 0; i < pids.size(); i++) {
            PackageProduct packageProduct = new PackageProduct();
            packageProduct.setProductId(pids.get(i));
            packageProduct.setExpectQuantity(qtys.get(i));
            packageProduct.setQuantity(0);
            packageProducts.add(packageProduct);
            _qty += packageProduct.getExpectQuantity();
        }

        p.setExpectQuantity(_qty);
        p.setSn(packageService.generateBatch());
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


    @RequestMapping(value = "/stock/send", method = RequestMethod.GET)
    public String sendStock(
            Model model
    ) {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        model.addAttribute("warehouses", warehouses);
        return "/package/send";
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
        for (int i = 0; i < max; i ++) {
            productWarehouseVos.get(i).setProductName(warehouses.get(i).getProduct().getProductName());
            productWarehouseVos.get(i).setProductSku(warehouses.get(i).getProduct().getProductSku());
        }

        return new RestResponse<>(productWarehouseVos);
    }

    @RequestMapping("/ajax/stock/get_stock")
    @ResponseBody
    public RestResponse<ProductWarehouseVo> ajaxGetStockByProductSku(
            @RequestParam String productSku,
            @RequestParam int qty
    ) {
        productSku = productSku.trim();
        ProductWarehouse productWarehouse = stockService.findAvailableStockByProductSku(productSku);
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
}


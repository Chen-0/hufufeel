package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelConverter;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.ExcelHepler;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.DocumentRepository;
import me.rubick.transport.app.repository.OrderRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.DocumentService;
import me.rubick.transport.app.service.OrderService;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.vo.OrderExcelVo;
import me.rubick.transport.app.vo.OrderSnapshotVo;
import me.rubick.transport.app.vo.PackageExcelVo;
import org.apache.poi.ss.usermodel.Row;
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
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class OrderController extends AbstractController {

    @Resource
    private ProductService productService;

    @Resource
    private OrderService orderService;

    @Resource
    private WarehouseRepository warehouseRepository;

    @Resource
    private DocumentService documentService;

    @RequestMapping("/order/index")
    public String index(
            Model model,
            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status
    ) {
        User user = userService.getByLogin();
        model.addAttribute("elements", orderService.findAll(user, keyword, status, pageable));
        model.addAttribute("_STATUS", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("MENU", "DINGDANGUANLI");
        return "/order/index";
    }

    @RequestMapping(value = "/order/{id}/show", method = RequestMethod.GET)
    public String showOrder(
            @PathVariable long id,
            Model model
    ) {
        Order order = orderService.findOne(id);
        model.addAttribute("ele", order);
        return "/order/show";
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public String postCreateOrder(
            @RequestParam("pids[]") List<Long> productIds,
            @RequestParam("qty[]") List<Integer> quantities,
            @RequestParam("wid") long wid,
            @RequestParam Map<String, String> params,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {

        Warehouse warehouse = warehouseRepository.findOne(wid);

        if (ObjectUtils.isEmpty(warehouse)) {
            log.error("仓库不存在 wid = {}", wid);
            throw new BusinessException("提交失败！含有一个或多个错误");
        }

        List<Product> products = productService.findProducts(productIds);

        if (products.size() == productIds.size() && productIds.size() == quantities.size()) {
            User user = userService.getByLogin();

            orderService.createOrder(user, products, quantities, warehouse, params);
            redirectAttributes.addFlashAttribute("SUCCESS", "新建发货单成功！");
            return "redirect:/order/index";
        } else {
            log.info("{}", products.size());
            log.info("{}", JSONMapper.toJSON(productIds));
            log.info("{}", JSONMapper.toJSON(quantities));
            log.info("{}", JSONMapper.toJSON(params));
            throw new BusinessException("提交失败！含有一个或多个错误");
        }
    }

    @RequestMapping("/order/{id}/cancel")
    public String cancelOrder(
            @PathVariable("id") long id,
            RedirectAttributes redirectAttributes) throws BusinessException {
        Order order = orderService.findOne(id);

        if (ObjectUtils.isEmpty(order)) {
            throw new BusinessException("[A005] 数据异常");
        }

        User user = userService.getByLogin();

        if (order.getUserId() != user.getId()) {
            throw new BusinessException("[A006] 禁止访问");
        }

        if (order.getStatus().equals(OrderStatusEnum.CHECK) || order.getStatus().equals(OrderStatusEnum.FAIL)) {
            orderService.cancelOrder(order, user.getId());
            redirectAttributes.addFlashAttribute("SUCCESS", "取消订单成功！");
        } else {
            throw new BusinessException("[A007] 状态异常");
        }

        return "redirect:/order/index";
    }

    @RequestMapping(value = "/order/import", method = RequestMethod.GET)
    public String getImportOrder() {
        return "order/import";
    }

    @RequestMapping(value = "/order/import", method = RequestMethod.POST)
    public String postImportOrder(
            @RequestParam("file") MultipartFile multipartFile,
            RedirectAttributes redirectAttributes
    ) {

        try {
            User user = userService.getByLogin();
            File file = documentService.multipartFile2File(multipartFile);
            ExcelHepler<OrderExcelVo> excelHepler = new ExcelHepler<>();
            List<OrderExcelVo> orderExcelVos = excelHepler.readToObject(file, new ExcelConverter<OrderExcelVo>() {
                @Override
                public OrderExcelVo read(Row row) throws BusinessException {
                    OrderExcelVo orderExcelVo = new OrderExcelVo();

                    orderExcelVo.setA(ExcelHepler.getValue(row, 0, false));
                    orderExcelVo.setB(ExcelHepler.getValue(row, 1, false));
                    orderExcelVo.setC(ExcelHepler.getValue(row, 2, false));
                    orderExcelVo.setD(ExcelHepler.getValue(row, 3, false));
                    orderExcelVo.setE(ExcelHepler.getValue(row, 4, false));
                    orderExcelVo.setF(ExcelHepler.getValue(row, 5, false));
                    orderExcelVo.setG(ExcelHepler.getValue(row, 6, false));
                    orderExcelVo.setH(ExcelHepler.getValue(row, 7, false));
                    orderExcelVo.setI(ExcelHepler.getValue(row, 8, false));
                    orderExcelVo.setJ(ExcelHepler.getValue(row, 9, false));
                    orderExcelVo.setK(ExcelHepler.getValue(row, 10, false));
                    orderExcelVo.setL(ExcelHepler.getValue(row, 11, false));
                    orderExcelVo.setM(ExcelHepler.getValue(row, 12, false));
                    orderExcelVo.setN(ExcelHepler.getValue(row, 13, false));
                    orderExcelVo.setO(ExcelHepler.getValue(row, 14, false));
                    orderExcelVo.setP(ExcelHepler.getValue(row, 15, true));
                    orderExcelVo.setQ(ExcelHepler.getValue(row, 16, true));
                    orderExcelVo.setR(ExcelHepler.getValue(row, 17, true));
                    orderExcelVo.setS(ExcelHepler.getValue(row, 18, true));
                    orderExcelVo.setT(ExcelHepler.getValue(row, 19, true));
                    orderExcelVo.setU(ExcelHepler.getValue(row, 20, true));
                    orderExcelVo.setV(ExcelHepler.getValue(row, 21, true));
                    orderExcelVo.setW(ExcelHepler.getValue(row, 22, true));
                    orderExcelVo.setX(ExcelHepler.getValue(row, 23, true));
                    orderExcelVo.setY(ExcelHepler.getValue(row, 24, true));

                    return orderExcelVo;
                }
            });

            log.info("{}", JSONMapper.toJSON(orderExcelVos));

            orderService.createOrder(orderExcelVos, user);
        } catch (BusinessException e) {
            log.warn("", e);
            redirectAttributes.addFlashAttribute("warn", e.getMessage());
            return "redirect:/order/import";
        } catch (CommonException e) {
            log.warn("", e);
            redirectAttributes.addFlashAttribute("warn", e.getMessage());
            return "redirect:/order/import";
        }

        redirectAttributes.addFlashAttribute("SUCCESS", "导入发货单成功！");
        return "redirect:/order/import";
    }
}

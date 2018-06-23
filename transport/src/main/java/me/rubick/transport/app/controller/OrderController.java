package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelConverter;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.response.RestResponse;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.ExcelHepler;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.StatementTypeEnum;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.DocumentRepository;
import me.rubick.transport.app.repository.OrderRepository;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.DocumentService;
import me.rubick.transport.app.service.OrderService;
import me.rubick.transport.app.service.PayService;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.vo.DocumentVo;
import me.rubick.transport.app.vo.OrderExcelVo;
import me.rubick.transport.app.vo.OrderSnapshotVo;
import me.rubick.transport.app.vo.PackageExcelVo;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.*;

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

    @Resource
    private PayService payService;

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

        if (status != null && status == OrderStatusEnum.FREEZE.ordinal()) {
            Map<String, Statements> map = payService.findUnpayStatementsByUserIdAndType(user.getId(), Arrays.asList(StatementTypeEnum.ORDER));
            log.info("{}", JSONMapper.toJSON(map));
            model.addAttribute("smap", map);
        }

        return "/order/index";
    }

    @RequestMapping(value = "/order/{id}/show", method = RequestMethod.GET)
    public String showOrder(
            @PathVariable long id,
            Model model
    ) {
        User user = userService.getByLogin();
        Order order = orderService.findOne(id);


        if (ObjectUtils.isEmpty(order)) {
            return "redirect:/order/index";
        }

        if (user.getId() == order.getId()) {
            return "redirect:/order/index";
        }
        OrderLogistics orderLogistics = orderService.findOrNewOrderLogistics(id);
        model.addAttribute("lg", orderLogistics);
        model.addAttribute("ele", order);
        return "/order/show";
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.GET)
    public String sendStock(
            Model model
    ) {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("CKT_1", configService.findByKey("CKT_1"));
        model.addAttribute("CKT_2", configService.findByKey("CKT_2"));
        model.addAttribute("CKT_3", configService.findByKey("CKT_3"));
        model.addAttribute("CKF_1", configService.findByKey("CKF_1"));

        if (ObjectUtils.isEmpty(model.asMap().get("sp"))) {
            model.addAttribute("sp", new OrderSnapshotVo());
        }

        List<Long> pids = (List<Long>) model.asMap().get("pids");
        List<Long> qty = (List<Long>) model.asMap().get("qty");
        log.info("{}", JSONMapper.toJSON(pids));
        log.info("{}", JSONMapper.toJSON(qty));

        if (!ObjectUtils.isEmpty(pids)) {
            model.addAttribute("products", productService.findProducts(pids));
            model.addAttribute("qty", qty);

        }
        return "/package/send";
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public String postCreateOrder(
            @RequestParam("pids[]") List<Long> productIds,
            @RequestParam("qty[]") List<Integer> quantities,
            @RequestParam("wid") long wid,
            @RequestParam Map<String, String> params,
            @RequestParam(name = "c_type") String cType,
            @RequestParam(name = "did", required = false) Long documentId,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {

        Warehouse warehouse = warehouseRepository.findOne(wid);
        Map<String, String> map = new HashMap<>();
        boolean hasError = false;
        if (ObjectUtils.isEmpty(warehouse)) {
            log.error("仓库不存在 wid = {}", wid);
            throw new BusinessException("提交失败！含有一个或多个错误");
        }

        List<Product> products = productService.findProducts(productIds);

        if (ObjectUtils.isEmpty(productIds)) {
            map.put("product", "请添加货品");
            hasError = true;
        }

        if (cType.equals("w")) {

            if (ObjectUtils.isEmpty(params.get("ckf2"))) {
                map.put("ckf2", "姓名不能为空");
                hasError = true;
            }

            if (ObjectUtils.isEmpty(params.get("ckf3"))) {
                map.put("ckf3", "省份不能为空");
                hasError = true;
            }

            if (ObjectUtils.isEmpty(params.get("ckf5"))) {
                map.put("ckf5", "城市不能为空");
                hasError = true;
            }

            if (ObjectUtils.isEmpty(params.get("ckf10"))) {
                map.put("ckf10", "街道不能为空");
                hasError = true;
            }

            if (ObjectUtils.isEmpty(params.get("ckf7"))) {
                map.put("ckf7", "邮编不能为空");
                hasError = true;
            }
        } else if (cType.equals("u")) {

            if (ObjectUtils.isEmpty(documentId) || documentId == -1) {
                map.put("did", "请上传PDF文件");
                hasError = true;
            }
        }

        if (hasError) {
            redirectAttributes.addFlashAttribute("cType", cType);
            redirectAttributes.addFlashAttribute("pids", productIds);
            redirectAttributes.addFlashAttribute("wid", wid);
            redirectAttributes.addFlashAttribute("qty", quantities);
            redirectAttributes.addFlashAttribute("error", map);
            redirectAttributes.addFlashAttribute("sp", BeanMapperUtils.map(params, OrderSnapshotVo.class));
            return "redirect:/order/create";
        }

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

    @RequestMapping(value = "/order/{id}/update", method = RequestMethod.GET)
    public String updateOrder(Model model, @PathVariable long id) {
        Order order = orderService.findOne(id);
        model.addAttribute("CKT_1", configService.findByKey("CKT_1"));
        model.addAttribute("CKT_2", configService.findByKey("CKT_2"));
        model.addAttribute("CKT_3", configService.findByKey("CKT_3"));
        model.addAttribute("CKF_1", configService.findByKey("CKF_1"));
        model.addAttribute("o", order);

        if (ObjectUtils.isEmpty(model.asMap().get("sp"))) {
            model.addAttribute("sp", order.getOrderSnapshotVo());
        }

        if (ObjectUtils.isEmpty(model.asMap().get("cType"))) {
            model.addAttribute("cType", order.getcType());
        }
        return "/package/update_send";
    }

    @RequestMapping(value = "/order/{id}/update", method = RequestMethod.POST)
    public String updateOrder(
            @PathVariable long id,
            @RequestParam Map<String, String> params,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "c_type") String cType,
            @RequestParam(name = "did", required = false) Long documentId
    ) {
        Map<String, String> map = new HashMap<>();
        boolean hasError = false;

        if (cType.equals("w")) {

            if (ObjectUtils.isEmpty(params.get("ckf2"))) {
                map.put("ckf2", "姓名不能为空");
                hasError = true;
            }

            if (ObjectUtils.isEmpty(params.get("ckf3"))) {
                map.put("ckf3", "省份不能为空");
                hasError = true;
            }

            if (ObjectUtils.isEmpty(params.get("ckf5"))) {
                map.put("ckf5", "城市不能为空");
                hasError = true;
            }

            if (ObjectUtils.isEmpty(params.get("ckf10"))) {
                map.put("ckf10", "街道不能为空");
                hasError = true;
            }

            if (ObjectUtils.isEmpty(params.get("ckf7"))) {
                map.put("ckf7", "邮编不能为空");
                hasError = true;
            }
        } else if (cType.equals("u")) {

            if (ObjectUtils.isEmpty(documentId) || documentId == -1) {
                map.put("did", "请上传PDF文件");
                hasError = true;
            }
        }

        if (hasError) {
            redirectAttributes.addFlashAttribute("cType", cType);
            redirectAttributes.addFlashAttribute("error", map);
            redirectAttributes.addFlashAttribute("sp", BeanMapperUtils.map(params, OrderSnapshotVo.class));
            return "redirect:/order/" + id + "/update";
        }

        Order order = orderService.findOne(id);
        OrderSnapshotVo orderSnapshotVo = BeanMapperUtils.map(params, OrderSnapshotVo.class);
        order.setReferenceNumber(orderSnapshotVo.getCkt4());
        order.setTn(orderSnapshotVo.getCkt5());
        order.setComment(orderSnapshotVo.getCkt6());
        order.setOrderSnapshot(JSONMapper.toJSON(orderSnapshotVo));
        order.setcType(cType);

        if (cType.equals("u")) {
            order.setDocumentId(Long.valueOf(params.get("did")));
            order.setPhone("");
            order.setContact("");
        } else {
            order.setDocumentId(null);
            order.setPhone(orderSnapshotVo.getCkf4());
            order.setContact(orderSnapshotVo.getCkf2());
        }

        orderService.updateOrder(order);

        redirectAttributes.addFlashAttribute("SUCCESS", "修改发货单成功！");
        return "redirect:/order/index";
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
                    orderExcelVo.setD(ExcelHepler.getValue(row, 3, true));
                    orderExcelVo.setE(ExcelHepler.getValue(row, 4, true));
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

    @RequestMapping(value = "/order/pdf/upload", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<DocumentVo> uploadFile(
            @RequestParam("uploadfile") MultipartFile uploadfile) {
        Document document = documentService.storeDocument(uploadfile);

        return new RestResponse<>(BeanMapperUtils.map(document, DocumentVo.class));

    }
}

package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelConverter;
import me.rubick.common.app.excel.ExcelWriter;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.response.RestResponse;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.DateUtils;
import me.rubick.common.app.utils.ExcelHelper;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.OrderStatusEnum;
import me.rubick.transport.app.constants.StatementTypeEnum;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.WarehouseRepository;
import me.rubick.transport.app.service.DocumentService;
import me.rubick.transport.app.service.OrderService;
import me.rubick.transport.app.service.PayService;
import me.rubick.transport.app.service.ProductService;
import me.rubick.transport.app.vo.DocumentVo;
import me.rubick.transport.app.vo.OrderExcelVo;
import me.rubick.transport.app.vo.OrderSnapshotVo;
import me.rubick.transport.app.vo.ProductSnapshotVo;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
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

        if (user.getId() != order.getUserId()) {
            return "redirect:/order/index";
        }

        OrderLogistics orderLogistics = orderService.findOrNewOrderLogistics(id);
        model.addAttribute("lg", orderLogistics);
        model.addAttribute("ele", order);

        List<Statements> statements = payService.findByUserIdAndTypeIn(
                order.getId(), Arrays.asList(StatementTypeEnum.ORDER)
        );
        model.addAttribute("statements", statements);
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

        if (ObjectUtils.isEmpty(model.asMap().get("cType"))) {
            model.addAttribute("cType", "w");
        }

        List<Long> pids = (List<Long>) model.asMap().get("pids");
        List<Long> qty = (List<Long>) model.asMap().get("qty");
        log.info("{}", JSONMapper.toJSON(pids));
        log.info("{}", JSONMapper.toJSON(qty));

        if (!ObjectUtils.isEmpty(pids)) {
            model.addAttribute("products", productService.findProducts(pids));
            model.addAttribute("qty", qty);

        }
        return "/order/create";
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
                map.put("did", "请上传PDF格式的文件！");
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
        return "/order/update";
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
            ExcelHelper<OrderExcelVo> excelHepler = new ExcelHelper<>();
            List<OrderExcelVo> orderExcelVos = excelHepler.readToObject(file, new ExcelConverter<OrderExcelVo>() {
                @Override
                public OrderExcelVo read(Row row) throws BusinessException {
                    OrderExcelVo orderExcelVo = new OrderExcelVo();

                    orderExcelVo.setA(ExcelHelper.getValue(row, 0, false));
                    orderExcelVo.setB(ExcelHelper.getValue(row, 1, false));
                    orderExcelVo.setC(ExcelHelper.getValue(row, 2, false));
                    orderExcelVo.setD(ExcelHelper.getValue(row, 3, true));
                    orderExcelVo.setE(ExcelHelper.getValue(row, 4, true));
                    orderExcelVo.setF(ExcelHelper.getValue(row, 5, false));
                    orderExcelVo.setG(ExcelHelper.getValue(row, 6, false));
                    orderExcelVo.setH(ExcelHelper.getValue(row, 7, false));
                    orderExcelVo.setI(ExcelHelper.getValue(row, 8, false));
                    orderExcelVo.setJ(ExcelHelper.getValue(row, 9, false));
                    orderExcelVo.setK(ExcelHelper.getValue(row, 10, false));
                    orderExcelVo.setL(ExcelHelper.getValue(row, 11, false));
                    orderExcelVo.setM(ExcelHelper.getValue(row, 12, false));
                    orderExcelVo.setN(ExcelHelper.getValue(row, 13, false));
                    orderExcelVo.setO(ExcelHelper.getValue(row, 14, false));
                    orderExcelVo.setP(ExcelHelper.getValue(row, 15, true));
                    orderExcelVo.setQ(ExcelHelper.getValue(row, 16, true));
                    orderExcelVo.setR(ExcelHelper.getValue(row, 17, true));
                    orderExcelVo.setS(ExcelHelper.getValue(row, 18, true));
                    orderExcelVo.setT(ExcelHelper.getValue(row, 19, true));
                    orderExcelVo.setU(ExcelHelper.getValue(row, 20, true));
                    orderExcelVo.setV(ExcelHelper.getValue(row, 21, true));
                    orderExcelVo.setW(ExcelHelper.getValue(row, 22, true));
                    orderExcelVo.setX(ExcelHelper.getValue(row, 23, true));
                    orderExcelVo.setY(ExcelHelper.getValue(row, 24, true));

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

    @RequestMapping(value = "/order/export", method = RequestMethod.GET)
    public void adminOrderExport(
            HttpServletResponse response,
            @PageableDefault(size = Integer.MAX_VALUE, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "-1") Integer status
    ) throws IOException {
        User user = userService.getByLogin();
        Page<Order> orders = orderService.findAll(user, keyword, status, pageable);
        List<Order> elements = orders.getContent();
        int row = elements.size();
        log.info("{}", row);
        Object[][] context = new Object[row + 1][21];

        context[0][0] = "发货单号";
        context[0][1] = "交易号";
        context[0][2] = "参考号";
        context[0][3] = "派送方式";
        context[0][4] = "快递单号";
        context[0][5] = "销售平台";
        context[0][6] = "仓库";
        context[0][7] = "总重量";
        context[0][8] = "创建时间";
        context[0][9] = "姓名";
        context[0][10] = "电话";
        context[0][11] = "国家";
        context[0][12] = "州/省";
        context[0][13] = "城市";
        context[0][14] = "街道";
        context[0][15] = "门牌号";
        context[0][16] = "邮编";
        context[0][17] = "sku";
        context[0][18] = "商品名称";
        context[0][19] = "发货数量";
        context[0][20] = "状态";

        int j = 1;
        for (int i = 0; i < row; i++) {
            Order o = elements.get(i);
            o.setOrderSnapshotVo(JSONMapper.fromJson(o.getOrderSnapshot(), OrderSnapshotVo.class));
            context[j][0] = o.getSn();
            log.info("{}", o.getSn());
            context[j][1] = o.getTn();
            context[j][2] = o.getReferenceNumber();
            context[j][3] = o.getOrderSnapshotVo().getCkt1();
            context[j][4] = o.getExpressNo();
            context[j][5] = o.getOrderSnapshotVo().getCkt3();
            context[j][6] = o.getWarehouseName();
            context[j][7] = o.getWeight().toString() + "KG";
            context[j][8] = DateUtils.date2StringYMDHMS(o.getCreatedAt());
            context[j][9] = o.getContact();
            context[j][10] = o.getPhone();
            context[j][11] = o.getOrderSnapshotVo().getCkf1();
            context[j][12] = o.getOrderSnapshotVo().getCkf3();
            context[j][13] = o.getOrderSnapshotVo().getCkf5();
            context[j][14] = o.getOrderSnapshotVo().getCkf10();
            context[j][15] = o.getOrderSnapshotVo().getCkf11();
            context[j][16] = o.getOrderSnapshotVo().getCkf7();

            StringBuilder skuBuilder = new StringBuilder();
            StringBuilder nameBuilder = new StringBuilder();
            StringBuilder qtyBuilder = new StringBuilder();

            for (OrderItem item: o.getOrderItems()) {
                ProductSnapshotVo productSnapshotVo = JSONMapper.fromJson(item.getProductSnapshot(), ProductSnapshotVo.class);
                skuBuilder.append(productSnapshotVo.getProductSku());
                skuBuilder.append(";");

                nameBuilder.append(productSnapshotVo.getProductName());
                nameBuilder.append(";");

                qtyBuilder.append(item.getQuantity());
                qtyBuilder.append(";");
            }

            context[j][17] = skuBuilder.toString();
            context[j][18] = nameBuilder.toString();
            context[j][19] = qtyBuilder.toString();
            context[j][20] = o.getStatus().getValue();


            j+=1;
        }

        log.info("{}", JSONMapper.toJSON(context));

        Date date = new Date();
        String s = DateUtils.date2String0(date);
        response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "HUFU_"+s+"_出库单.xlsx";
        fileName = URLEncoder.encode(fileName, "utf-8");
        response.setHeader("Content-Disposition", MessageFormat.format("attachment; filename*=\"{0}\"", fileName));
        ExcelWriter.getExcelInputSteam(context, response.getOutputStream());
    }
}

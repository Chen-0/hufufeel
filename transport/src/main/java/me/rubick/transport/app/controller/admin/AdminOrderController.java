package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelRow;
import me.rubick.common.app.excel.ExcelWriter;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.exception.FormException;
import me.rubick.common.app.exception.HttpNoFoundException;
import me.rubick.common.app.helper.FormHelper;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.DateUtils;
import me.rubick.common.app.utils.ExcelHelper;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.OrderStatusEnum;
import me.rubick.transport.app.constants.StatementTypeEnum;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.OrderRepository;
import me.rubick.transport.app.service.OrderService;
import me.rubick.transport.app.service.PayService;
import me.rubick.transport.app.vo.OrderSnapshotVo;
import me.rubick.transport.app.vo.ProductSnapshotVo;
import me.rubick.transport.app.vo.admin.OrderCheckOutVo;
import me.rubick.transport.app.vo.admin.OrderFormVo;
import org.aspectj.weaver.ast.Or;
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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
@SessionAttributes("orderStatus")
@Slf4j
public class AdminOrderController extends AbstractController {

    @Resource
    private OrderService orderService;

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private PayService payService;

    @ModelAttribute("orderStatus")
    public Integer orderStatus() {
        return -1;
    }

    @RequestMapping(value = "/order/index", method = RequestMethod.GET)
    public String adminOrderIndex(
            Model model,
            @PageableDefault(size = 25, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status
    ) {
        Page<Order> orders = orderService.findAll(null, keyword, status, null, null, pageable);
        model.addAttribute("elements", orders);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);

        if (!ObjectUtils.isEmpty(status)) {
            model.addAttribute("orderStatus", status);
        }

        return "/admin/order/index";
    }

    @RequestMapping(value = "/order/export", method = RequestMethod.GET)
    public void adminOrderExport(
            HttpServletResponse response,
            @PageableDefault(size = Integer.MAX_VALUE, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status
    ) throws IOException {
        Page<Order> orders = orderService.findAll(null, keyword, status, null, null, pageable);
        List<Order> elements = orders.getContent();
        int row = elements.size();

        Object[][] context = new Object[row + 1][21];

        context[0][0] = "发货单号";
        context[0][1] = "运费（USD）";
        context[0][2] = "快递公司（可空）";
        context[0][3] = "快递单号";
        context[0][4] = "交易号";
        context[0][5] = "参考号";
        context[0][6] = "派送方式";
        context[0][7] = "销售平台";
        context[0][8] = "仓库";
        context[0][9] = "总重量";
        context[0][10] = "姓名";
        context[0][11] = "电话";
        context[0][12] = "国家";
        context[0][13] = "州/省";
        context[0][14] = "城市";
        context[0][15] = "街道";
        context[0][16] = "门牌号";
        context[0][17] = "邮编";
        context[0][18] = "sku";
        context[0][19] = "商品名称";
        context[0][20] = "发货数量";

        int j = 1;
        for (int i = 0; i < row; i++) {
            Order o = elements.get(i);
            if (!o.getStatus().equals(OrderStatusEnum.CHECK)) {
                continue;
            }
            o.setOrderSnapshotVo(JSONMapper.fromJson(o.getOrderSnapshot(), OrderSnapshotVo.class));
            context[j][0] = o.getSn();
            context[j][1] = "";
            context[j][2] = "";
            context[j][3] = "";
            context[j][4] = o.getTn();
            context[j][5] = o.getReferenceNumber();
            context[j][6] = o.getOrderSnapshotVo().getCkt1();
            context[j][7] = o.getOrderSnapshotVo().getCkt3();
            context[j][8] = o.getWarehouseName();
            context[j][9] = o.getWeight().toString() + "KG";
            context[j][10] = o.getContact();
            context[j][11] = o.getPhone();
            context[j][12] = o.getOrderSnapshotVo().getCkf1();
            context[j][13] = o.getOrderSnapshotVo().getCkf3();
            context[j][14] = o.getOrderSnapshotVo().getCkf5();
            context[j][15] = o.getOrderSnapshotVo().getCkf10();
            context[j][16] = o.getOrderSnapshotVo().getCkf11();
            context[j][17] = o.getOrderSnapshotVo().getCkf7();

            StringBuilder skuBuilder = new StringBuilder();
            StringBuilder nameBuilder = new StringBuilder();
            StringBuilder qtyBuilder = new StringBuilder();

            for (OrderItem item : o.getOrderItems()) {
                ProductSnapshotVo productSnapshotVo = JSONMapper.fromJson(item.getProductSnapshot(), ProductSnapshotVo.class);
                skuBuilder.append(productSnapshotVo.getProductSku());
                skuBuilder.append(";");

                nameBuilder.append(productSnapshotVo.getProductName());
                nameBuilder.append(";");

                qtyBuilder.append(item.getQuantity());
                qtyBuilder.append(";");
            }

            context[j][18] = skuBuilder.toString();
            context[j][19] = nameBuilder.toString();
            context[j][20] = qtyBuilder.toString();


            j += 1;
        }

        Date date = new Date();
        String s = DateUtils.date2String0(date);
        response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "HUFU_" + s + "_出库单.xlsx";
        fileName = URLEncoder.encode(fileName, "utf-8");
        response.setHeader("Content-Disposition", MessageFormat.format("attachment; filename*=\"{0}\"", fileName));
        ExcelWriter.getExcelInputSteam(context, response.getOutputStream());
    }

    @RequestMapping(value = "/order/{id}/show", method = RequestMethod.GET)
    public String showOrder(
            @PathVariable long id,
            Model model
    ) {
        Order order = orderService.findOne(id);
        OrderLogistics orderLogistics = orderService.findOrNewOrderLogistics(id);
        model.addAttribute("lg", orderLogistics);
        model.addAttribute("ele", order);
        List<Statements> statements = payService.findByUserIdAndTypeIn(
                order.getId(), Arrays.asList(StatementTypeEnum.ORDER)
        );
        model.addAttribute("statements", statements);
        return "/admin/order/show";
    }

    @RequestMapping("/order/{id}/change_status")
    public String changeProductStatus(
            @PathVariable("id") long id,
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "") String comment,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("orderStatus") Integer status
    ) {
        Order order = orderRepository.findOne(id);

        if (ObjectUtils.isEmpty(order)) {
            return "redirect:/admin/product/index";
        }

        if (name.equals("success")) {
            order.setStatus(OrderStatusEnum.READY);

            messageService.send(
                    order.getUserId(),
                    "/order/" + order.getId() + "/show",
                    MessageFormat.format("出库单：{0} 审核成功！", order.getSn())
            );
        } else if (name.equals("fail")) {
            order.setStatus(OrderStatusEnum.FAIL);
            order.setReason(comment);

            messageService.send(
                    order.getUserId(),
                    "/order/" + order.getId() + "/show",
                    MessageFormat.format("出库单：{0} 审核失败！", order.getSn())
            );
        } else {
            return "redirect:/admin/product/index";
        }

        orderRepository.save(order);
        redirectAttributes.addFlashAttribute("success", "更新货品审核状态！");


        if (ObjectUtils.isEmpty(status) || status == -1) {
            return "redirect:/admin/order/index";
        } else {
            return "redirect:/admin/order/index?status=" + status;
        }
    }

    /**
     * 审核收费页面
     */
    @RequestMapping(value = "/order/{id}/check_out", method = RequestMethod.GET)
    public String getCheckOut(
            @PathVariable long id,
            Model model
    ) {
        Order order = orderService.findOne(id);

        if (ObjectUtils.isEmpty(order)) {
            return "redirect:/admin/order/index";
        }

        model.addAttribute("ele", order);
        if (ObjectUtils.isEmpty(model.asMap().get("fele"))) {
            model.addAttribute("fele", new OrderCheckOutVo());
        }

        return "/admin/order/check_out";
    }

    @RequestMapping(value = "/order/{id}/check_out", method = RequestMethod.POST)
    public String postCheckOut(
            @PathVariable long id,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("orderStatus") Integer status,
            @RequestParam String json
    ) throws HttpNoFoundException {
        Order order = orderService.findOne(id);
        OrderCheckOutVo orderCheckOutVo = JSONMapper.fromJson(json, OrderCheckOutVo.class);

        if (ObjectUtils.isEmpty(order)) {
            throw new HttpNoFoundException();
        }

        try {
            FormHelper formHelper = FormHelper.getInstance();
            formHelper.notNull(orderCheckOutVo.getTotal(), "total");
            formHelper.mustDecimal(orderCheckOutVo.getTotal(), "total");
            formHelper.hasError();
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), orderCheckOutVo);
            return "redirect:/admin/order/" + order.getId() + "/check_out";
        }

        orderService.checkOut(
                order,
                new BigDecimal(orderCheckOutVo.getTotal()),
                orderCheckOutVo.getExpress(),
                orderCheckOutVo.getExpressNo()
        );

        redirectAttributes.addFlashAttribute("success", "运单审核成功！");

        if (ObjectUtils.isEmpty(status) || status == -1) {
            return "redirect:/admin/order/index";
        } else {
            return "redirect:/admin/order/index?status=" + status;
        }
    }

    @RequestMapping(value = "/order/{id}/out_bound", method = RequestMethod.GET)
    public String outBound(
            @PathVariable long id,
            Model model
    ) {
        Order order = orderService.findOne(id);

        if (ObjectUtils.isEmpty(order)) {
            return "redirect:/admin/order/index";
        }

        Statements statements = payService.createORDER(order);
        model.addAttribute("ss", statements);
        model.addAttribute("ele", order);
        model.addAttribute("cost", userService.findCostSubjectByUserId(order.getUserId()));
        model.addAttribute("material_fee_list", configService.findMapByKey("material_fee"));
        model.addAttribute("package_fee_list", configService.findMapByKey("package_fee"));
        return "/admin/order/out_bound";
    }

    @RequestMapping(value = "/order/{id}/out_bound", method = RequestMethod.POST)
    public String postOutBound(
            @PathVariable long id,
            Model model,
            @RequestParam(required = false, defaultValue = "0") BigDecimal surcharge,
            @RequestParam(required = false, defaultValue = "") String surchargeComment,
            @RequestParam BigDecimal package_fee,
            @RequestParam BigDecimal material_fee,
            @RequestParam BigDecimal order_fee,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("orderStatus") Integer status
    ) {
        Order order = orderService.findOne(id);

        if (ObjectUtils.isEmpty(order)) {
            return "redirect:/admin/order/index";
        }

        List<Statements> statementsList = new ArrayList<>();
        BigDecimal t = BigDecimal.ZERO;

        Statements statements1 = payService.createORDER(order);
        statementsList.add(payService.saveStatements(statements1, order_fee));
        t = t.add(statements1.getTotal());

        //额外费用
        if (surcharge.compareTo(BigDecimal.ZERO) > 0) {
            t = t.add(surcharge);
            statementsList.add(payService.createWithSave(
                    order.getUserId(),
                    String.valueOf(order.getId()),
                    "运单额外收费原因：" + surchargeComment,
                    surcharge
            ));
        }

        //打包费
        if (package_fee.compareTo(BigDecimal.ZERO) > 0) {
            t = t.add(package_fee);
            statementsList.add(payService.createWithSave(
                    order.getUserId(),
                    String.valueOf(order.getId()),
                    "打包费",
                    package_fee
            ));
        }

        //物料费
        if (material_fee.compareTo(BigDecimal.ZERO) > 0) {
            t = t.add(material_fee);
            statementsList.add(payService.createWithSave(
                    order.getUserId(),
                    String.valueOf(order.getId()),
                    "物料费",
                    material_fee
            ));
        }

        order = orderService.outbound(order, t, surcharge, surchargeComment);

        boolean flag1 = payService.payStatements(statementsList);

        if (flag1) {
            messageService.send(
                    order.getUserId(),
                    MessageFormat.format("/order/{0}/show", order.getId()),
                    MessageFormat.format("出库单：{0}已经出库！", order.getSn())
            );
        } else {
            order.setNextStatus(order.getStatus());
            order.setStatus(OrderStatusEnum.FREEZE);
            orderRepository.save(order);

            messageService.send(
                    order.getUserId(),
                    MessageFormat.format("/order/{0}/show", order.getId()),
                    MessageFormat.format("出库单：{0}，扣费失败，请充值账号并重新缴费。", order.getSn())
            );
        }

        redirectAttributes.addFlashAttribute("success", "运单出库成功！");

        if (ObjectUtils.isEmpty(status) || status == -1) {
            return "redirect:/admin/order/index";
        } else {
            return "redirect:/admin/order/index?status=" + status;
        }
    }

    @RequestMapping(value = "/order/{id}/logistics", method = RequestMethod.GET)
    public String getUpdateLogistics(
            @PathVariable long id,
            Model model
    ) {
        Order order = orderService.findOne(id);
        OrderLogistics orderLogistics = orderService.findOrNewOrderLogistics(id);

        model.addAttribute("ele", order);
        model.addAttribute("fele", BeanMapperUtils.map(order, OrderFormVo.class).toMap());
        model.addAttribute("lg", orderLogistics);

        return "/admin/order/logistics";
    }

    @RequestMapping(value = "/order/{id}/logistics", method = RequestMethod.POST)
    public String postUpdateLogistics(
            @PathVariable long id,
            @RequestParam String comment,
            @RequestParam(required = false, defaultValue = "") String expressNo,
            @RequestParam(required = false, defaultValue = "") String express,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("orderStatus") Integer status
    ) {
        Order order = orderService.findOne(id);
        order.setExpressNo(expressNo);
        order.setExpress(express);
        orderRepository.save(order);

        OrderLogistics orderLogistics = orderService.findOrNewOrderLogistics(id);
        orderLogistics.setComment(comment);
        orderService.storeOrderLogistics(orderLogistics);

        redirectAttributes.addFlashAttribute("success", "更新物流信息成功");
        if (ObjectUtils.isEmpty(status) || status == -1) {
            return "redirect:/admin/order/index";
        } else {
            return "redirect:/admin/order/index?status=" + status;
        }
    }

    @RequestMapping(value = "/order/import", method = RequestMethod.GET)
    public String getImport(
            Model model
    ) {
        return "/admin/order/import";
    }

    /**
     * 导入
     */
    @RequestMapping(value = "/order/import", method = RequestMethod.POST)
    public String postImport(
            @RequestParam MultipartFile file,
            RedirectAttributes redirectAttributes
    ) throws BusinessException, CommonException {
        FormHelper formHelper = FormHelper.getInstance();
        if (file.isEmpty()) {
            formHelper.addError("file", "请上传文件！");
            try {
                formHelper.hasError();
            } catch (FormException e) {
                throwForm(redirectAttributes, e.getErrorField(), e.getForm());
                return "redirect:/admin/order/import";
            }
        }
        File tempFile = documentService.multipartFile2File(file);

        List<ExcelRow> excelRows = ExcelHelper.read(tempFile);

        log.info("{}", JSONMapper.toJSON(excelRows));
        for (ExcelRow row : excelRows) {
            int r = orderRepository.countBySn(row.getA());
            if (r != 1) {
                formHelper.addError("file", "发货单号：" + row.getA() + " 不存在！");
            } else {
                Order o = orderRepository.findTopBySn(row.getA());

                if (!o.getStatus().equals(OrderStatusEnum.CHECK)) {
                    formHelper.addError("file", "发货单号：" + row.getA() + " 的状态不是待审核状态！");
                }
            }

            try {
                BigDecimal b = new BigDecimal(row.getB());
            } catch (NumberFormatException e) {
                formHelper.addError("file", "发货单号：" + row.getA() + " 中，【运费】不合法！");
            }
        }

        try {
            formHelper.hasError();
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), e.getForm());
            return "redirect:/admin/order/import";
        }

        for (ExcelRow row : excelRows) {
            Order order = orderRepository.findTopBySn(row.getA());
            orderService.checkOut(order, new BigDecimal(row.getB()), row.getC(), row.getD());
        }

        redirectAttributes.addFlashAttribute("success", "出库单导入成功！");
        return "redirect:/admin/order/index";
    }
}

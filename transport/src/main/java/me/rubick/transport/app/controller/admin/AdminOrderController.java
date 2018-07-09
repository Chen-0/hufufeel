package me.rubick.transport.app.controller.admin;

import me.rubick.common.app.excel.ExcelRow;
import me.rubick.common.app.excel.ExcelWriter;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.exception.FormException;
import me.rubick.common.app.exception.HttpNoFoundException;
import me.rubick.common.app.helper.FormHelper;
import me.rubick.common.app.utils.BeanMapperUtils;
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
import java.util.List;

@Controller
@RequestMapping("/admin")
@SessionAttributes("orderStatus")
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
        Page<Order> orders = orderService.findAll(null, keyword, status, pageable);
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
        Page<Order> orders = orderService.findAll(null, keyword, status, pageable);
        List<Order> elements = orders.getContent();
        int row = elements.size();

        Object[][] context = new Object[row + 1][16];

        context[0][0] = "发货单号";
        context[0][1] = "交易号";
        context[0][2] = "参考号";
        context[0][3] = "派送方式";
        context[0][4] = "销售平台";
        context[0][5] = "仓库";
        context[0][6] = "总重量";
        context[0][7] = "姓名";
        context[0][8] = "电话";
        context[0][9] = "国家";
        context[0][10] = "州/省";
        context[0][11] = "城市";
        context[0][12] = "街道";
        context[0][13] = "门牌号";

        for (int i = 0; i < row; i++) {
            Order o = elements.get(i);
            if (!o.getStatus().equals(OrderStatusEnum.CHECK)) {
                continue;
            }
            o.setOrderSnapshotVo(JSONMapper.fromJson(o.getOrderSnapshot(), OrderSnapshotVo.class));
            context[i + 1][0] = o.getSn();
            context[i + 1][1] = o.getTn();
            context[i + 1][2] = o.getReferenceNumber();
            context[i + 1][3] = o.getOrderSnapshotVo().getCkt1();
            context[i + 1][4] = o.getOrderSnapshotVo().getCkt3();
            context[i + 1][5] = o.getWarehouseName();
            context[i + 1][6] = o.getWeight().toString();
            context[i + 1][7] = o.getContact();
            context[i + 1][8] = o.getPhone();
            context[i + 1][9] = o.getOrderSnapshotVo().getCkf1();
            context[i + 1][10] = o.getOrderSnapshotVo().getCkf3();
            context[i + 1][11] = o.getOrderSnapshotVo().getCkf5();
            context[i + 1][12] = o.getOrderSnapshotVo().getCkf10();
            context[i + 1][13] = o.getOrderSnapshotVo().getCkf11();
        }

        response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "HUFU-管理员-出库单.xlsx";
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

        Statements statements = payService.createORDER(order);
        model.addAttribute("ss", statements);
        model.addAttribute("ele", order);
        return "/admin/order/check_out";
    }

    @RequestMapping(value = "/order/{id}/check_out", method = RequestMethod.POST)
    public String postCheckOut(
            @RequestParam(required = false, defaultValue = "", name = "express_no") String expressNo,
            @RequestParam(required = false, defaultValue = "", name = "express") String express,
            @RequestParam(required = false) BigDecimal total,
            @PathVariable long id,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("orderStatus") Integer status
    ) throws HttpNoFoundException {
        Order order = orderService.findOne(id);

        if (ObjectUtils.isEmpty(order)) {
            throw new HttpNoFoundException();
        }

        List<Statements> statementsList = new ArrayList<>();
        Statements statements = payService.createORDER(order);
        statementsList.add(payService.saveStatements(statements, total));

        orderService.checkOut(order, statementsList.get(0).getTotal(), express, expressNo);

        boolean flag1 = payService.payStatements(statementsList);

        if (flag1) {
            messageService.send(
                    order.getUserId(),
                    MessageFormat.format("/order/{0}/show", order.getId()),
                    MessageFormat.format("出库单：{0}审核成功！", order.getSn())
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
            RedirectAttributes redirectAttributes,
            @ModelAttribute("orderStatus") Integer status
    ) {
        Order order = orderService.findOne(id);

        if (ObjectUtils.isEmpty(order)) {
            return "redirect:/admin/order/index";
        }

        List<Statements> statementsList = new ArrayList<>();
        BigDecimal t = BigDecimal.ZERO;

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
            @RequestParam(required = false, defaultValue = "", name = "express_no") String expressNo,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("orderStatus") Integer status
    ) {
        Order order = orderService.findOne(id);
        order.setExpressNo(expressNo);
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
        }
        File tempFile = documentService.multipartFile2File(file);

        List<ExcelRow> excelRows = ExcelHelper.read(tempFile);

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

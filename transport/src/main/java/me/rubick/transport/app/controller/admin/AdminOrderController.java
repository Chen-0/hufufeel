package me.rubick.transport.app.controller.admin;

import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.model.Package;
import me.rubick.transport.app.repository.OrderRepository;
import me.rubick.transport.app.service.OrderService;
import me.rubick.transport.app.service.PayService;
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
import java.text.MessageFormat;

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


    @RequestMapping("/order/index")
    public String adminPackageIndex(
            Model model,
            @PageableDefault(size = 25, direction = Sort.Direction.DESC, sort = {"id"}) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) Integer status
    ) {
        Page<Order> orders = orderService.findAll(null, keyword, status, pageable);
        model.addAttribute("elements", orders);
        model.addAttribute("status", status);

        if (!ObjectUtils.isEmpty(status)) {
            model.addAttribute("orderStatus", status);
        }

        return "/admin/order/index";
    }

    @RequestMapping(value = "/order/{id}/show", method = RequestMethod.GET)
    public String showOrder(
            @PathVariable long id,
            Model model
    ) {
        Order order = orderService.findOne(id);
        model.addAttribute("ele", order);
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
        } else if (name.equals("fail")) {
            order.setStatus(OrderStatusEnum.FAIL);
            order.setReason(comment);
        } else {
            return "redirect:/admin/product/index";
        }

        orderRepository.save(order);
        redirectAttributes.addFlashAttribute("success", "更新货品审核状态！");
        messageService.send(
                order.getUserId(),
                "/order/index",
                MessageFormat.format("出库单：{0} 审核成功！", order.getReferenceNumber())
        );

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
        return "/admin/order/out_bound";
    }

    @RequestMapping(value = "/order/{id}/out_bound", method = RequestMethod.POST)
    public String postOutBound(
            @PathVariable long id,
            Model model,
            @RequestParam(required = false) BigDecimal total,
            @RequestParam(required = false, defaultValue = "") String express,
            @RequestParam(required = false, defaultValue = "") String expressNo,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("orderStatus") Integer status
    ) {
        Order order = orderService.findOne(id);

        if (ObjectUtils.isEmpty(order)) {
            return "redirect:/admin/order/index";
        }


        Statements statements = payService.createORDER(order);
        statements = payService.saveStatements(statements, total);

        order = orderService.outbound(order, statements.getTotal(), express, expressNo);

        boolean flag = payService.payStatements(statements.getId());

        if (flag) {
            messageService.send(
                    order.getUserId(),
                    MessageFormat.format("/order/{0}/show", order.getId()),
                    MessageFormat.format("出库单：{0}已经出库！", order.getReferenceNumber())
            );
        } else {
            order.setNextStatus(order.getStatus());
            order.setStatus(OrderStatusEnum.FREEZE);
            orderRepository.save(order);

            messageService.send(
                    order.getUserId(),
                    MessageFormat.format("/order/{0}/show", order.getId()),
                    MessageFormat.format("出库单：{0}，扣费失败，请充值账号并重新缴费。", order.getReferenceNumber())
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
        model.addAttribute("lg", orderLogistics);

        return "/admin/order/logistics";
    }

    @RequestMapping(value = "/order/{id}/logistics", method = RequestMethod.POST)
    public String postUpdateLogistics(
            @PathVariable long id,
            @RequestParam String comment,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("orderStatus") Integer status
    ) {
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
}

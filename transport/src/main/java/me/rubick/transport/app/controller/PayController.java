package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.alipay.AlipayNotify;
import me.rubick.transport.app.alipay.AlipayService;
import me.rubick.transport.app.alipay.AlipaySubmit;
import me.rubick.transport.app.model.Payment;
import me.rubick.transport.app.model.PaymentType;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.PaymentRepository;
import me.rubick.transport.app.service.PayService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@Slf4j
public class PayController extends AbstractController {

    @Resource
    private AlipayService alipayService;

    @Resource
    private PayService payService;

    @RequestMapping(value = "/pay/alipay/target", method = RequestMethod.GET)
    public String toPay(
            Model model,
            @RequestParam long id
    ) throws IOException, BusinessException {
        User user = userService.getByLogin();
        Payment payment = payService.findPayment(id, user);

        if (ObjectUtils.isEmpty(payment)) {
            // TODO
            throw new BusinessException("");
        }

        Map<String, String> params = new HashMap<>();

        if (payment.getType().equals(PaymentType.ACCOUNT)) {
            params = alipayService.pay(
                    payment.getOutTradeNo(),
                    payment.getUserId(),
                    "账号充值 - HUFUFEEL（虎芙）",
                    "账号充值 - HUFUFEEL（虎芙）",
                    payment.getTotalFee()
            );
        }

        model.addAttribute("params", params);
        return "pay/alipay";
    }


    @RequestMapping("/pay/alipay/notify")
    @ResponseBody
    public String alipayNotify(@RequestParam Map<String, String> params, HttpServletRequest request) {
        log.info("------------------------------------------------------------------");
        log.info("PayController::alipayNotify::request.getParameterMap::{}", JSONMapper.toJSON(request.getParameterMap()));
        log.info("PayController::alipayNotify::params::{}", JSONMapper.toJSON(params));
        log.info("------------------------------------------------------------------");

        if (AlipayNotify.verify(params)) {
            log.info("PayController::alipayNotify::verify::success");
            try {
                payService.successForPayment(params.get("out_trade_no"));
            } catch (Exception e) {
                log.info("", e);
            }
        } else {
            log.info("PayController::alipayNotify::verify::fail");
        }

        return "SUCCESS";
    }

    @RequestMapping("/pay/alipay/success")
    public String alipaySuccess() {
        return "redirect:/pay/success";
    }

    @RequestMapping("/pay/success")
    @ResponseBody
    public String paySuccess() {
        return "SUCCESS - FOR PAY";
    }
}

package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.transport.app.model.Payment;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.service.PayService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * 个人中心
 */
@Controller
@Slf4j
public class UserController extends AbstractController {

    @Resource
    private PayService payService;

    @RequestMapping("/user/charge")
    public String chargeForAccount(
            @RequestParam BigDecimal total
            ) throws BusinessException {
        User user = userService.getByLogin();
        Payment payment = payService.createPaymentForAccount(user, total);

        return MessageFormat.format("redirect:/pay/alipay/target?id={0}", payment.getId());
    }
}

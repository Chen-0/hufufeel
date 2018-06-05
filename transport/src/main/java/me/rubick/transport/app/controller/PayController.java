package me.rubick.transport.app.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import me.rubick.transport.app.alipay.AlipayService;
import me.rubick.transport.app.alipay.AlipaySubmit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class PayController {

    @Resource
    private AlipayService alipayService;

    @RequestMapping(value = "/pay/target", method = RequestMethod.GET)
    public String toPay(
            Model model,
            @RequestParam long id
    ) throws IOException {
        Map<String, String> params = alipayService.pay(UUID.randomUUID().toString(), 1L, "支付测试 2", "支付测试 3", new BigDecimal(0.01));
        model.addAttribute("params", params);

        return "pay/alipay";
    }
}

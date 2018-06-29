package me.rubick.transport.app.alipay;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.JSONMapper;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.math.RoundingMode.FLOOR;

@Component
@Slf4j
public class AlipayService {

    private String SERVICE = "create_direct_pay_by_user";
    private String PARTNER = "2088221276655152";
    private String notify_url = "http://www.hufufeel.com/Pay/notifyurl";
    private String return_url = "http://www.hufufeel.com/Pay/returnurl";
    private String seller_email = "305929336.lp@gmail.com";
    private String anti_phishing_key = "";
    private String _input_charset = "utf-8";
    private String extra_common_param;


    public Map<String, String> pay(
            String outTradeNo,
            long userId,
            String subject,
            String body,
            BigDecimal totalFee
    ) {
        Map<String, String> map = new HashMap<>();

        map.put("service", "create_direct_pay_by_user");
        map.put("partner", "2088221276655152");
        map.put("notify_url", "http://www.hufufeel.com/pay/alipay/notify");
        map.put("return_url", "http://www.hufufeel.com/pay/alipay/success");
        map.put("seller_email", "305929336.lp@gmail.com");
        map.put("anti_phishing_key", "1234567890");
        map.put("exter_invoke_ip", "127.0.0.1");
        map.put("_input_charset", "utf-8");

        map.put("out_trade_no", outTradeNo);
        map.put("subject", subject);
        map.put("body", body);
        map.put("total_fee", totalFee.setScale(2, FLOOR).toString());
        map.put("extra_common_param", String.valueOf(userId));

        log.info("------------ 发起支付宝调用 --------------------");
        log.info("{}", JSONMapper.toJSON(map));

        Map<String, String> params = AlipaySubmit.submit(map);

        log.info("params={}", JSONMapper.toJSON(params));
        return params;
    }
}

package me.rubick.hufu.logistics.app.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EmailService {

//    @Resource
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("han_zh@yeah.net");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendInEmail(String to) {
        this.sendEmail(to, "虎芙转运网", "尊敬的客户：您的货物已入库。详情请登录虎芙官网查看 - 惟客惟尊");
    }

    public void sendMendEmail(String to) {
        this.sendEmail(to, "虎芙转运网", "尊敬的客户：您的运单需要补差价。详情请登录虎芙官网查看 - 惟客惟尊");
    }
}

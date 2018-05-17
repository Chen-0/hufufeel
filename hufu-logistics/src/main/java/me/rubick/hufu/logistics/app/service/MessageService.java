package me.rubick.hufu.logistics.app.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import me.rubick.hufu.logistics.app.model.Umsg;
import me.rubick.hufu.logistics.app.repository.UmsgRepository;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Transactional
public class MessageService {

    @Resource
    private UmsgRepository umsgRepository;

    public void sendInMessage(Integer userId) {
        Umsg umsg = new Umsg();
        umsg.setUid(userId);
        umsg.setContent("您的货物已入库");
        umsg.setRead_flag(false);
        umsg.setTime(new Date());
        umsgRepository.save(umsg);
    }


    public void sendMendMessage(Integer userId) {
        Umsg umsg = new Umsg();
        umsg.setUid(userId);
        umsg.setContent("您的运单需要补差价");
        umsg.setRead_flag(false);
        umsg.setTime(new Date());
        umsgRepository.save(umsg);
    }
}

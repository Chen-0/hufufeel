package me.rubick.transport.app.service;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.TextUtils;
import me.rubick.transport.app.model.Message;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Transactional
public class MessageService {

    @Resource
    private MessageRepository messageRepository;

    public Page<Message> findAll(User user, Pageable pageable) {
        return messageRepository.findByUserId(user.getId(), pageable);
    }

    public Message findOne(long id) {
        Message message = messageRepository.findOne(id);
        message.setIsRead(true);
        messageRepository.save(message);
        return  message;
    }

    public void send(long userId, String url, String context) {
        Message message = new Message();
        message.setUserId(userId);
        message.setContext(context);
        message.setIsRead(false);
        message.setTarget(url);

        messageRepository.save(message);
    }

    /**
     * 获取未读消息
     * @param user
     * @return
     */
    public int hasMessage(User user) {
        if (ObjectUtils.isEmpty(user)) {
            return 0;
        }

        int c = messageRepository.countByUserIdAndIsRead(user.getId(), false);
        return c;
    }

    public List<Message> getLatestMessage(User user) {
        if (ObjectUtils.isEmpty(user)) {
            return Collections.emptyList();
        }

        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "id");
        Sort sort = new Sort(order);
        PageRequest pageRequest = new PageRequest(0, 3, sort);
        Page<Message> messages = messageRepository.findByUserIdAndIsRead(user.getId(), false, pageRequest);
        return messages.getContent();
    }

    public void readAll(User user) {
        messageRepository.updateAllToRead(user.getId());
    }
}

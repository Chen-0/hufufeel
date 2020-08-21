package me.rubick.transport.app.controller;

import me.rubick.transport.app.model.Message;
import me.rubick.transport.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.MessageFormat;

@Controller
public class MessageController extends AbstractController {


    @RequestMapping(value = "/m/r", method = RequestMethod.GET)
    public String readMsg(
            @RequestParam long id
    ) {
        Message message = messageService.findOne(id);
        return MessageFormat.format("redirect:{0}", message.getTarget());
    }

    @RequestMapping(value = "/message/index", method = RequestMethod.GET)
    public String index(
            Model model,
            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        User user = userService.getByLogin();
        Page<Message> messages = messageService.findAll(user, pageable);
        model.addAttribute("elements", messages);
        return "message/index";
    }

    @RequestMapping(value = "/message/read/all", method = RequestMethod.GET)
    public String readAll() {
        User user = userService.getByLogin();
        messageService.readAll(user);
        return "redirect:/message/index";
    }
}
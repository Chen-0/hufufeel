package me.rubick.transport.app.controller;

import me.rubick.transport.app.service.UserService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class AuthController {

    @Resource
    private UserService userService;

//    @RequestMapping(value = "/register", method = RequestMethod.GET)
//    public String postRegister() {
//        return "auth/register";
//    }

//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public String postRegister(User user) {
//        userService.createUser(user);
//        return "redirect:/";
//    }
}

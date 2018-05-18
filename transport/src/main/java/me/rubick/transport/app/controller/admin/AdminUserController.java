package me.rubick.transport.app.controller.admin;

import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/user/")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/index")
    public String index(Model model) {
        List<User> users = userRepository.findAll();

        model.addAttribute("users", users);

        return "admin/user/index";
    }
}

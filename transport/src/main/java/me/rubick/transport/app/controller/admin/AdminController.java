package me.rubick.transport.app.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

    @RequestMapping("/admin/index")
    public String adminIndex() {
        return "admin/index";
    }
}

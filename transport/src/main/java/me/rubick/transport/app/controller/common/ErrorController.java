package me.rubick.transport.app.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping("/error/403")
    public String error403() {
        return "/error/403";
    }

    @RequestMapping("/error/page_not_found")
    public String pageNoFound() {
        return "/error/404";
    }
}

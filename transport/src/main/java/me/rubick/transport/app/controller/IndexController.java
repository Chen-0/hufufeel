package me.rubick.transport.app.controller;

import me.rubick.transport.app.repository.DocumentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class IndexController {

    @Resource
    private DocumentRepository documentRepository;


    @RequestMapping("/")
    public String index(Model model) {

        return "/index";
    }
}

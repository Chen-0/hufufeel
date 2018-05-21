package me.rubick.transport.app.controller;

import me.rubick.transport.app.repository.DocumentRepository;
import me.rubick.transport.app.utils.JsonMapper;
import me.rubick.transport.app.vo.ProductFormVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class IndexController {

    @Resource
    private DocumentRepository documentRepository;


    @RequestMapping("/")
    public String index(Model model) {

        return "/index";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String ddd(@Valid ProductFormVo productFormVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.print(bindingResult.getClass());
            System.out.println(JsonMapper.toJson(bindingResult.getModel()));
            return bindingResult.toString();
        }
        return JsonMapper.toJson(productFormVo);
    }
}

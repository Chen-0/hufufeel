package me.rubick.transport.app.controller;

import me.rubick.transport.app.repository.DocumentRepository;
import me.rubick.transport.app.vo.ProductFormVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class IndexController {

    @Resource
    private DocumentRepository documentRepository;

    @RequestMapping("/")
    public String root() {
        return "redirect:/index";
    }

    /**
     * 首页
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model) {
        return "/index/index";
    }

    /**
     * 关于我们
     * @return
     */
    @RequestMapping("/about_us")
    public String aboutUs() {
        return "/index/about_us";
    }

    /**
     * 费用说明
     * @return
     */
    @RequestMapping("/cost")
    public String cost() {
        return "index/cost";
    }

    /**
     * 常见问题
     * @return
     */
    @RequestMapping("/qa")
    public String qa() {
        return "index/qa";
    }

    @RequestMapping("/strategy")
    public String strategy() {
        return "index/strategy";
    }

    @RequestMapping("/contact_us")
    public String contactUs() {
        return "index/contact_us";
    }

    @RequestMapping("/test")
    public String ddd(@Valid ProductFormVo productFormVo, BindingResult bindingResult) {
        return "index";
    }
}

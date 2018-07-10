package me.rubick.transport.app.controller;

import me.rubick.common.app.response.RestResponse;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.transport.app.model.Notice;
import me.rubick.transport.app.repository.DocumentRepository;
import me.rubick.transport.app.repository.NoticeRepository;
import me.rubick.transport.app.vo.ProductFormVo;
import me.rubick.transport.app.vo.admin.NoticeFormVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
public class IndexController {

    @Resource
    private DocumentRepository documentRepository;

    @Resource
    private NoticeRepository noticeRepository;

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
    public String index(
            @PageableDefault(size = 5, direction = Sort.Direction.DESC, sort = "id") Pageable pageable,
            Model model
    ) {
        model.addAttribute("notices", noticeRepository.findAll(pageable).getContent());
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
}

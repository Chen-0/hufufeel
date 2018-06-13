package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.UserRepository;
import me.rubick.transport.app.service.UserService;
import me.rubick.transport.app.vo.CostSubjectSnapshotVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/user/")
@Slf4j
public class AdminUserController extends AbstractController {

    @Autowired
    private UserRepository userRepository;

    @Resource
    private UserService userService;

    @RequestMapping("/index")
    public String index(Model model) {
        List<User> users = userService.findAll("ROLE_HWC");

        model.addAttribute("elements", users);

        return "admin/user/index";
    }

    @RequestMapping(value = "/{id}/cost_subject", method = RequestMethod.GET)
    public String userCostSubject(
            @PathVariable("id") long id,
            Model model
    ) {
        User user = userRepository.findOne(id);
        model.addAttribute("user", user);
        return "/admin/user/cost_subject";
    }

    @RequestMapping(value = "/{id}/cost_subject", method = RequestMethod.POST)
    @ResponseBody
    public String postUserCostSubject(
            @PathVariable("id") long id,
//            CostSubjectSnapshotVo costSubjectSnapshotVo
            @RequestParam("rkt") String rkt,
            @RequestParam("rkv") BigDecimal rkv,
            @RequestParam("sjt") String sjt,
            @RequestParam("sjv") BigDecimal sjv,
            @RequestParam("ddt") String ddt,
            @RequestParam("ddv[]") List<BigDecimal> ddv,
            RedirectAttributes redirectAttributes
    ) {
        User user = userRepository.findOne(id);
        CostSubjectSnapshotVo costSubjectSnapshotVo = new CostSubjectSnapshotVo();

        costSubjectSnapshotVo.setRkt(rkt);
        costSubjectSnapshotVo.setRkv(rkv);
        costSubjectSnapshotVo.setSjt(sjt);
        costSubjectSnapshotVo.setSjv(sjv);
        costSubjectSnapshotVo.setDdt(ddt);
        costSubjectSnapshotVo.setDdv(ddv);
        log.info("{}", JSONMapper.toJSON(costSubjectSnapshotVo));

        userService.storeCostSubject(user, costSubjectSnapshotVo);
        redirectAttributes.addFlashAttribute("SUCCESS", "设置费用成功！");
        return  "redirect:/admin/user/index";
    }
}

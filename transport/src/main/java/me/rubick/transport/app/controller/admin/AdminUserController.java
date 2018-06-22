package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.OrderStatusEnum;
import me.rubick.transport.app.model.Statements;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.UserRepository;
import me.rubick.transport.app.service.PayService;
import me.rubick.transport.app.service.UserService;
import me.rubick.transport.app.vo.CostSubjectSnapshotVo;
import me.rubick.transport.app.vo.UserCsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

@Controller
@RequestMapping("/admin/user/")
@Slf4j
public class AdminUserController extends AbstractController {

    @Autowired
    private UserRepository userRepository;

    @Resource
    private UserService userService;

    @Resource
    private PayService payService;

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

        CostSubjectSnapshotVo costSubjectSnapshotVo = userService.findCostSubjectByUserId(user.getId());
        if (ObjectUtils.isEmpty(costSubjectSnapshotVo)) {
            costSubjectSnapshotVo = new CostSubjectSnapshotVo();
            costSubjectSnapshotVo.setRkt("RK-AZ");
            costSubjectSnapshotVo.setRkv(BigDecimal.ZERO);
            costSubjectSnapshotVo.setSjt("SJ-AS");
            costSubjectSnapshotVo.setSjv(BigDecimal.ZERO);
            costSubjectSnapshotVo.setDdt("DD-AZ");
            costSubjectSnapshotVo.setDdv(null);
        }

        model.addAttribute("user", user);
        model.addAttribute("cs", costSubjectSnapshotVo);

        model.addAttribute("INP_RK_AZ", configService.findByKey("INP-RK-AZ"));
        model.addAttribute("INP_RK_AX", configService.findByKey("INP-RK-AX"));
        model.addAttribute("INP_SJ_AS", configService.findByKey("INP-SJ-AS"));
        model.addAttribute("INP_SJ_AJ", configService.findByKey("INP-SJ-AJ"));
        model.addAttribute("INP_DD_AZ_1", configService.findByKey("INP_DD_AZ_1"));
        model.addAttribute("INP_DD_AZ_2", configService.findByKey("INP_DD_AZ_2"));
        model.addAttribute("INP_DD_AZ_3", configService.findByKey("INP_DD_AZ_3"));
        model.addAttribute("INP_DD_AZ_4", configService.findByKey("INP_DD_AZ_4"));
        model.addAttribute("INP_DD_AJ", configService.findByKey("INP-DD-AJ"));
        return "/admin/user/cost_subject";
    }

    @RequestMapping(value = "/{id}/cost_subject", method = RequestMethod.POST)
    public String postUserCostSubject(
            @PathVariable("id") long id,
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
        return "redirect:/admin/user/index";
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getCreateUser() {
        return "/admin/user/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String postCreateUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String csPhone,
            @RequestParam String csQQ,
            RedirectAttributes redirectAttributes
    ) {
        try {
            UserCsVo userCsVo = new UserCsVo();
            userCsVo.setCsPhone(csPhone);
            userCsVo.setCsQQ(csQQ);

            User user = new User();
            user.setName(name);
            user.setUsername(username);
            user.setPassword(password);
            user.setUsd(BigDecimal.ZERO);
            user.setCsInfo(JSONMapper.toJSON(userCsVo));

            user = userService.createUser(user);

            redirectAttributes.addFlashAttribute("success", "创建用户成功！");
            return "redirect:/admin/user/" + user.getId() + "/cost_subject";
        } catch (IllegalArgumentException e) {
            log.error("", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("username", username);
            redirectAttributes.addFlashAttribute("password", password);
            redirectAttributes.addFlashAttribute("name", name);
            return "redirect:/admin/user/create";

        }
    }

    @RequestMapping(value = "/store_cost", method = RequestMethod.GET)
    public String getStoreCost(
            @RequestParam("ids[]") List<Long> ids,
            Model model
    ) {
        List<User> users = userService.findByIdIn(ids);

        model.addAttribute("elements", users);

        return "/admin/user/store_cost";
    }

    @RequestMapping(value = "/store_cost", method = RequestMethod.POST)
    public String postStoreCost(
            @RequestParam("ids[]") List<Long> ids,
            @RequestParam BigDecimal total,
            RedirectAttributes redirectAttributes
    ) {
        List<User> users = userService.findByIdIn(ids);

        for (User user: users) {
            Statements statements = payService.createSTORECOST(user, total);

            boolean flag = payService.payStatements(statements.getId());

            if (flag) {
                userService.updateUserFreeze(user.getId());
                messageService.send(
                        user.getId(),
                        "/user/index",
                        MessageFormat.format("仓租费一共 {0} USD， 扣款成功！", statements.getTotal())
                );
            } else {
                userService.updateUserFreeze(user.getId());

                messageService.send(
                        user.getId(),
                        "/user/index",
                        MessageFormat.format("仓租费一共 {0} USD，扣款失败，请充值账号并重新缴费。", statements.getTotal())
                );
            }
        }

        redirectAttributes.addFlashAttribute("success", "仓租费批量扣费成功！");
        return "redirect:/admin/user/index";
    }
}

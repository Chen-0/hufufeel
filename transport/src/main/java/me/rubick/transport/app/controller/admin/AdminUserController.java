package me.rubick.transport.app.controller.admin;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.CommonException;
import me.rubick.common.app.exception.FormException;
import me.rubick.common.app.exception.HttpNoFoundException;
import me.rubick.common.app.helper.FormHelper;
import me.rubick.common.app.utils.BeanMapperUtils;
import me.rubick.common.app.utils.DateUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.model.Statements;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.repository.UserRepository;
import me.rubick.transport.app.service.PayService;
import me.rubick.transport.app.service.UserService;
import me.rubick.transport.app.vo.CostSubjectSnapshotVo;
import me.rubick.transport.app.vo.UserCsVo;
import me.rubick.transport.app.vo.admin.RechargeFormVo;
import me.rubick.transport.app.vo.admin.UserCreateVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

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
    public String index(
            @RequestParam(defaultValue = "", required = false) String keyword,
            Model model
    ) {
        List<User> users = userService.findAll("ROLE_HWC", keyword);

        model.addAttribute("elements", users);
        model.addAttribute("keyword", keyword);

        return "admin/user/index";
    }

    @RequestMapping(value = "/{id}/cost_subject", method = RequestMethod.GET)
    public String userCostSubject(
            @PathVariable("id") long id,
            Model model
    ) {
        User user = userService.findOne(id);

        CostSubjectSnapshotVo costSubjectSnapshotVo = userService.findCostSubjectByUserId(user.getId());
        if (ObjectUtils.isEmpty(costSubjectSnapshotVo)) {
            costSubjectSnapshotVo = new CostSubjectSnapshotVo();
            costSubjectSnapshotVo.setRkt("RK-AZ");
            costSubjectSnapshotVo.setRkv(BigDecimal.ZERO);
            costSubjectSnapshotVo.setSjt("SJ-AS");
            costSubjectSnapshotVo.setSjv(BigDecimal.ZERO);
            costSubjectSnapshotVo.setDdt("DD-AZ");
            costSubjectSnapshotVo.setDdv(null);
            costSubjectSnapshotVo.setThrkt("TH_RK_AD");
            costSubjectSnapshotVo.setThrkv(BigDecimal.ZERO);
            costSubjectSnapshotVo.setThsjt("TH_SJ_SL");
            costSubjectSnapshotVo.setThsjv(BigDecimal.ZERO);
        }

        UserCreateVo userCreateVo = BeanMapperUtils.map(user, UserCreateVo.class);
        userCreateVo.setCsPhone(user.getUserCsVo().getCsPhone());
        userCreateVo.setCsQQ(user.getUserCsVo().getCsQQ());

        model.addAttribute("user", user);
        model.addAttribute("cs", costSubjectSnapshotVo);
        model.addAttribute("fele", userCreateVo.toMap());

        model.addAttribute("INP_RK_AZ", configService.findByKey("INP-RK-AZ"));
        model.addAttribute("INP_RK_AX", configService.findByKey("INP-RK-AX"));
        model.addAttribute("INP_SJ_AS", configService.findByKey("INP-SJ-AS"));
        model.addAttribute("INP_SJ_AJ", configService.findByKey("INP-SJ-AJ"));
        model.addAttribute("INP_DD_AZ_1", configService.findByKey("INP_DD_AZ_1"));
        model.addAttribute("INP_DD_AZ_2", configService.findByKey("INP_DD_AZ_2"));
        model.addAttribute("INP_DD_AZ_3", configService.findByKey("INP_DD_AZ_3"));
        model.addAttribute("INP_DD_AZ_4", configService.findByKey("INP_DD_AZ_4"));
        model.addAttribute("INP_DD_AJ", configService.findByKey("INP-DD-AJ"));

        //退货单
        model.addAttribute("INP_TH_RK_AD", configService.findByKey("INP_TH_RK_AD"));
        model.addAttribute("INP_TH_SJ_SL", configService.findByKey("INP_TH_SJ_SL"));
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
            @RequestParam String thrkt,
            @RequestParam BigDecimal thrkv,
            @RequestParam String thsjt,
            @RequestParam BigDecimal thsjv,
            @RequestParam String csPhone,
            @RequestParam String csQQ,
            RedirectAttributes redirectAttributes
    ) {
        User user = userService.findOne(id);

        try {
            FormHelper formHelper = FormHelper.getInstance();
            formHelper.notNull(csPhone, "csPhone");
            formHelper.notNull(csQQ, "csQQ");
            formHelper.hasError();
        } catch (FormException e) {
            redirectAttributes.addFlashAttribute("fele", e.getForm());
            redirectAttributes.addFlashAttribute("ferror", e.getErrorField());
            return "redirect:/admin/user/" + user.getId() + "/cost_subject";
        }

        user.getUserCsVo().setCsQQ(csQQ);
        user.getUserCsVo().setCsPhone(csPhone);
        userService.update(user);

        CostSubjectSnapshotVo costSubjectSnapshotVo = new CostSubjectSnapshotVo();
        costSubjectSnapshotVo.setRkt(rkt);
        costSubjectSnapshotVo.setRkv(rkv);
        costSubjectSnapshotVo.setSjt(sjt);
        costSubjectSnapshotVo.setSjv(sjv);
        costSubjectSnapshotVo.setDdt(ddt);
        costSubjectSnapshotVo.setDdv(ddv);
        costSubjectSnapshotVo.setThrkt(thrkt);
        costSubjectSnapshotVo.setThrkv(thrkv);
        costSubjectSnapshotVo.setThsjt(thsjt);
        costSubjectSnapshotVo.setThsjv(thsjv);

        userService.storeCostSubject(user, costSubjectSnapshotVo);
        redirectAttributes.addFlashAttribute("SUCCESS", "设置费用成功！");
        return "redirect:/admin/user/index";
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String getCreateUser(Model model) {
        if (ObjectUtils.isEmpty(model.asMap().get("fele"))) {
            UserCreateVo userCreateVo = new UserCreateVo();
            userCreateVo.setHwcSn(userService.generateSn());
            model.addAttribute("fele", userCreateVo.toMap());
        }
        return "/admin/user/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String postCreateUser(
            @RequestParam String json,
            RedirectAttributes redirectAttributes
    ) {
        log.info("{}", json);
        UserCreateVo userCreateVo = JSONMapper.fromJson(json, UserCreateVo.class);

        try {
            FormHelper formHelper = FormHelper.getInstance();
            formHelper.notNull(userCreateVo.getName(), "name");
            formHelper.notNull(userCreateVo.getUsername(), "username");
            formHelper.notNull(userCreateVo.getPassword(), "password");
            formHelper.notNull(userCreateVo.getHwcSn(), "hwcSn");
            formHelper.notNull(userCreateVo.getCsPhone(), "csPhone");
            formHelper.notNull(userCreateVo.getCsQQ(), "csQQ");

            String pattern = "^[0-9]{6}";
            boolean isMatch = Pattern.matches(pattern, userCreateVo.getHwcSn());

            if (!isMatch) {
                formHelper.addError("hwcSn", "请输入6位数字");
            }
            formHelper.hasError();
        } catch (FormException e) {
            redirectAttributes.addFlashAttribute("ferror", e.getErrorField());
            redirectAttributes.addFlashAttribute("fele", userCreateVo.toMap());
            return "redirect:/admin/user/create";
        }

        try {
            UserCsVo userCsVo = new UserCsVo();
            userCsVo.setCsPhone(userCreateVo.getCsPhone());
            userCsVo.setCsQQ(userCreateVo.getCsQQ());

            User user = BeanMapperUtils.map(userCreateVo, User.class);
            user.setUsd(BigDecimal.ZERO);
            user.setCsInfo(JSONMapper.toJSON(userCsVo));
            user = userService.createUser(user);

            redirectAttributes.addFlashAttribute("success", "创建用户成功！");
            return "redirect:/admin/user/" + user.getId() + "/cost_subject";
        } catch (FormException e) {
            redirectAttributes.addFlashAttribute("ferror", e.getErrorField());
            redirectAttributes.addFlashAttribute("fele", userCreateVo);
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

        for (User user : users) {
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


    @RequestMapping(value = "/{id}/password", method = RequestMethod.GET)
    public String getPassword(
            @PathVariable long id,
            Model model
    ) {
        User user = userService.findOne(id);

        model.addAttribute("user", user);

        if (ObjectUtils.isEmpty(model.asMap().get("fele"))) {
            model.addAttribute("fele", new HashMap<String, String>());
        }

        return "/admin/user/password";
    }

    @RequestMapping(value = "/{id}/password", method = RequestMethod.POST)
    public String postPassword(
            @PathVariable long id,
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws HttpNoFoundException {
        User user = userService.findOne(id);

        if (ObjectUtils.isEmpty(user)) {
            throw new HttpNoFoundException();
        }

        try {
            FormHelper formHelper = FormHelper.getInstance();
            formHelper.validateDefault0("password", password);
            formHelper.hasError();
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), e.getForm());
            return "redirect:/admin/user/" + user.getId() + "/password";
        }

        model.addAttribute("user", user);

        userService.resetPassword(user, password);
        redirectAttributes.addFlashAttribute("success", "重置密码成功！");
        return "redirect:/admin/user/index";
    }

    @RequestMapping(value = "/{id}/charge", method = RequestMethod.GET)
    public String getCharge(
            @PathVariable long id,
            Model model
    ) {
        User user = userService.findOne(id);

        model.addAttribute("user", user);

        if (ObjectUtils.isEmpty(model.asMap().get("fele"))) {
            model.addAttribute("fele", new HashMap<String, String>());
        }

        return "/admin/user/charge";
    }

    @RequestMapping(value = "/{id}/charge", method = RequestMethod.POST)
    public String postCharge(
            @PathVariable long id,
            @RequestParam String delta,
            Model model,
            RedirectAttributes redirectAttributes
    ) throws HttpNoFoundException {
        User user = userService.findOne(id);

        if (ObjectUtils.isEmpty(user)) {
            throw new HttpNoFoundException();
        }

        try {
            FormHelper formHelper = FormHelper.getInstance();
            formHelper.validateDefault0("delta", delta);
            formHelper.mustDecimal(delta, "delta");
            formHelper.hasError();
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), e.getForm());
            return "redirect:/admin/user/" + user.getId() + "/charge";
        }

        model.addAttribute("user", user);

        userService.chargeUser(user, new BigDecimal(delta));
        redirectAttributes.addFlashAttribute("success", "修改余额成功！");
        return "redirect:/admin/user/index";
    }

    @RequestMapping(value = "/statements/index", method = RequestMethod.GET)
    public String getStatements(
            Model model,
            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String startAt,
            @RequestParam(required = false) String endAt) {
//        User user = userService.findOne(userId);

        Date start = DateUtils.stringToDate(startAt);
        Date end = DateUtils.stringToDate(endAt);
        Page<Statements> statementsPage = payService.findAllStatements(userId, start, end, pageable);

        model.addAttribute("users", userService.findAll());
        model.addAttribute("elements", statementsPage);
        model.addAttribute("startAt", startAt);
        model.addAttribute("endAt", endAt);
        model.addAttribute("userId", userId);

        return "/admin/user/statements";
    }

    @RequestMapping(value = "/statements/export", method = RequestMethod.GET)
    public void exportStatements(
            Model model,
            @PageableDefault(size = Integer.MAX_VALUE, sort = {"id"}, direction = Sort.Direction.DESC, page = 0) Pageable pageable,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String startAt,
            @RequestParam(required = false) String endAt,
            HttpServletResponse response) throws IOException, CommonException {
        User user = null;

        if (! ObjectUtils.isEmpty(userId)) {
            user = userService.findOne(userId);
        }
//        User user = userService.getByLogin();

//        if (ObjectUtils.isEmpty(user)) {
//            return;
//        }

        Date start = DateUtils.stringToDate(startAt);
        Date end = DateUtils.stringToDate(endAt);
        Page<Statements> statementsPage = payService.findAllStatements(userId, start, end, pageable);
        List<Statements> statements = statementsPage.getContent();

        Workbook workbook = payService.anaStatements(user, statements);
        response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "HUFU-ADMIN-费用明细.xlsx";
        fileName = URLEncoder.encode(fileName, "utf-8");
        response.setHeader("Content-Disposition", MessageFormat.format("attachment; filename*=\"{0}\"", fileName));
        workbook.write(response.getOutputStream());
    }

    @RequestMapping(value = "/{id}/recharge", method = RequestMethod.GET)
    public String getRecharge(
            @PathVariable long id,
            Model model
    ) {
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        if (!model.asMap().containsKey("fele")) {
            model.addAttribute("fele", new HashMap<String, String>());
        }
        return "/admin/user/recharge";
    }

    @RequestMapping(value = "/{id}/recharge", method = RequestMethod.POST)
    public String PostRecharge(
            @PathVariable long id,
            @RequestParam String json,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        User user = userService.findOne(id);
        RechargeFormVo rechargeFormVo = JSONMapper.fromJson(json, RechargeFormVo.class);

        try {
            FormHelper formHelper = FormHelper.getInstance();
            formHelper.validateDefault0("total", rechargeFormVo.getTotal());
            formHelper.validateDefault0("comment", rechargeFormVo.getComment());

            formHelper.hasError();
        } catch (FormException e) {
            throwForm(redirectAttributes, e.getErrorField(), rechargeFormVo);
            return "redirect:/admin/user/" + user.getId() + "/recharge";
        }

        Statements statements = payService.createRecharge(user, rechargeFormVo.getTotal(), rechargeFormVo.getComment());
        payService.payStatements(statements.getId());

        redirectAttributes.addFlashAttribute("success", "补收费成功！");
        return "redirect:/admin/user/index";
    }
}

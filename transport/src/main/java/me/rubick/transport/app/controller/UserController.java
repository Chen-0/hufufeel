package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelWriter;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.utils.DateUtils;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.OrderStatusEnum;
import me.rubick.transport.app.constants.PackageStatusEnum;
import me.rubick.transport.app.constants.PaymentStatusEnum;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.OrderRepository;
import me.rubick.transport.app.repository.PackageRepository;
import me.rubick.transport.app.repository.PaymentRepository;
import me.rubick.transport.app.service.PayService;
import me.rubick.transport.app.vo.CostSubjectSnapshotVo;
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
import java.util.List;

/**
 * 个人中心
 */
@Controller
@Slf4j
public class UserController extends AbstractController {

    @Resource
    private PayService payService;

    @Resource
    private PaymentRepository paymentRepository;

    @RequestMapping(value = "/user/charge_account", method = RequestMethod.GET)
    public String getChargeAccount(Model model) {
        model.addAttribute("U2R", configService.findOneByKey("U2R"));
        return "user/charge_account";
    }

    @RequestMapping(value = "/user/charge_account", method = RequestMethod.POST)
    public String postChargeAccount(
            @RequestParam BigDecimal total,
            RedirectAttributes redirectAttributes
    ) throws BusinessException {
        User user = userService.getByLogin();

        if (ObjectUtils.isEmpty(total)) {
            redirectAttributes.addFlashAttribute("nu", "请输入充值金额！");
            return "redirect:/user/charge_account";
        }

        try {
            Payment payment = payService.createPaymentForAccount(user, total);
            return MessageFormat.format("redirect:/pay/alipay/target?id={0}", payment.getId());
        } catch (BusinessException e) {
            log.warn("{}", e.getMessage());
        } catch (Exception e) {
            log.error("", e);
        }

        return "redirect:/user/charge_account";
    }

    @RequestMapping(value = "/user/statements/index", method = RequestMethod.GET)
    public String getStatements(
            Model model,
            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String startAt,
            @RequestParam(required = false) String endAt) {
        User user = userService.getByLogin();

        Date start = DateUtils.stringToDate(startAt);
        Date end = DateUtils.stringToDate(endAt);
        Page<Statements> statementsPage = payService.findAllStatements(user.getId(), start, end, pageable);
        model.addAttribute("elements", statementsPage);
        model.addAttribute("startAt", startAt);
        model.addAttribute("endAt", endAt);

        return "/user/statements";
    }

    @RequestMapping(value = "/user/statements/export", method = RequestMethod.GET)
    public void exportStatements(
            Model model,
            @PageableDefault(size = Integer.MAX_VALUE, sort = {"id"}, direction = Sort.Direction.DESC, page = 0) Pageable pageable,
            @RequestParam(required = false) String startAt,
            @RequestParam(required = false) String endAt,
            HttpServletResponse response) throws IOException {
        User user = userService.getByLogin();

        Date start = DateUtils.stringToDate(startAt);
        Date end = DateUtils.stringToDate(endAt);
        Page<Statements> statementsPage = payService.findAllStatements(user.getId(), start, end, pageable);
        List<Statements> statements = statementsPage.getContent();

        int row = statements.size();
        Object[][] context = new Object[row][7];
        context[0][0] = "编号";
        context[0][1] = "费用说明";
        context[0][2] = "费用类型";
        context[0][3] = "支付状态";
        context[0][4] = "金额";
        context[0][5] = "创建时间";
        context[0][6] = "支付时间";

        for (int i = 1; i < row; i++) {
            Statements s = statements.get(i);

            context[i][0] = i;
            context[i][1] = s.getComment();
            context[i][2] = s.getType().getValue();
            context[i][3] = s.getStatus().getValue();
            context[i][4] = s.getTotal().toString();
            context[i][5] = DateUtils.date2StringYMDHMS(s.getCreatedAt());
            context[i][6] = DateUtils.date2StringYMDHMS(s.getPayAt());
        }

        log.info("{}", JSONMapper.toJSON(context));

        response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String fileName = "HUFU-"+user.getHwcSn()+"-费用明细.xlsx";
        fileName = URLEncoder.encode(fileName, "utf-8");
        response.setHeader("Content-Disposition", MessageFormat.format("attachment; filename*=\"{0}\"", fileName));
        ExcelWriter.getExcelInputSteam(context, response.getOutputStream());
    }

    @RequestMapping(value = "/user/statements/{id}/pay", method = RequestMethod.GET)
    public String payStatements(
            Model model,
            @PathVariable long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Statements statements = payService.secPayStatements(id);
            redirectAttributes.addFlashAttribute("SUCCESS", "支付成功！");

            switch (statements.getType()) {
                case ORDER:
                    return "redirect:/order/" + statements.getTarget() + "/show";
                case SJ:
                    return "redirect:/package/" + statements.getTarget() + "/show";
                case RK:
                    return "redirect:/package/" + statements.getTarget() + "/show";
            }
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("ERROR", e.getMessage());
        }

        return "redirect:/user/statements/index";
    }

    @Resource
    private OrderRepository orderRepository;

    @Resource
    private PackageRepository packageRepository;

    @RequestMapping(value = "/user/index", method = RequestMethod.GET)
    public String indexUser(Model model) {
        User user = userService.getByLogin();
        CostSubjectSnapshotVo costSubjectSnapshotVo = userService.findCostSubjectByUserId(user.getId());

        long oc = orderRepository.countByUserIdAndStatus(user.getId(), OrderStatusEnum.FREEZE);
        long pc = packageRepository.countByUserIdAndStatus(user.getId(), PackageStatusEnum.FREEZE);

        model.addAttribute("oc", oc);
        model.addAttribute("pc", pc);
        model.addAttribute("user", user);
        model.addAttribute("cc", costSubjectSnapshotVo);

        return "/user/index";
    }

    @RequestMapping(value = "/user/charge_index", method = RequestMethod.GET)
    public String chargeIndex(
            @PageableDefault(direction = Sort.Direction.DESC, size = Integer.MAX_VALUE) Pageable pageable,
            Model model) {
        User user = userService.getByLogin();
        Page<Payment> payments = paymentRepository.findByUserIdAndStatus(user.getId(), PaymentStatusEnum.TRUE, pageable);

        model.addAttribute("elements", payments);

        return "user/charge_index";
    }


}

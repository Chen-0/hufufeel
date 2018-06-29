package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.constants.OrderStatusEnum;
import me.rubick.transport.app.constants.PackageStatusEnum;
import me.rubick.transport.app.model.*;
import me.rubick.transport.app.repository.OrderRepository;
import me.rubick.transport.app.repository.PackageRepository;
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
import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * 个人中心
 */
@Controller
@Slf4j
public class UserController extends AbstractController {

    @Resource
    private PayService payService;

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
    public String getStatements(Model model, @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        User user = userService.getByLogin();
        Page<Statements> statementsPage = payService.findAllStatements(user.getId(), pageable);
        model.addAttribute("elements", statementsPage);
        return "/user/statements";
    }

    @RequestMapping(value = "/user/statements/{id}/pay", method = RequestMethod.GET)
    public String payStatements(
            Model model,
            @PathVariable long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            payService.secPayStatements(id);
            redirectAttributes.addFlashAttribute("SUCCESS", "支付成功！");
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
}

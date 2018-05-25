package me.rubick.hufu.logistics.app.controller;

import me.rubick.hufu.logistics.app.library.Common;
import me.rubick.hufu.logistics.app.model.User;
import me.rubick.hufu.logistics.app.service.WaybillService;
import me.rubick.hufu.logistics.app.utils.JSONMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import me.rubick.hufu.logistics.app.model.Address;
import me.rubick.hufu.logistics.app.model.Waybill;
import me.rubick.hufu.logistics.app.service.UserService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserService userService;

    @Resource
    private WaybillService waybillService;

    @RequestMapping(value = "index", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(
            @PageableDefault(size = 50) Pageable pageable,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            Model model
    ) {
        keyword = Common.encodeStr(keyword);
        System.out.println(keyword);
        Page<User> users = userService.findByCustomerLike(pageable, keyword);

        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);

        return "user/index";
    }

    //@TODO 审核列表
    @RequestMapping(value = "address/index", method = RequestMethod.GET)
    public String checkList(
            Model model
    ) {
        List<Address> addresses = userService.getUnCheckAddress();
        model.addAttribute("addresses", addresses);
        logger.info("找到未审核的地址：{} 个", addresses.size());

        return "address/index";
    }


    @RequestMapping(value = "address/pass", method = RequestMethod.POST)
    public String pass(
            @RequestParam("id") Integer id,
            RedirectAttributes redirectAttributes
    ) {
        Address address = userService.findAddress(id);

        Common.checkNotNull(address);

        address.setVerify("Ok");
        userService.saveAddress(address);

        redirectAttributes.addFlashAttribute("success", "审核成功！");

        return "redirect:/user/address/index";
    }

    //@TODO 更新个人信息
    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String getUpdate(
            @PathVariable("id") Integer id,
            Model model
    ) {
        User user = userService.findUser(id);
        Common.checkNotNull(user);

        System.out.println(user.getAddresses().size());

        model.addAttribute("user", user);

        return "user/update";
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String postUpdate(
            @PathVariable("id") Integer id,
            User user1,
            RedirectAttributes redirectAttributes
    ) {
        User user = userService.findUser(id);
        logger.info("更新前： {}", JSONMapper.toJSON(user));
        Common.checkNotNull(user);

        user.setUserna(user1.getUserna());
        user.setUsername(user1.getUsername());
        user.setIntegral(user1.getIntegral());
        user.setXing(user1.getXing());
        user.setMing(user1.getMing());
        user.setAddress(user1.getAddress());
        user.setMail(user1.getMail());
        user.setPhone(user1.getPhone());
        user.setQq(user1.getQq());
        user.setBeizhu(user1.getBeizhu());

        if (!user.getRole_id().equals(user1.getRole_id())) {
            user.setRole_id(user1.getRole_id());

            switch (user1.getRole_id()) {
                case 1:
                    user.setTotal(new BigDecimal(0));
                    break;
                case 2:
                    user.setTotal(new BigDecimal(1000));
                    break;
                case 3:
                    user.setTotal(new BigDecimal(2000));
                    break;
                case 4:
                    user.setTotal(new BigDecimal(3000));
                    break;

            }
        }

        logger.info("更新后：{}", JSONMapper.toJSON(user));

        userService.update(user);

        redirectAttributes.addFlashAttribute("success", "修改成功！");

        return "redirect:/user/index";
    }

    @RequestMapping(value = "{id}/destroy", method = RequestMethod.POST)
    public String destroy(
            @PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes
    ) {
        User user = userService.findUser(id);
        //删除用户的所有订单
        List<Waybill> waybills = user.getWaybills();
        if (waybills != null) {
            waybillService.destroy(waybills);
        }
        //删除用户的地址
        List<Address> addresses = user.getAddresses();
        if (addresses != null) {
            userService.destroyAddress(addresses);
        }

        userService.destroy(user);

        redirectAttributes.addFlashAttribute("success", "删除成功！");
        return "redirect:/user/index";
    }
}

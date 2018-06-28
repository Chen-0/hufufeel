package me.rubick.transport.app.controller.admin;

import me.rubick.common.app.exception.FormException;
import me.rubick.common.app.helper.FormHelper;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.controller.AbstractController;
import me.rubick.transport.app.vo.admin.SettingVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController extends AbstractController {

    @RequestMapping("/admin/index")
    public String adminIndex() {
        return "redirect:/admin/product/index";
    }

    @RequestMapping(value = "/admin/setting", method = RequestMethod.GET)
    public String settingIndex(Model model) {
        String u2r = configService.findOneByKey("u2r");
        SettingVo settingVo = new SettingVo();
        settingVo.setU2r(u2r);
        if (ObjectUtils.isEmpty(model.asMap().get("fele"))) {
            model.addAttribute("fele", settingVo);
        }

        return "/admin/setting";
    }

    @RequestMapping(value = "/admin/setting", method = RequestMethod.POST)
    public String postSetting(
            @RequestParam String json,
            RedirectAttributes redirectAttributes
    ) {
        SettingVo settingVo = JSONMapper.fromJson(json, SettingVo.class);

        try {
            FormHelper formHelper = FormHelper.getInstance();
            formHelper.mustDecimal(settingVo.getU2r(), "u2r");
            formHelper.hasError();
        } catch (FormException e) {
            redirectAttributes.addFlashAttribute("fele", e.getForm());
            redirectAttributes.addFlashAttribute("ferror", e.getErrorField());
            return "redirect:/admin/setting";
        }

        configService.update("U2R", settingVo.getU2r());
        redirectAttributes.addFlashAttribute("SUCCESS", "成功更新系统设置！");
        return "redirect:/admin/setting";
    }
}

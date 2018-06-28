package me.rubick.transport.app.controller;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.service.ConfigService;
import me.rubick.transport.app.service.DocumentService;
import me.rubick.transport.app.service.MessageService;
import me.rubick.transport.app.service.UserService;
import me.rubick.transport.app.vo.AbstractVo;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
public abstract class AbstractController {

    @Resource
    protected UserService userService;

    @Resource
    protected MessageService messageService;

    @Resource
    protected DocumentService documentService;

    @Resource
    protected ConfigService configService;


    protected <T extends AbstractVo> void throwForm(
            RedirectAttributes redirectAttributes,
            Map<String, String> errorField,
            T errorForm) {

        log.info("=============== ERROR FORM ===============");
        log.info("ERROR ---- {}", JSONMapper.toJSON(errorField));
        log.info("ERROR ---- {}", JSONMapper.toJSON(errorForm.toMap()));
        log.info("==========================================");
        redirectAttributes.addFlashAttribute("fele", errorForm.toMap());
        redirectAttributes.addFlashAttribute("ferror", errorField);
    }
}

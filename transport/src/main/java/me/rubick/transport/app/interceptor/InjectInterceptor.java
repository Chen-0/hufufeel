package me.rubick.transport.app.interceptor;

import lombok.extern.slf4j.Slf4j;
import me.rubick.transport.app.model.Menu;
import me.rubick.transport.app.model.Message;
import me.rubick.transport.app.model.User;
import me.rubick.transport.app.service.MenuService;
import me.rubick.transport.app.service.MessageService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@Slf4j
public class InjectInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && !(auth instanceof AnonymousAuthenticationToken) && modelAndView != null) {
                User user = (User) auth.getPrincipal();
                modelAndView.getModelMap().addAttribute("USER", user);

                //注入信息
                injectMessage(user, modelAndView);
            }

            //注入菜单
            injectMenu(request, modelAndView);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Resource
    private MenuService menuService;

    private void injectMenu(HttpServletRequest request, ModelAndView modelAndView) {
        if (ObjectUtils.isEmpty(modelAndView)) {
            return;
        }

        Object o = modelAndView.getModelMap().get("MENU");

        if (! ObjectUtils.isEmpty(o) && o instanceof String) {
            String s = (String) o;

            List<Menu> menuList = menuService.getMenu(s);
            modelAndView.getModelMap().addAttribute("MENU", menuList);
        } else {
            List<Menu> menuList = menuService.getMenu("DEFAULT");
            modelAndView.getModelMap().addAttribute("MENU", menuList);
        }

        if (request.getRequestURI().startsWith("/admin/")) {
            modelAndView.getModelMap().addAttribute("AFLAG", true);
        } else {
            modelAndView.getModelMap().addAttribute("AFLAG", false);
        }
    }

    @Resource
    private MessageService messageService;

    private void injectMessage(User user, ModelAndView modelAndView) {
        int count = messageService.hasMessage(user);
        List<Message> messages = messageService.getLatestMessage(user);

        modelAndView.getModelMap().addAttribute("MSG_COUNT", count);
        modelAndView.getModelMap().addAttribute("MSG_ELEMENTS", messages);
    }
}
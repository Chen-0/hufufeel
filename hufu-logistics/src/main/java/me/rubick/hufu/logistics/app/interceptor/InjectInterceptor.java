package me.rubick.hufu.logistics.app.interceptor;

import me.rubick.hufu.logistics.app.model.Company;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import me.rubick.hufu.logistics.app.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class InjectInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && !(auth instanceof AnonymousAuthenticationToken) && modelAndView != null) {
                Company user = (Company) auth.getPrincipal();
                modelAndView.getModelMap().addAttribute("USER", user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

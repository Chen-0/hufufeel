package me.rubick.transport.app.interceptor;

import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {

            Map parameter = request.getParameterMap();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Object o = auth.getPrincipal();

            if (o != null && o instanceof User) {
                User company = (User)o;

                logger.info("The user  [{}] access url is: {}", company.getName(), request.getRequestURI());
                logger.info("The request parameter is: {}", JSONMapper.toJSON(parameter));
            } else {
                logger.info("The request url is: {}", request.getRequestURI());
                logger.info("The request parameter is: {}", JSONMapper.toJSON(parameter));
            }


        } catch (Throwable e) {
            logger.error("", e);
        }

        return super.preHandle(request, response, handler);
    }
}

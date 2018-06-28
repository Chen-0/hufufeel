package me.rubick.transport.app.interceptor;

import me.rubick.common.app.utils.JSONMapper;
import me.rubick.transport.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            Map parameter = request.getParameterMap();
            String ip = getIp(request);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!ObjectUtils.isEmpty(auth)) {
                Object o = auth.getPrincipal();
                if (o != null && o instanceof User) {
                    User company = (User) o;

                    logger.info("[{}]({}) ACCESS - {} - {}", ip, company.getName(), request.getRequestURI(), JSONMapper.toJSON(parameter));
                    return super.preHandle(request, response, handler);
                }
            }

            logger.info("[{}] ACCESS - {} - {}", ip, request.getRequestURI(), JSONMapper.toJSON(parameter));
        } catch (Throwable e) {
            logger.error("", e);
        }

        return super.preHandle(request, response, handler);
    }


    private String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}

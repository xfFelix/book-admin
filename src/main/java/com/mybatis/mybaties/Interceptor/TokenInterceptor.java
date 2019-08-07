package com.mybatis.mybaties.Interceptor;

import com.mybatis.mybaties.utils.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("Access-Token");
        if (null != token) {
            boolean result = TokenUtil.validate(token);
            if (result) {
                return true;
            }
            return false;
        }
        return false;
    }
}

package com.mybatis.mybaties.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CrosInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //添加跨域CORS
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
//        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Access-Token");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Expose-Headers", "*");
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
        }
        return true;
    }
}

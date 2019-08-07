package com.mybatis.mybaties;

import com.mybatis.mybaties.Interceptor.CrosInterceptor;
import com.mybatis.mybaties.Interceptor.TokenInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MybatiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatiesApplication.class, args);
    }

}

@SpringBootConfiguration
class MySpringMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List patterns = new ArrayList();
        patterns.add("/user/checkuser");
        patterns.add("/upload");
        patterns.add("/downfile");
        registry.addInterceptor(new CrosInterceptor());
        registry.addInterceptor(new TokenInterceptor()).excludePathPatterns(patterns);
    }
}

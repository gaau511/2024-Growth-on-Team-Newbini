package com.newbini.quizard.config;

import com.newbini.quizard.interceptor.LoginMemberInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginMemberInterceptor loginMemberInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginMemberInterceptor)
                .addPathPatterns("/", "/bookmark", "/parameter", "/result", "/upload"); // 특정 경로에만 인터셉터를 적용합니다.
    }
}


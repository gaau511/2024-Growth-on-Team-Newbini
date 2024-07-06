package com.newbini.newbeinquiz.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoginMemberInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // session이 있나 확인
        Object loginMember = null;
        if (request.getSession(false) != null) {
            loginMember = request.getSession(false).getAttribute("loginMember");
        }

        request.setAttribute("loginMember", loginMember);
        log.info("loginMember = {}", loginMember);

        return true; // 계속해서 다른 인터셉터나 컨트롤러로 요청을 전달합니다.
    }
}

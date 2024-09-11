package org.example.security_test.handler;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.security_test.service.LoginTryService;

import java.io.IOException;


@RequiredArgsConstructor
public class IpBlockFilter implements Filter {

    public final LoginTryService loginTryService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String ip = httpRequest.getRemoteAddr(); // Request Header 에 있는 client IP 취득
//        logger.info("")

        // Client IP 가 차단된 경우
        if(loginTryService.isBlocked(ip)){
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "IP 가 차단되었습니다.");  // Web 에 error message 전달
        }else{
            chain.doFilter(request, response);   // TODO 무슨 기능인지 검색해보기
        }
    }
}

package org.example.security_test.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.security_test.entity.UserEntity;
import org.example.security_test.repo.IpBlockRepo;
import org.example.security_test.repo.UserRepo;
import org.example.security_test.service.LoginTryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final LoginTryService loginTryService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HttpSession session = request.getSession();

        super.onAuthenticationFailure(request, response, exception);

        String errorMessage = "ID/PW를 확인하세요";  // 사용자에게 표시할 error message

        if(exception.getMessage().equalsIgnoreCase("User is disabled")){
            errorMessage = "계정이 비활성화되었습니다.";
        }else if(exception.getMessage().equalsIgnoreCase("invalid credentials")){
            errorMessage = "ID/PW 를 확인하세요";
        }
        session.setAttribute("error", errorMessage);  // session 에 error message 저장

        String ip = request.getRemoteAddr();  // 클라이언트의 IP 획득
        loginTryService.loginFailed(ip);      // DB Black List 에 차단할 IP 등록
    }
}



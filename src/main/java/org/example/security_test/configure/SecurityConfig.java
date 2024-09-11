package org.example.security_test.configure;

import lombok.RequiredArgsConstructor;
import org.example.security_test.entity.IpBlockEntity;
import org.example.security_test.handler.CustomLoginFailureHandler;
import org.example.security_test.handler.CustomLoginSuccessHandler;
import org.example.security_test.handler.IpBlockFilter;
import org.example.security_test.repo.IpBlockRepo;
import org.example.security_test.repo.UserRepo;
import org.example.security_test.service.LoginTryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private UserRepo userRepo;
    private IpBlockRepo ipBlockRepo;
    private final LoginTryService loginTryService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // method chaining 은 순서대로 작동하기 때문에 순서가 매우 중요함!
        http
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/", "/login", "/join", "/joinProc").permitAll() // 로그인하지 않은 사용자도 회원가입 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );

        // 로그인에 대한 method chaining
        http
                .formLogin
                        (auth -> auth
                                .successHandler(customLoginSuccessHandler())  // 로그인 success 핸들러
                                .failureHandler(customLoginFailureHandler())  // 로그인 failure 핸들러
                                .loginPage("/login")
                                .loginProcessingUrl("/loginProc")
                                .permitAll()
                );

        // 개발 단계에서 csrf 일단 비활성화
//        http
//                .csrf((auth-> auth.disable()));

        http
                .sessionManagement((auth-> auth
                        .maximumSessions(1)  // 1개의 ID 에 대해 다중 로그인 허용(최대 3개)
                        .maxSessionsPreventsLogin(true)));  // 다중 로그인 개수 초과 시 true : 새로운 로그인 차단, false : 기존 세션 1개 삭제해서 새로운 로그인

        // Session Fixation
        http
                .sessionManagement((auth -> auth
                        .sessionFixation()  //
                        .changeSessionId()));    // 로그인 시 동일한 Session 에 대한 id 변경

        // 로그인을 시도한 IP 의 account 가 block 인지 아닌지 check
        http
                .addFilterBefore(new IpBlockFilter(loginTryService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 로그인 suceess handler
    public CustomLoginSuccessHandler customLoginSuccessHandler(){
        return new CustomLoginSuccessHandler(userRepo, ipBlockRepo);
    }

    // 로그인 fail handler
    public CustomLoginFailureHandler customLoginFailureHandler(){
        return new CustomLoginFailureHandler(loginTryService);
    }
}

package org.example.security_test.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // PW BCrypt
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
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

        http
                .formLogin(auth -> auth
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
        return http.build();
    }

}

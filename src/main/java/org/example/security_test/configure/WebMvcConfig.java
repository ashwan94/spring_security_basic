package org.example.security_test.configure;

import lombok.RequiredArgsConstructor;
import org.example.security_test.cmmn.IpAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private IpAccessInterceptor ipAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ipAccessInterceptor)
                .addPathPatterns("/**");
    }
}

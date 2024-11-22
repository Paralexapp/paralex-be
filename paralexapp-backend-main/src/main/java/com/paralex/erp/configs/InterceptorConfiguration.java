package com.paralex.erp.configs;

import com.paralex.erp.interceptors.UserSessionTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    private final UserSessionTokenInterceptor userSessionTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userSessionTokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/logout","/api/v1/auth/validate-otp",
                        "/api/v1/auth/reset-password",
                        "/api/v1/auth/logout",
                        "/api/v1/auth/initiate-password-reset",
                        "/api/v1/auth/reset-password",
                        "/api/v1/auth/upload-to-cloudinary",
                        "/api/v1/auth/get-user",
                        "/api/v1/auth/get-user-by-id/{id}",
                        "/api/v1/auth/get-user-by-email/email",
                        "/api/v1/auth/get-registration-level");
    }
}
package com.prac.ktb.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations(System.getProperty("user.dir") + "/src/main/resources/static/attachments/profileImage"); // 실제 폴더 위치!
    }
}

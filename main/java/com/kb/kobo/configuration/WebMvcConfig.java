package com.kb.kobo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 허용할 출처
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true); // 자격 증명(쿠키 등) 허용
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 기본적인 리소스 핸들링 설정: src/main/resources/static/의 모든 파일을 서빙
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);

        // /public/** 경로로 요청된 파일을 src/main/resources/static/public/에서 제공
        registry.addResourceHandler("/public/**")
                .addResourceLocations("classpath:/static/public/")
                .setCachePeriod(3600);

        // /signup/** 경로로 요청된 파일을 src/main/resources/static/signup/에서 제공
        registry.addResourceHandler("/signup/**")
                .addResourceLocations("classpath:/static/signup/")
                .setCachePeriod(3600);

        // /login/** 경로로 요청된 파일을 src/main/resources/static/login/에서 제공
        registry.addResourceHandler("/login/**")
                .addResourceLocations("classpath:/static/login/")
                .setCachePeriod(3600);

    }
}

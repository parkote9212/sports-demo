package com.pgc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // application.yml의 cors.allowed-origins 값을 가져옵니다.
    // 값이 없으면 기본적으로 로컬호스트들을 허용합니다.
    @Value("${cors.allowed-origins:http://localhost:3000,http://localhost:5173,http://localhost:5174}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 변수로 받아온 허용 출처들을 적용
                //.allowedOrigins(allowedOrigins) 
				  .allowedOrigins("*")
				  .allowedMethods("*");
                //.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                //.allowCredentials(true);
    }
}
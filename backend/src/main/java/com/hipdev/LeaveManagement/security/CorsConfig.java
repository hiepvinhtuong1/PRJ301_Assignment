package com.hipdev.LeaveManagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Đánh dấu đây là một class cấu hình trong Spring Boot
public class CorsConfig {

    @Bean // Định nghĩa một Bean để Spring quản lý
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() { // Tạo một instance của WebMvcConfigurer để cấu hình CORS
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép CORS trên tất cả các API endpoints
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Chỉ định các phương thức HTTP được phép
                        .allowedOrigins("*"); // Cho phép truy cập từ bất kỳ domain nào (có thể thay "*" bằng danh sách domain cụ thể để bảo mật hơn)
            }
        };
    }
}

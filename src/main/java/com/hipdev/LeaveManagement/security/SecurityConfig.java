package com.hipdev.LeaveManagement.security;

import com.hipdev.LeaveManagement.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Đánh dấu đây là class cấu hình
@EnableWebSecurity // Kích hoạt bảo mật web trong Spring Security
@EnableMethodSecurity // Kích hoạt bảo mật dựa trên annotation (VD: @PreAuthorize)
public class SecurityConfig {

    @Autowired
    JWTAuthFilter jwtAuthFilter; // Inject bộ lọc JWT (Xử lý token)

    @Autowired
    CustomUserDetailService customUserDetailService; // Inject service để lấy thông tin user

    /**
     * Cấu hình bảo mật chính (Security Filter Chain)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Tắt CSRF (Không cần thiết khi dùng JWT)
                .cors(Customizer.withDefaults()) // Cho phép CORS với cấu hình mặc định
                .authorizeRequests(request -> request
                        .requestMatchers("/auth/**", "/deparment", "leave-request").permitAll() // Các URL này không cần xác thực
                        .anyRequest().authenticated()) // Mọi request khác cần xác thực
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không sử dụng session (Dùng JWT thay thế)
                .authenticationProvider(authenticationProvider()) // Đăng ký provider xác thực
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Thêm filter JWT trước UsernamePasswordAuthenticationFilter
        return http.build();
    }

    /**
     * Cấu hình AuthenticationProvider để xác thực user từ database
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService); // Đặt service lấy user từ database
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); // Đặt cơ chế mã hóa mật khẩu (BCrypt)
        return daoAuthenticationProvider;
    }

    /**
     * Cấu hình PasswordEncoder để mã hóa mật khẩu người dùng
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Sử dụng BCrypt để mã hóa mật khẩu
    }

    /**
     * Cấu hình AuthenticationManager để quản lý xác thực user
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

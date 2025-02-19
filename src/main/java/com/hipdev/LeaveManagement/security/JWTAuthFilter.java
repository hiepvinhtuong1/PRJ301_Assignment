package com.hipdev.LeaveManagement.security;

import com.hipdev.LeaveManagement.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Đánh dấu đây là một Spring Bean để Spring Boot quản lý
public class JWTAuthFilter extends OncePerRequestFilter { // Kế thừa OncePerRequestFilter để xử lý request một lần duy nhất

    @Autowired
    private JWTUtils jwtUtils; // Inject class JWTUtils để xử lý JWT

    private CachingUserDetailsService cachingUserDetailsService; // Lấy thông tin user từ cache

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization"); // Lấy token từ request header
        final String jwtToken;
        final String username;

        // Nếu không có header Authorization hoặc nó rỗng -> bỏ qua và tiếp tục request
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Cắt bỏ phần "Bearer " để lấy token thực sự
        jwtToken = authorizationHeader.substring(7);
        username = jwtUtils.extractUsername(jwtToken); // Trích xuất username từ JWT

        // Kiểm tra nếu username có trong token và chưa có authentication nào trong SecurityContextHolder
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = cachingUserDetailsService.loadUserByUsername(username); // Load user từ database/cache
            if (jwtUtils.isValidToken(jwtToken, userDetails)) { // Kiểm tra xem token có hợp lệ không
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // Tạo SecurityContext mới
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()); // Tạo authentication token từ userDetails
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Thêm thông tin request vào token
                securityContext.setAuthentication(token); // Gán authentication vào SecurityContext
                SecurityContextHolder.setContext(securityContext); // Cập nhật SecurityContextHolder
            }
        }
        filterChain.doFilter(request, response); // Tiếp tục chuỗi filter
    }
}

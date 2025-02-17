package com.hipdev.LeaveManagement.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service // Đánh dấu đây là một Spring Service Component
public class JWTUtils {

    private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 7; // Thời gian hết hạn của token: 7 ngày

    private final SecretKey key; // Khóa bí mật dùng để ký và xác thực JWT

    public JWTUtils() {
        // Chuỗi bí mật để mã hóa JWT
        String secretString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";

        // Chuyển chuỗi bí mật thành dạng byte
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));

        // Tạo SecretKey dùng thuật toán HmacSHA256
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    // Tạo JWT từ thông tin UserDetails
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // Thêm username vào token
                .issuedAt(new Date(System.currentTimeMillis())) // Thời điểm phát hành token
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Thiết lập thời gian hết hạn
                .signWith(key) // Ký token với khóa bí mật
                .compact(); // Tạo chuỗi JWT hoàn chỉnh
    }

    // Trích xuất username từ token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Trích xuất thông tin cụ thể từ JWT bằng cách sử dụng lambda function
    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(
                Jwts.parser() // Tạo parser để đọc token
                        .verifyWith(key) // Xác minh token bằng khóa bí mật
                        .build()
                        .parseSignedClaims(token) // Phân tích token
                        .getPayload() // Lấy dữ liệu trong token
        );
    }

    // Kiểm tra xem token có hợp lệ không (có đúng username và chưa hết hạn không)
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Lấy username từ token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Kiểm tra username và hạn sử dụng
    }

    // Kiểm tra xem token có hết hạn không
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date()); // So sánh thời gian hết hạn với hiện tại
    }
}

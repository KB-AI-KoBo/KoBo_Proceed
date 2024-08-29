package com.kb.kobo.controller;

import com.kb.kobo.dto.UserLoginDto;
import com.kb.kobo.service.UserService;
import com.kb.kobo.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        // 사용자 인증 처리
        String username = userLoginDto.getEmail();
        String token = jwtUtil.generateToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("refreshToken", refreshToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 세션 무효화
        request.getSession().invalidate();

        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // 로그아웃 성공 후 응답
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestParam String refreshToken) {
        try {
            String username = jwtUtil.extractClaims(refreshToken).getSubject();
            if (username != null && !jwtUtil.isTokenExpired(refreshToken)) {
                String newAccessToken = jwtUtil.generateToken(username);
                String newRefreshToken = jwtUtil.generateRefreshToken(username);

                Map<String, String> response = new HashMap<>();
                response.put("token", newAccessToken);
                response.put("refreshToken", newRefreshToken);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired refresh token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred"));
        }
    }

    @GetMapping("/protected")
    @ResponseBody
    public ResponseEntity<String> getProtectedResource() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            return ResponseEntity.ok("보호된 리소스에 접근 성공 - " + username);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근 권한이 없습니다.");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteAccount(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증이 필요합니다.");
        }
        String username = authentication.getName();

        try {
            userService.deleteUserByUsername(username);

            // 로그아웃 후 세션 무효화 및 쿠키 삭제
            request.getSession().invalidate();

            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            return ResponseEntity.ok("계정이 성공적으로 삭제되었습니다.");
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

    // Spring Security에서 발생하는 AccessDeniedException 및 AuthenticationException 예외를 처리
    @ExceptionHandler({AccessDeniedException.class, UsernameNotFoundException.class})
    public ResponseEntity<String> handleSecurityException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없거나 인증 오류가 발생했습니다.");
    }

    // 기타 예외를 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
    }
}

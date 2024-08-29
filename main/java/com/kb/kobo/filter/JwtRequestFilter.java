package com.kb.kobo.filter;

import com.kb.kobo.authentication.UserEmailPasswordAuthenticationToken;
import com.kb.kobo.service.CustomUserDetailsService;
import com.kb.kobo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtRequestFilter implements javax.servlet.Filter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtRequestFilter(CustomUserDetailsService customUserDetailsService, @Lazy JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 코드 (필요시 구현)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 요청 URI 확인
        String requestURI = httpRequest.getRequestURI();
        System.out.println("Request URI: " + requestURI);


        // /chat 엔드포인트를 제외하는 로직 추가
        if ("/chat".equals(requestURI)) {
            chain.doFilter(request, response);
            System.out.println("/chat 경로는 JWT 인증 없이 통과");
            return;
        }

        // /error 엔드포인트를 제외하는 로직 추가
        if ("/error".equals(requestURI)) {
            chain.doFilter(request, response);
            System.out.println("/error 경로는 JWT 인증 없이 통과");
            return;
        }

        final String authorizationHeader = httpRequest.getHeader("Authorization");
        System.out.println("Authorization Header: " + authorizationHeader);

        String email = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7); // "Bearer " 제거
            System.out.println("Extracting token: " + jwtToken);

            try {
                email = jwtUtil.extractEmail(jwtToken); // 사용자 이메일 추출
                System.out.println("Extracted Email: " + email);
            } catch (Exception e) {
                System.err.println("Token extraction failed: " + e.getMessage());
                chain.doFilter(request, response);
                return;
            }
        } else {
            System.out.println("Authorization header is either null or does not start with Bearer.");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                System.out.println("Loaded UserDetails for email: " + email);

                if (jwtUtil.validateToken(jwtToken, email)) {
                    System.out.println("JWT token is valid.");
                    UserEmailPasswordAuthenticationToken authenticationToken =
                            new UserEmailPasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    System.out.println("JWT token is not valid.");
                }
            } catch (Exception e) {
                System.err.println("UserDetails loading or token validation failed: " + e.getMessage());
            }
        }

        chain.doFilter(request, response); // 다음 필터 체인으로 요청 전달
    }


    @Override
    public void destroy() {
        // 필터 종료 코드 (필요시 구현)
    }
}

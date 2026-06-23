package com.nrs1209.travelapp.global.security.filter;

import com.nrs1209.travelapp.global.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에서 authorization 헤더 추출
        String authorization = request.getHeader("Authorization");

        // authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        // 토큰 유효성 및 만료 시간 검증
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 username 추출
        String username = jwtUtil.getUsernameFromToken(token);

        // 스프링 시큐리티 기본 user 빌더 이용해 userdetails 생성
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password("temp")
                .authorities("ROLE_USER")
                .build();

        // 스프링 시큐리티 인증 토큰 생성 및 Context 세션에 넣기
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 필터로 진행
        filterChain.doFilter(request, response);
    }
}

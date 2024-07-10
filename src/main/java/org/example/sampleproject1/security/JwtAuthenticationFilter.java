package org.example.sampleproject1.security;

import org.example.sampleproject1.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;


    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    // 필터 메인 로직
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 요청 헤더에서 Authorization 헤더 가져옴
        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Authorization 헤더가 Bearer 로 시작하는지 확인
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7); // "Bearer " 문자열 이후의 토큰 부분을 추출
            username = jwtTokenUtil.getUsernameFromToken(token); // 토큰에서 사용자 이름을 추출
        }

        // 사용자 이름이 존재하고, 현재 SecurityContext에 인증 정보가 없는 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 사용자 상세 정보를 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 토큰이 유효한지 검증
            if (jwtTokenUtil.validateToken(token)) {
                // 유효한 토큰인 경우, 인증 객체를 생성하여 SecurityContext에 설정
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터를 실행
        chain.doFilter(request, response);
    }
}

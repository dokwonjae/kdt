package org.example.sampleproject1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    // application.properties 또는 application.yml 파일에서 설정된 값을 주입받음
    @Value("${jwt.secret}")
    private String secret; // JWT 서명에 사용될 비밀 키

    @Value("${jwt.expiration}")
    private Long expiration; // JWT 토큰의 만료 시간 (초 단위)

    // 사용자 이름을 기반으로 JWT 토큰을 생성하는 메서드
    public String generateToken(String username) {
        Date now = new Date(); // 현재 시간
        Date expiryDate = new Date(now.getTime() + expiration * 1000); // 만료 시간 계산

        // JWT 토큰 생성
        return Jwts.builder()
                .setSubject(username) // 토큰의 주제를 사용자 이름으로 설정
                .setIssuedAt(now) // 토큰 발행 시간 설정
                .setExpiration(expiryDate) // 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, secret) // 서명 알고리즘과 비밀 키를 사용하여 서명
                .compact(); // JWT 토큰을 문자열로 직렬화
    }

    // JWT 토큰에서 사용자 이름을 추출하는 메서드
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret) // 서명 검증을 위해 비밀 키를 설정
                .parseClaimsJws(token) // 토큰을 파싱하고 클레임을 가져옴
                .getBody();

        // 여기서 토큰을 파싱하고 클레임 가져온다는거는 JWT가 헤더, 페이로드, 시그니처 세 부분으로 나눠져있고
        // 페이로드에 클레임이 포함되어 있음 클레임은 토큰에 담긴 정보 의미( 식별자, 발행자, 만료시간 등)
        // 토큰을 파싱하고 클레임 가져온다는 말은 JWT 문자 해ㅊ독해서 그 안의 데이터 추출한다는 의미임 확인?

        return claims.getSubject(); // 클레임에서 주제(사용자 이름)를 반환.
    }

    // JWT 토큰의 유효성을 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            // 토큰을 파싱하여 서명 검증이 성공하면 유효한 토큰으로 간주
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 서명 검증이 실패하거나 다른 예외가 발생하면 유효하지 않은 토큰으로 간주
            return false;
        }
    }
}

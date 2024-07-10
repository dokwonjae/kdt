package org.example.sampleproject1.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    // JPA 엔티티로 db랑 매핑되어있음
    // security가 userdetails에서 사용자 정보 가져와서 인증, 권한부여 처리함

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한이 없는 사용자로 설정
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 체크 (계정 만료 날짜)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠겨있는지 체크
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 체크 ( 비밀번호 만료 날짜)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 체크
    }
}

// 굳이 쓰자면 이런식으로 상태 설정 가능함
// User user = new User();
//user.setUsername("john_doe");
//user.setPassword("securepassword");
//user.setRole("USER");
//user.setAccountNonExpired(true);
//user.setAccountNonLocked(true);
//user.setCredentialsNonExpired(true);
//user.setEnabled(true);
package org.example.sampleproject1.service;

import org.example.sampleproject1.entity.User;
import org.example.sampleproject1.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 이름으로 사용자를 조회하고, UserDetails 객체를 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // UserRepository 써서 사용자 이름으로 사용자 조회
        User user = userRepository.findByUsername(username);

        // 사용자 없으면 UsernameNotFoundException 발생
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // 사용자가 있으면 User 객체 반환 User 클래스는 UserDetails 구현
        return user;
    }
}

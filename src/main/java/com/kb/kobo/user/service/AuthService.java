package com.kb.kobo.user.service;

import com.kb.kobo.user.domain.User;
import com.kb.kobo.user.dto.TokenDto;
import com.kb.kobo.user.dto.UserLoginReqDto;
import com.kb.kobo.user.repository.UserRepository;
import com.kb.kobo.user.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenDto login (UserLoginReqDto userLoginReqDto) {
        User user = userRepository.findByEmail(userLoginReqDto.getEmail())
                .orElseThrow(() -> new RuntimeException("회원가입하지 않은 사용자입니다."));

        if (!passwordEncoder.matches(userLoginReqDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return TokenDto.builder()
                .access_token(tokenProvider.createAccessToken(user))
                .build();
    }
}

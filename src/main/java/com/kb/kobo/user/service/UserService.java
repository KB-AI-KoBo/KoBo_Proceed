package com.kb.kobo.user.service;

import com.kb.kobo.user.domain.User;
import com.kb.kobo.user.dto.UpdatedUserDto;
import com.kb.kobo.user.dto.UserInfoDto;
import com.kb.kobo.user.dto.UserSignupReqDto;
import com.kb.kobo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserInfoDto signUp(UserSignupReqDto userSignUpRequest) {
        if (userRepository.findByEmail(userSignUpRequest.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .id(userSignUpRequest.getId())
                .username(userSignUpRequest.getUsername())
                .password(passwordEncoder.encode(userSignUpRequest.getPassword()))
                .email(userSignUpRequest.getEmail())
                .companyName(userSignUpRequest.getCompanyName())
                .companySize(userSignUpRequest.getCompanySize())
                .registrationNumber(userSignUpRequest.getRegistrationNumber())
                .companyEmail(userSignUpRequest.getCompanyEmail())
                .industry(userSignUpRequest.getIndustry())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return UserInfoDto.from(user);
    }

    @Transactional
    public UserInfoDto updateUser(Principal principal, UpdatedUserDto updatedUserDto) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        if (updatedUserDto.getUsername() != null && !updatedUserDto.getUsername().isEmpty()) {
            user.setUsername(updatedUserDto.getUsername());
        }

        if (updatedUserDto.getPassword() != null && !updatedUserDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        }

        if (updatedUserDto.getEmail() != null && !updatedUserDto.getEmail().isEmpty()) {
            user.setEmail(updatedUserDto.getEmail());
        }

        if (updatedUserDto.getCompanyName() != null && !updatedUserDto.getCompanyName().isEmpty()) {
            user.setCompanyName(updatedUserDto.getCompanyName());
        }

        if (updatedUserDto.getCompanySize() != null && !updatedUserDto.getCompanySize().isEmpty()) {
            user.setCompanySize(updatedUserDto.getCompanySize());
        }

        if (updatedUserDto.getRegistrationNumber() != null && !updatedUserDto.getRegistrationNumber().isEmpty()) {
            user.setRegistrationNumber(updatedUserDto.getRegistrationNumber());
        }

        if (updatedUserDto.getCompanyEmail() != null && !updatedUserDto.getCompanyEmail().isEmpty()) {
            user.setCompanyEmail(updatedUserDto.getCompanyEmail());
        }

        if (updatedUserDto.getIndustry() != null && !updatedUserDto.getIndustry().isEmpty()) {
            user.setIndustry(updatedUserDto.getIndustry());
        }

        userRepository.save(user);

        return UserInfoDto.from(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
    }

    public boolean emailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public String getTmpPassword() {
        char[] charSet = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String newPassword = "";

        for (int i = 0; i < 10; i++) {
            int idx = (int) (charSet.length * Math.random());
            newPassword += charSet[idx];
        }

        return newPassword;
    }

    @Transactional
    public void updatePassword(String tmpPassword, String email) {
        String encryptedPassword = passwordEncoder.encode(tmpPassword);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        user.setPassword(encryptedPassword);
    }
}

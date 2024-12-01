package com.kb.kobo.user.controller;

import com.kb.kobo.user.domain.User;
import com.kb.kobo.user.dto.UpdatedUserDto;
import com.kb.kobo.user.dto.UserInfoDto;
import com.kb.kobo.user.dto.UserSignupReqDto;
import com.kb.kobo.user.repository.UserRepository;
import com.kb.kobo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserInfoDto> signup(@Valid @RequestBody UserSignupReqDto userSignupReqDto) {
        UserInfoDto savedUser = userService.signUp(userSignupReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Principal principal) {

        System.out.println(principal.getName());

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("회원가입하지 않은 사용자입니다."));

        return ResponseEntity.ok(user);

    }

    @PostMapping("/profile")
    public ResponseEntity<UserInfoDto> updateUser(Principal principal, @RequestBody UpdatedUserDto updatedUserDto) {
        UserInfoDto updatedUserInfo = userService.updateUser(principal, updatedUserDto);
        return ResponseEntity.ok(updatedUserInfo);
    }

    @GetMapping("/profile/username/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile/email/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}

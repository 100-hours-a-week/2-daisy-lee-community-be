package com.prac.ktb.auth.service;

import com.prac.ktb.auth.config.JwtProvider;
import com.prac.ktb.auth.dto.LoginResponseDto;
import com.prac.ktb.common.exception.CustomException;
import com.prac.ktb.user.entity.User;
import com.prac.ktb.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDto login(String email, String password) {
        User loginUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("user_login_fail", HttpStatus.NOT_FOUND));

        if(!passwordEncoder.matches(password, loginUser.getPassword())) {
            throw new CustomException("user_login_fail", HttpStatus.UNAUTHORIZED);
        }

        String jwt = jwtProvider.generateToken(loginUser.getId());

        return new LoginResponseDto(loginUser.getId(), jwt);
    }


}

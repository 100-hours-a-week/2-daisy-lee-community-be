package com.prac.ktb.auth.service;

import com.prac.ktb.auth.config.JwtProvider;
import com.prac.ktb.auth.dto.LoginRequestDto;
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

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User loginUser = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new CustomException("user_login_fail", HttpStatus.NOT_FOUND));

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), loginUser.getPassword())) { // 입력한 password, DB에 저장된 암호화된 password
            throw new CustomException("user_login_fail", HttpStatus.UNAUTHORIZED);
        }

        String jwt = jwtProvider.generateToken(loginUser.getEmail());

        return new LoginResponseDto(loginUser.getId(), jwt);
    }


}

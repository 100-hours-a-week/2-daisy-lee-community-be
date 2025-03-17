package com.prac.ktb.auth.controller;

import com.prac.ktb.auth.dto.LoginResponseDto;
import com.prac.ktb.auth.service.AuthService;
import com.prac.ktb.common.dto.ApiResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/sessions")
@Transactional
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(@RequestBody String email, String password) {
        LoginResponseDto loginResponseDto = authService.login(email, password);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("user_login_success", loginResponseDto));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> logout(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("user_logout_success", null));
    }
}

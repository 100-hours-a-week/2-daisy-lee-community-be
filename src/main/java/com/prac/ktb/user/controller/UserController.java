package com.prac.ktb.user.controller;

import com.prac.ktb.auth.config.JwtProvider;
import com.prac.ktb.common.dto.ApiResponseDto;
import com.prac.ktb.user.dto.UserRequestDto;
import com.prac.ktb.user.entity.User;
import com.prac.ktb.common.exception.CustomException;
import com.prac.ktb.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@Transactional
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    // 회원가입
    @PostMapping
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> createUser(@RequestBody UserRequestDto userReqDto) {
        User newUser = userService.createUser(userReqDto);

        Map<String, Object> responseData = Map.of("userId", newUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("user_register_success", responseData));

    }

    // 사용자 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getUserInfo(@PathVariable Long userId,
                                                                           @AuthenticationPrincipal UserDetails userDetails) {
        String tokenEmail = userDetails.getUsername();
        User selectUser = userService.getUserInfoById(userId);

        if(!tokenEmail.equals(selectUser.getEmail())) {
            throw new CustomException("user_forbidden", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(new ApiResponseDto<>("user_info_success", selectUser));
    }
}

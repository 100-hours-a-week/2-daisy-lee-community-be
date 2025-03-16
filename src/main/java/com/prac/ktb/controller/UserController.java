package com.prac.ktb.controller;

import com.prac.ktb.auth.JwtService;
import com.prac.ktb.dto.ApiResponseDto;
import com.prac.ktb.dto.UserRequestDto;
import com.prac.ktb.entity.User;
import com.prac.ktb.exception.CustomException;
import com.prac.ktb.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController("/users")
@Transactional
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
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
                                                                           @RequestHeader("Authorization") String accessToken) {
        if(accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new CustomException("user_unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String jwt = accessToken.substring(7);
        Long tokenUserId = jwtService.validateAndExtractUserId(jwt);

        if(!tokenUserId.equals(userId)) {
            throw new CustomException("user_forbidden", HttpStatus.FORBIDDEN);
        }

        User selectUser = userService.getUserInfo(userId);
        return ResponseEntity.ok(new ApiResponseDto<>("user_info_success", selectUser));
    }
}

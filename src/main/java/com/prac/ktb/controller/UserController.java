package com.prac.ktb.controller;

import com.prac.ktb.dto.ApiResponseDto;
import com.prac.ktb.dto.UserRequestDto;
import com.prac.ktb.entity.User;
import com.prac.ktb.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/users")
@Transactional
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> createUser(@RequestBody UserRequestDto userReqDto) {
        User newUser = userService.createUser(userReqDto);

        Map<String, Object> responseData = Map.of("userId", newUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("user_register_success", responseData));

    }
}

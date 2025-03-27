package com.prac.ktb.user.controller;

import com.prac.ktb.auth.config.JwtProvider;
import com.prac.ktb.common.dto.ApiResponseDto;
import com.prac.ktb.user.dto.UserRequestDto;
import com.prac.ktb.user.dto.UserResponseDto;
import com.prac.ktb.user.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/users")
@Transactional
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
    }

    /**
     * [회원가입]
     * @param userReqDto
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<UserResponseDto>> createUser(@ModelAttribute UserRequestDto userReqDto) {
        UserResponseDto newUserResDto = userService.createUser(userReqDto);

        Map<String, Object> responseData = Map.of("userEmail", newUserResDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("user_register_success", responseData));

    }

    /**
     * [회원정보 조회]
     * @param userId
     * @param userDetails
     * @return ResponseEntity
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserInfo(@PathVariable Long userId,
                                                                       @AuthenticationPrincipal UserDetails userDetails) {

        UserResponseDto selectUserDto = userService.getUserInfoById(userId);
        //userService.validateUserAccess(selectUserDto.getEmail(), userDetails);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("user_info_success", selectUserDto));

    }

    /**
     * [회원정보 수정]
     * @param userId
     * @param userReqDto
     * @param userDetails
     * @return ResponseEntity
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUserInfo(@PathVariable Long userId,
                                                                          @ModelAttribute UserRequestDto userReqDto,
                                                                          @AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDto userResDto = userService.updateUser(userId, userDetails, userReqDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponseDto<>("user_update_success", userResDto));

    }

    /**
     * [비밀번호 수정]
     * @param userId
     * @param userReqDto
     * @param userDetails
     * @return ResponseEntity
     */
    @PatchMapping("/{userId}/password")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUserPassword(@PathVariable Long userId,
                                                                                  @RequestBody UserRequestDto userReqDto,
                                                                                  @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUserPassword(userId, userDetails, userReqDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponseDto<>("user_update_password_success", null));

    }

    /**
     * [회원 탈퇴]
     * @param userId
     * @param userDetails
     * @return ResponseEntity
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> deleteUser(@PathVariable Long userId,
                                                                          @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> responseData = userService.deleteUser(userId, userDetails);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("user_delete_success", responseData));

    }

    /**
     * [이메일 중복 체크]
     * @param email
     * @return ResponseEntity
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDto<Map<String, Boolean>>> checkEmail(@RequestParam String email) {
        boolean isDuplicated = userService.isEmailDuplicated(email);
        return ResponseEntity.ok(new ApiResponseDto<>("check_email_success", Map.of("duplicated", isDuplicated)));
    }

    /**
     * [닉네임 중복 체크]
     * @param nickname
     * @return ResponseEntity
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponseDto<Map<String, Boolean>>> checkNickname(@RequestParam String nickname) {
        boolean isDuplicated = userService.isNicknameDuplicated(nickname);
        return ResponseEntity.ok(new ApiResponseDto<>("check_nickname_success", Map.of("duplicated", isDuplicated)));
    }
}
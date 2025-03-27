package com.prac.ktb.user.service;

import com.prac.ktb.user.dto.UserRequestDto;
import com.prac.ktb.user.dto.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserInfoById(Long id);
    UserResponseDto updateUser(Long userId, UserDetails userDetails, UserRequestDto userRequestDto);
    Map<String, Object> deleteUser(Long userId, UserDetails userDetails);
    void updateUserPassword(Long userId, UserDetails userDetails, UserRequestDto userRequestDto);
    void validateUserAccess(String email, UserDetails userDetails);
    boolean isEmailDuplicated(String email);
    boolean isNicknameDuplicated(String nickname);

}

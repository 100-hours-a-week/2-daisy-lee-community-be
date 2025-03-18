package com.prac.ktb.user.service;

import com.prac.ktb.user.dto.UserRequestDto;
import com.prac.ktb.user.dto.UserResponseDto;
import com.prac.ktb.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserInfoById(Long id);
    UserResponseDto updateUser(Long userId, UserDetails userDetails, UserRequestDto userRequestDto);
    void validateUserAccess(String email, UserDetails userDetails);

}

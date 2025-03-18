package com.prac.ktb.user.service;

import com.prac.ktb.user.dto.UserRequestDto;
import com.prac.ktb.user.entity.User;

public interface UserService {
    User createUser(UserRequestDto userRequestDto);
    User getUserInfoById(Long id);
}

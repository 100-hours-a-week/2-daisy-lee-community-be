package com.prac.ktb.service;

import com.prac.ktb.dto.UserRequestDto;
import com.prac.ktb.entity.User;

public interface UserService {
    User createUser(UserRequestDto userRequestDto);
}

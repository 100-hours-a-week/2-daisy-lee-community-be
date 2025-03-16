package com.prac.ktb.service;

import com.prac.ktb.dto.UserRequestDto;
import com.prac.ktb.entity.User;
import com.prac.ktb.exception.CustomException;
import com.prac.ktb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User createUser(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new CustomException("user_email_conflict", HttpStatus.CONFLICT);
        }

        User newUser = User.builder()
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .nickname(userRequestDto.getNickname())
                .profileImagePath(userRequestDto.getProfileImage())
                .build();

        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public User getUserInfo(Long id) {
        User selectUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        return User.builder()
                .id(selectUser.getId())
                .email(selectUser.getEmail())
                .nickname(selectUser.getNickname())
                .profileImagePath(selectUser.getProfileImagePath())
                .build();
    }
}

package com.prac.ktb.user.service;

import com.prac.ktb.user.dto.UserRequestDto;
import com.prac.ktb.user.dto.UserResponseDto;
import com.prac.ktb.user.entity.User;
import com.prac.ktb.common.exception.CustomException;
import com.prac.ktb.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private UserResponseDto userResDto;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new CustomException("user_email_conflict", HttpStatus.CONFLICT);
        }

        userResDto = UserResponseDto.builder()
                .id(userRequestDto.getId())
                .email(userRequestDto.getEmail())
                .nickname(userRequestDto.getNickname())
                .profileImagePath(userRequestDto.getProfileImagePath())
                .build();

        userRepository.save(userResDto);
        return userResDto;
    }

    @Override
    public UserResponseDto getUserInfoById(Long userId) {
        User selectUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        return UserResponseDto.builder()
                .id(selectUser.getId())
                .email(selectUser.getEmail())
                .nickname(selectUser.getNickname())
                .profileImagePath(selectUser.getProfileImagePath())
                .build();
        }

    @Override
    public UserResponseDto updateUser(Long userId, UserDetails userDetails, UserRequestDto userReqDto) {
        User updateUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        String email = updateUser.getEmail();
        validateUserAccess(email, userDetails);

        if(userReqDto.getNickname() != null && !userReqDto.getNickname().isEmpty())
            updateUser.setNickname(userReqDto.getNickname());

        if(userReqDto.getProfileImagePath() != null && !userReqDto.getProfileImagePath().isEmpty())
            updateUser.setProfileImagePath(userReqDto.getProfileImagePath());

        // @Transactional 사용할 경우 save() 불필요, 변경 감지
        userResDto = UserResponseDto.builder()
                .nickname(updateUser.getNickname())
                .profileImagePath(updateUser.getProfileImagePath())
                .build();

        return userResDto;
    }

    // 토큰 email과 현 접속자 email 비교
    @Override
    public void validateUserAccess(String userDtoEmail, UserDetails userDetails) {
        String tokenEmail = userDetails.getUsername();

        if(!tokenEmail.equals(userDtoEmail)) {
            throw new CustomException("user_forbidden", HttpStatus.FORBIDDEN);
        }
    }
}

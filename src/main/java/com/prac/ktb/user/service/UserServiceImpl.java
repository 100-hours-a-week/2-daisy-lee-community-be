package com.prac.ktb.user.service;

import com.prac.ktb.common.config.CommonProperties;
import com.prac.ktb.common.service.FileService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommonProperties commonProperties;
    private final FileService fileService;
    private UserResponseDto userResDto;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new CustomException("user_email_conflict", HttpStatus.CONFLICT);
        }

        User newUser = new User();
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        newUser.setNickname(userRequestDto.getNickname());

        MultipartFile userProfileImage = userRequestDto.getProfileImagePath();
        if (userProfileImage != null && !userProfileImage.isEmpty()) {
            String imageUrl = fileService.saveProfileImage(userProfileImage); // 이미지 저장 후 URL 반환
            newUser.setProfileImagePath(imageUrl);
        }

        userRepository.save(newUser);

        return UserResponseDto.builder()
                .email(userRequestDto.getEmail())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .nickname(userRequestDto.getNickname())
                .build();
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

        updateUser.setNickname(userReqDto.getNickname());

        MultipartFile profileImage = userReqDto.getProfileImagePath();
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = fileService.saveProfileImage(profileImage); // 이미지 저장 후 URL 반환
            updateUser.setProfileImagePath(imageUrl);
        }

        // @Transactional 사용할 경우 save() 불필요, 변경 감지
        userResDto = UserResponseDto.builder()
                .nickname(updateUser.getNickname())
                .profileImagePath(updateUser.getProfileImagePath())
                .build();

        return userResDto;
    }

    @Override
    public Map<String, Object> deleteUser(Long userId, UserDetails userDetails) {
        User deleteUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        if (deleteUser.isDeleted()) {
            throw new CustomException("user_already_deleted", HttpStatus.BAD_REQUEST);
        }
        deleteUser.delete();
        userRepository.save(deleteUser);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("redirectURL", commonProperties.getLogoutURL());

        return responseMap;
    }

    @Override
    public void updateUserPassword(Long userId, UserDetails userDetails, UserRequestDto userReqDto) {
        User updateUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        String email = updateUser.getEmail();
        validateUserAccess(email, userDetails);

        updateUser.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
    }

    // 토큰 email과 현 접속자 email 비교
    @Override
    public void validateUserAccess(String userDtoEmail, UserDetails userDetails) {
        String tokenEmail = userDetails.getUsername();

        if (!tokenEmail.equals(userDtoEmail)) {
            throw new CustomException("user_forbidden", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public boolean isEmailDuplicated(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isNicknameDuplicated(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}

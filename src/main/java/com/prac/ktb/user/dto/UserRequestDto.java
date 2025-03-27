package com.prac.ktb.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    //private Long id;
    private String password;
    private String email;
    private String nickname;
    private MultipartFile profileImagePath;
    //private Date deletedAt;
}

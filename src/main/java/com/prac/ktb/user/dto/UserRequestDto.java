package com.prac.ktb.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private Long id;
    private String password;
    private String email;
    private String nickname;
    private String profileImagePath;
    private Date droppedAt;
}

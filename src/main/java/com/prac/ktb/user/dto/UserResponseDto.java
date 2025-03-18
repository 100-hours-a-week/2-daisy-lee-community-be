package com.prac.ktb.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private Long id;
    private String password;
    private String email;
    private String nickname;
    private String profileImagePath;
    private Date droppedAt;

    @Builder
    public UserResponseDto(Long id, String email, String password, String nickname, String profileImagePath, Date droppedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImagePath = profileImagePath;
        this.droppedAt = droppedAt;
    }
}

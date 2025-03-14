package com.prac.ktb.dto;

import lombok.Getter;

@Getter
public class ApiResponseDto {
    private String message;
    private Object data;

    public ApiResponseDto(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}

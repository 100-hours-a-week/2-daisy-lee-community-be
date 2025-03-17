package com.prac.ktb.common.exception;

import com.prac.ktb.common.dto.ApiResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final ApiResponseDto<?> response;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.response = new ApiResponseDto<>(message, null);
    }
}

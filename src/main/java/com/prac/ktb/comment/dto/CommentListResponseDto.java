package com.prac.ktb.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentListResponseDto {
    private final List<CommentResponseDto> comments;

    @Builder
    public CommentListResponseDto(List<CommentResponseDto> comments) {
        this.comments = comments;
    }
}

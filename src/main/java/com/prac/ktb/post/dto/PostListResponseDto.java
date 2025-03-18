package com.prac.ktb.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponseDto {
    private final List<PostResponseDto> posts;

    @Builder
    public PostListResponseDto(List<PostResponseDto> posts) {
        this.posts = posts;
    }
}

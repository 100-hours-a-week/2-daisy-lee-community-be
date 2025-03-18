package com.prac.ktb.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {
    private Long id;
    private String authorId;
    private String contents;
    private String postThumbnailPath;
    private int countRecommendation;
    private int countView;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    @Builder
    public PostResponseDto(Long id, String authorId, String contents, String postThumbnailPath, int countRecommendation, int countView, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.authorId = authorId;
        this.contents = contents;
        this.postThumbnailPath = postThumbnailPath;
        this.countRecommendation = countRecommendation;
        this.countView = countView;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }
}

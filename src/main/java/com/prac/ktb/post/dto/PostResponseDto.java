package com.prac.ktb.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prac.ktb.comment.dto.CommentResponseDto;
import com.prac.ktb.user.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto {
    private Long id;
    private String title;
    private Long authorId;
    private String contents;
    private String postThumbnailPath;
    private int countRecommendation;
    private int countView;
    private int countComment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    private AuthorDto author;
    private List<CommentResponseDto> comments;

    @Builder
    public PostResponseDto(Long id, String title, Long authorId, String contents, String postThumbnailPath, int countRecommendation, int countView, int countComment, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt, AuthorDto author) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.contents = contents;
        this.postThumbnailPath = postThumbnailPath;
        this.countRecommendation = countRecommendation;
        this.countView = countView;
        this.countComment = countComment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
        this.author = author;
    }

    @Data
    @Builder
    public static class AuthorDto {
        private Long id;
        private String nickname;
        private String profileImagePath;
    }
}

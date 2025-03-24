package com.prac.ktb.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.prac.ktb.post.dto.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {
    private Long id;
    private String comment;
    private Long commentAuthorId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private AuthorDto author;

    @Builder
    public CommentResponseDto(Long id, String comment, Long commentAuthorId, AuthorDto author, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.comment = comment;
        this.commentAuthorId = commentAuthorId;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    @Data
    @Builder
    public static class AuthorDto {
        private Long id;
        private String nickname;
        private String profileImagePath;
    }
}

package com.prac.ktb.post.dto;

import com.prac.ktb.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    private Long id;
    private String title;
    private Long authorId;
    private String contents;
    private MultipartFile postThumbnailPath;
    private int countRecommendation;
    private int countView;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
}

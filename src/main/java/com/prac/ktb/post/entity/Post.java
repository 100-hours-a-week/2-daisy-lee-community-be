package com.prac.ktb.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorId;
    private String contents;
    private String postThumbnailPath;
    private int countRecommendation;
    private int countView;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt; // 삭제된 경우 삭제 시간 저장

    public boolean isDeleted() {
        return deletedAt != null;
    }
}

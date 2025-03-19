package com.prac.ktb.post.entity;

import com.prac.ktb.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column
    private String postThumbnailPath;

    @Column
    private int countRecommendation;

    @Column
    private int countView;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Column
    private LocalDateTime deletedAt; // 삭제된 경우 삭제 시간 저장

    @Builder
    public Post(Long id, String title, User author, String contents, String postThumbnailPath, int countRecommendation, int countView, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.contents = contents;
        this.postThumbnailPath = postThumbnailPath;
        this.countRecommendation = countRecommendation;
        this.countView = countView;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void delete(){
        this.deletedAt = LocalDateTime.now();
    }
}

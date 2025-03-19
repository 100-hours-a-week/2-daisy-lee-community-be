package com.prac.ktb.comment.entity;

import com.prac.ktb.post.entity.Post;
import com.prac.ktb.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_author_id")
    private User user;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Column
    private LocalDateTime deletedAt;

}

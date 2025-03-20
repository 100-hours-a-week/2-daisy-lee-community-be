package com.prac.ktb.postRecommendation.entity;

import com.prac.ktb.post.entity.Post;
import com.prac.ktb.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="postsRecommendation")
public class PostRecommendation {

    @EmbeddedId
    private RecommendationId id;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_recommendation", nullable = false)
    private Boolean isRecommendation = false;

    @PrePersist
    public void prePersist() {
        if(isRecommendation == null) {
            isRecommendation = false;
        }
    }


}

package com.prac.ktb.postRecommendation;

import com.prac.ktb.post.Post;
import com.prac.ktb.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRecommendationRepository extends JpaRepository<PostRecommendation, Long> {
    boolean existsByUserAndPost(User user, Post post);

    Optional<PostRecommendation> findByUserAndPost(User user, Post post);
}

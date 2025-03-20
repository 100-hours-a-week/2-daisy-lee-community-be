package com.prac.ktb.postRecommendation.repository;

import com.prac.ktb.postRecommendation.entity.PostRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRecommendationRepository extends JpaRepository<PostRecommendation, Long> {
    Optional<PostRecommendation> findByPostIdAndUserId(Long postId, Long userId);
}

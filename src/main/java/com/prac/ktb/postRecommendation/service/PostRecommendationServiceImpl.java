package com.prac.ktb.postRecommendation.service;

import com.prac.ktb.post.entity.Post;
import com.prac.ktb.post.repository.PostRepository;
import com.prac.ktb.postRecommendation.entity.PostRecommendation;
import com.prac.ktb.postRecommendation.repository.PostRecommendationRepository;
import com.prac.ktb.user.entity.User;
import com.prac.ktb.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostRecommendationServiceImpl implements PostRecommendationService {
    private PostRecommendationRepository recommendationRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Transactional
    public void toggleRecommendation(Long postId, Long userId) {
        Optional<PostRecommendation> recommendation = recommendationRepository.findByPostIdAndUserId(postId, userId);

        if (recommendation.isPresent()) {
            // 이미 존재하면 상태 변경 (토글)
            PostRecommendation existingRecommendation = recommendation.get();
            existingRecommendation.setIsRecommendation(!existingRecommendation.getIsRecommendation());
        } else {
            // 없으면 새로 생성
            PostRecommendation newRecommendation = new PostRecommendation();

            Post post = postRepository.findById(postId).get();
            User user = userRepository.findById(userId).get();

            newRecommendation.setPost(post);
            newRecommendation.setUser(user);
            newRecommendation.setIsRecommendation(true);
            recommendationRepository.save(newRecommendation);
        }
    }
}

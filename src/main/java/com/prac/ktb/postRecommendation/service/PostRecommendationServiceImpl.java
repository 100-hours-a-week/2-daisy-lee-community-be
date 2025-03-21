package com.prac.ktb.postRecommendation.service;

import com.prac.ktb.post.entity.Post;
import com.prac.ktb.post.repository.PostRepository;
import com.prac.ktb.postRecommendation.entity.PostRecommendation;
import com.prac.ktb.postRecommendation.entity.RecommendationId;
import com.prac.ktb.postRecommendation.repository.PostRecommendationRepository;
import com.prac.ktb.user.entity.User;
import com.prac.ktb.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostRecommendationServiceImpl implements PostRecommendationService {
    private final PostRecommendationRepository recommendationRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleRecommendation(Long postId, UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        Optional<PostRecommendation> recommendation = recommendationRepository.findByPostIdAndUserId(postId, userId);

        if (recommendation.isPresent()) {
            // 이미 존재하면 상태 변경 (토글)
            PostRecommendation existingRecommendation = recommendation.get();
            existingRecommendation.setIsRecommendation(!existingRecommendation.getIsRecommendation());

            return existingRecommendation.getIsRecommendation();
        } else {
            // 없으면 새로 생성
            PostRecommendation newRecommendation = new PostRecommendation();

            Post post = postRepository.findById(postId).get();
            User user = userRepository.findById(userId).get();

            RecommendationId id = new RecommendationId(postId, userId);

            newRecommendation.setId(id);
            newRecommendation.setPost(post);
            newRecommendation.setUser(user);
            newRecommendation.setIsRecommendation(true);
            recommendationRepository.save(newRecommendation);

            return true;
        }


    }
}

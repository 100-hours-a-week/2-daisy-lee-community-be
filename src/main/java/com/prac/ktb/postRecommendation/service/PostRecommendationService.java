package com.prac.ktb.postRecommendation.service;

import com.prac.ktb.common.exception.CustomException;
import com.prac.ktb.post.entity.Post;
import com.prac.ktb.post.repository.PostRepository;
import com.prac.ktb.postRecommendation.entity.PostRecommendation;
import com.prac.ktb.postRecommendation.repository.PostRecommendationRepository;
import com.prac.ktb.user.entity.User;
import com.prac.ktb.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostRecommendationService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostRecommendationRepository postRecommendationRepository;

    public int recommendationPost(Long postId, UserDetails userDetails) {
        User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("post_not_found", HttpStatus.NOT_FOUND));

        // 이미 추천했는지 확인
        boolean alreadyRecommended = postRecommendationRepository.existsByUserAndPost(user, post);
        if (alreadyRecommended) {
            return post.getCountRecommendation(); // 중복이면 증가 없이 그대로 반환
        }

        // 추천 저장
        PostRecommendation postRecommendation = PostRecommendation.builder()
                .user(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();
        postRecommendationRepository.save(postRecommendation);

        // 좋아요 수 증가
        post.setCountRecommendation(post.getCountRecommendation() + 1);
        postRepository.save(post);

        return post.getCountRecommendation();
    }
}

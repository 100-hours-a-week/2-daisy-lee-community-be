package com.prac.ktb.postRecommendation.controller;

import com.prac.ktb.common.dto.ApiResponseDto;
import com.prac.ktb.post.dto.PostResponseDto;
import com.prac.ktb.postRecommendation.service.PostRecommendationService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/recommendation")
@Transactional
public class PostRecommendationController {

    private final PostRecommendationService postRecommendationService;

    public PostRecommendationController(PostRecommendationService postRecommendationService) {
        this.postRecommendationService = postRecommendationService;
    }

    /**
     * [게시물 추천]
     * @return ResponseEntity
     */
    @PostMapping("/toggle")
    public ResponseEntity<ApiResponseDto<PostResponseDto>> toggleRecommendation(@PathVariable Long postId,
                                                                                @AuthenticationPrincipal UserDetails userDetails) {
        boolean isRecommendation = postRecommendationService.toggleRecommendation(postId, userDetails);
        String message = isRecommendation ? "recommendation_true" : "recommendation_false";

        Map<String, Object> responseData = Map.of("postId", postId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(message, responseData));

    }

}

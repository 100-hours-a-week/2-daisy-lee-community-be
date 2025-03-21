package com.prac.ktb.postRecommendation.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface PostRecommendationService {
    boolean toggleRecommendation(Long postId, UserDetails userDetails);
}

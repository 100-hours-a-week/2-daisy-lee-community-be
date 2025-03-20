package com.prac.ktb.postRecommendation.controller;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts/{postId}/recommendation")
@Transactional
public class PostRecommendationController {

}

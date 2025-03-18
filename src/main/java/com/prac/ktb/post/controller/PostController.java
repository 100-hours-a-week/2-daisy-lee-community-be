package com.prac.ktb.post.controller;

import com.prac.ktb.common.dto.ApiResponseDto;
import com.prac.ktb.post.dto.PostListResponseDto;
import com.prac.ktb.post.dto.PostResponseDto;
import com.prac.ktb.post.entity.Post;
import com.prac.ktb.post.service.PostService;
import com.prac.ktb.user.dto.UserResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@Transactional
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * [게시물 목록 조회]
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<PostResponseDto>> getAllPosts() {

        PostListResponseDto allPosts = postService.getAllPosts();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("posts_all_get_success", allPosts));
    }
}

package com.prac.ktb.post.controller;

import com.prac.ktb.common.dto.ApiResponseDto;
import com.prac.ktb.post.dto.PostListResponseDto;
import com.prac.ktb.post.dto.PostRequestDto;
import com.prac.ktb.post.dto.PostResponseDto;
import com.prac.ktb.post.entity.Post;
import com.prac.ktb.post.service.PostService;
import com.prac.ktb.postRecommendation.service.PostRecommendationService;
import com.prac.ktb.user.dto.UserRequestDto;
import com.prac.ktb.user.dto.UserResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@Transactional
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRecommendationService postRecommendationService;

    /**
     * [게시물 등록]
     *
     * @param postReqDto
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<PostResponseDto>> createUser(@ModelAttribute PostRequestDto postReqDto,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        PostResponseDto newPostResDto = postService.createPost(postReqDto, userDetails);

        Map<String, Object> responseData = Map.of("postId", newPostResDto.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("post_create_success", responseData));

    }

    /**
     * [게시물 목록 조회]
     *
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<PostResponseDto>> getAllPosts() {

        PostListResponseDto allPosts = postService.getAllPosts();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("posts_all_get_success", allPosts));
    }

    /**
     * [게시물 수정]
     *
     * @param postId
     * @param postReqDto
     * @param userDetails
     * @return ResponseEntity
     */
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<PostResponseDto>> updatePost(@PathVariable Long postId,
                                                                      @ModelAttribute PostRequestDto postReqDto,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {

        postService.updatePost(postId, userDetails, postReqDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("posts_all_get_success", null));
    }

    /**
     * [게시물 단일 조회]
     *
     * @param postId
     * @param userDetails
     * @return ResponseEntity
     */
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<PostResponseDto>> getPost(@PathVariable Long postId,
                                                                   @AuthenticationPrincipal UserDetails userDetails) {

        PostResponseDto postResDto = postService.getPostById(postId, userDetails);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("post_get_success", postResDto));
    }

    /**
     * [게시물 삭제]
     *
     * @param postId
     * @param userDetails
     * @return ResponseEntity
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> deletePost(@PathVariable Long postId,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> responseData = postService.deletePost(postId, userDetails);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("post_delete_success", responseData));

    }

    /**
     * [게시물 추천]
     *
     * @param postId
     * @param userDetails
     * @return ResponseEntity
     */
    @PostMapping("/{postId}/recommendation")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> recommendationPost(@PathVariable Long postId,
                                                                                  @AuthenticationPrincipal UserDetails userDetails) {
        int recommendationCount = postRecommendationService.recommendationPost(postId, userDetails);

        Map<String, Object> data = Map.of("recommendationCount", recommendationCount);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("recommendation_success", data));
    }
}

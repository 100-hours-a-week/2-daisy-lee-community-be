package com.prac.ktb.post.controller;

import com.prac.ktb.common.dto.ApiResponseDto;
import com.prac.ktb.post.dto.PostListResponseDto;
import com.prac.ktb.post.dto.PostRequestDto;
import com.prac.ktb.post.dto.PostResponseDto;
import com.prac.ktb.post.entity.Post;
import com.prac.ktb.post.service.PostService;
import com.prac.ktb.user.dto.UserRequestDto;
import com.prac.ktb.user.dto.UserResponseDto;
import jakarta.transaction.Transactional;
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
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * [게시물 등록]
     * @param postReqDto
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<PostResponseDto>> createUser(@RequestBody PostRequestDto postReqDto,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        PostResponseDto newPostResDto = postService.createPost(postReqDto, userDetails);

        Map<String, Object> responseData = Map.of("postId", newPostResDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("post_create_success", responseData));

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

    /**
     * [게시물 수정]
     * @param postId
     * @param postReqDto
     * @param userDetails
     * @return ResponseEntity
     */
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponseDto<PostResponseDto>> updatePost(@PathVariable Long postId,
                                                                      @RequestBody PostRequestDto postReqDto,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {

        postService.updatePost(postId, userDetails, postReqDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponseDto<>("posts_all_get_success", null));
    }

    /**
     * [게시물 단일 조회]
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
}

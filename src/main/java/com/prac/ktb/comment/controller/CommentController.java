package com.prac.ktb.comment.controller;

import com.prac.ktb.comment.dto.CommentListResponseDto;
import com.prac.ktb.comment.dto.CommentRequestDto;
import com.prac.ktb.comment.dto.CommentResponseDto;
import com.prac.ktb.comment.service.CommentService;
import com.prac.ktb.common.dto.ApiResponseDto;
import com.prac.ktb.post.dto.PostRequestDto;
import com.prac.ktb.post.dto.PostResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/comments")
@Transactional
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * [댓글 등록]
     * @param commentReqDto
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<CommentResponseDto>> createComment(@PathVariable Long postId,
                                                                            @RequestBody CommentRequestDto commentReqDto,
                                                                            @AuthenticationPrincipal UserDetails userDetails) {

        CommentResponseDto newCommentResDto = commentService.createComment(postId, commentReqDto, userDetails);

        Map<String, Object> responseData = Map.of("commentId", newCommentResDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>("comment_create_success", responseData));

    }

    /**
     * [댓글 목록 조회]
     * @return ResponseEntity
     */
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<CommentResponseDto>>> getAllComments(@PathVariable Long postId) {
        CommentListResponseDto commentListResDto = commentService.getAllComments(postId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("comment_list_success", commentListResDto));
    }

    /**
     * [댓글 단일 조회]
     * @return ResponseEntity
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<CommentResponseDto>> getComment(@PathVariable Long postId,
                                                                         @PathVariable Long commentId) {
        CommentResponseDto commentResDto = commentService.getComment(postId, commentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponseDto<>("comment_get_success", commentResDto));
    }

    /**
     * [댓글 수정]
     * @param postId
     * @param commentId
     * @param commentReqDto
     * @param userDetails
     * @return ResponseEntity
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<PostResponseDto>> updatePost(@PathVariable Long postId,
                                                                      @PathVariable Long commentId,
                                                                      @RequestBody CommentRequestDto commentReqDto,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {

        commentService.updateComment(postId, commentId, userDetails, commentReqDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponseDto<>("", null));
    }

    /**
     * [댓글 삭제]
     * @param postId
     * @param commentId
     * @param userDetails
     * @return ResponseEntity
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto<PostResponseDto>> deletePost(@PathVariable Long postId,
                                                                      @PathVariable Long commentId,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponseDto<>("", null));
    }


}

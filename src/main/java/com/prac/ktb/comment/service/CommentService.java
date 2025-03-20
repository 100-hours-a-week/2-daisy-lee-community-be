package com.prac.ktb.comment.service;

import com.prac.ktb.comment.dto.CommentListResponseDto;
import com.prac.ktb.comment.dto.CommentRequestDto;
import com.prac.ktb.comment.dto.CommentResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(Long postId, CommentRequestDto commentReqDto, UserDetails userDetails);
    CommentListResponseDto getAllComments(Long postId);
    CommentResponseDto getComment(Long postId, Long commentId);
    void updateComment(Long postId, Long commentId, UserDetails userDetails, CommentRequestDto commentReqDto);
    void deleteComment(Long commentId, UserDetails userDetails);
}

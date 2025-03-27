package com.prac.ktb.comment.service;

import com.prac.ktb.comment.dto.CommentListResponseDto;
import com.prac.ktb.comment.dto.CommentRequestDto;
import com.prac.ktb.comment.dto.CommentResponseDto;
import com.prac.ktb.comment.entity.Comment;
import com.prac.ktb.comment.repository.CommentRepository;
import com.prac.ktb.common.exception.CustomException;
import com.prac.ktb.post.dto.PostRequestDto;
import com.prac.ktb.post.entity.Post;
import com.prac.ktb.post.repository.PostRepository;
import com.prac.ktb.user.entity.User;
import com.prac.ktb.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentResponseDto createComment(Long postId, CommentRequestDto commentReqDto, UserDetails userDetails) {
        Post post = postRepository.findById(postId)
                        .orElseThrow(() -> new CustomException("post_not_found", HttpStatus.NOT_FOUND));

        User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
                        .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        Comment newComment = Comment.builder()
                        .post(post)
                        .user(user)
                        .comment(commentReqDto.getComment())
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .deletedAt(null)
                        .build();

        commentRepository.save(newComment);

        return CommentResponseDto.builder()
                .id(newComment.getId())
                .comment(newComment.getComment())
                .commentAuthorId(newComment.getUser().getId())
                .createdAt(newComment.getCreatedAt())
                .modifiedAt(newComment.getModifiedAt())
                .deletedAt(newComment.getDeletedAt())
                .build();
    }

    @Override
    public CommentListResponseDto getAllComments(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdAndDeletedAtIsNullOrderByCreatedAtDesc(postId);

        List<CommentResponseDto> commentDtos = comments.stream()
                .map(comment -> {
                    User author = comment.getUser();
                    return CommentResponseDto.builder()
                            .id(comment.getId())
                            .comment(comment.getComment())
                            .commentAuthorId(comment.getUser().getId())
                            .author(CommentResponseDto.AuthorDto.builder()
                                    .id(author.getId())
                                    .nickname(author.getNickname())
                                    .profileImagePath(author.getProfileImagePath())
                                    .build())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .deletedAt(comment.getDeletedAt())
                            .build();
                })
                .toList();

        return CommentListResponseDto.builder()
                .comments(commentDtos)
                .build();
    }

    @Override
    public CommentResponseDto getComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new CustomException("comment_not_found", HttpStatus.NOT_FOUND));

        return CommentResponseDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .commentAuthorId(comment.getUser().getId())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }

    @Override
    public void updateComment(Long postId, Long commentId, UserDetails userDetails, CommentRequestDto commentReqDto) {
        User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        Comment updateComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("comment_not_found", HttpStatus.NOT_FOUND));

        // 본인 작성인지 확인
        if(!updateComment.getUser().getId().equals(user.getId())) {
            throw new CustomException("comment_update_unauthorized", HttpStatus.UNAUTHORIZED);
        }

        updateComment.setComment(commentReqDto.getComment());
        updateComment.setModifiedAt(LocalDateTime.now());

    }

    @Override
    public void deleteComment(Long commentId, UserDetails userDetails) {
        User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        Comment deleteComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException("comment_not_found", HttpStatus.NOT_FOUND));

        // 본인 작성인지 확인
        if(!deleteComment.getUser().getId().equals(user.getId())) {
            throw new CustomException("comment_update_unauthorized", HttpStatus.UNAUTHORIZED);
        }

        deleteComment.delete(); // soft delete
        commentRepository.save(deleteComment);
    }


}

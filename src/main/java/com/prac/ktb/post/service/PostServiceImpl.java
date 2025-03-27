package com.prac.ktb.post.service;

import com.prac.ktb.comment.dto.CommentResponseDto;
import com.prac.ktb.comment.repository.CommentRepository;
import com.prac.ktb.common.config.CommonProperties;
import com.prac.ktb.common.exception.CustomException;
import com.prac.ktb.common.service.FileService;
import com.prac.ktb.post.dto.PostListResponseDto;
import com.prac.ktb.post.dto.PostRequestDto;
import com.prac.ktb.post.dto.PostResponseDto;
import com.prac.ktb.post.entity.Post;
import com.prac.ktb.post.repository.PostRepository;
import com.prac.ktb.postRecommendation.entity.PostRecommendation;
import com.prac.ktb.postRecommendation.repository.PostRecommendationRepository;
import com.prac.ktb.user.dto.UserResponseDto;
import com.prac.ktb.user.entity.User;
import com.prac.ktb.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommonProperties commonProperties;
    private final FileService fileService;
    private final PostRecommendationRepository postRecommendationRepository;
    private PostResponseDto postResDto;

    @Override
    public PostResponseDto createPost(PostRequestDto postReqDto, UserDetails userDetails) {
        User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        Post newPost = new Post();
        newPost.setTitle(postReqDto.getTitle());
        newPost.setContents(postReqDto.getContents());
        newPost.setAuthor(user);
        newPost.setCreatedAt(LocalDateTime.now());
        newPost.setModifiedAt(LocalDateTime.now());

        MultipartFile postThumbnail = postReqDto.getPostThumbnailPath();
        if (postThumbnail != null && !postThumbnail.isEmpty()) {
            String imageUrl = fileService.savePostThumbnail(postThumbnail); // 이미지 저장 후 URL 반환
            newPost.setPostThumbnailPath(imageUrl);
        }

        postRepository.save(newPost);

        return PostResponseDto.builder()
                .id(newPost.getId())
                .build();
    }

    @Override
    public PostListResponseDto getAllPosts() {
        List<Post> posts = postRepository.findByDeletedAtIsNullOrderByCreatedAtDesc();

        List<PostResponseDto> postDtos = posts.stream()
                .map(post -> {
                    int countComment = commentRepository.countByPostAndDeletedAtIsNull(post);
                    User author = post.getAuthor();
                    return PostResponseDto.builder()
                            .id(post.getId())
                            .authorId(post.getAuthor().getId())
                            .author(PostResponseDto.AuthorDto.builder()
                                    .id(author.getId())
                                    .nickname(author.getNickname())
                                    .profileImagePath(author.getProfileImagePath())
                                    .build())
                            .title(post.getTitle())
                            .contents(post.getContents())
                            .postThumbnailPath(post.getPostThumbnailPath())
                            .countRecommendation(post.getCountRecommendation())
                            .countView(post.getCountView())
                            .countComment(countComment)
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .deletedAt(post.getDeletedAt())
                            .build();
                })
                .toList();

        return PostListResponseDto.builder()
                .posts(postDtos)
                .build();
    }

    @Override
    public void updatePost(Long postId, UserDetails userDetails, PostRequestDto postReqDto) {
        Post updatePost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("post_not_found", HttpStatus.NOT_FOUND));

        User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        // 본인 작성인지 확인
        if (!updatePost.getAuthor().getId().equals(user.getId())) {
            throw new CustomException("post_unauthorized", HttpStatus.UNAUTHORIZED);
        }

        MultipartFile postThumbnail = postReqDto.getPostThumbnailPath();
        if (postThumbnail != null && !postThumbnail.isEmpty()) {
            String imageUrl = fileService.savePostThumbnail(postThumbnail); // 이미지 저장 후 URL 반환
            updatePost.setPostThumbnailPath(imageUrl);
        }

        updatePost.setTitle(postReqDto.getTitle());
        updatePost.setContents(postReqDto.getContents());
        updatePost.setModifiedAt(LocalDateTime.now());

    }

    @Override
    public PostResponseDto getPostById(Long postId, UserDetails userDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("post_not_found", HttpStatus.NOT_FOUND));

        User author = userRepository.findById(post.getAuthor().getId())
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        int countComment = commentRepository.countByPostAndDeletedAtIsNull(post);
        LocalDateTime recommendationCreatedAt = null;
        Optional<PostRecommendation> recommendation = postRecommendationRepository.findByUserAndPost(author, post);

        recommendationCreatedAt = recommendation.map(PostRecommendation::getCreatedAt).orElse(null);
        post.setCountView(post.getCountView() + 1); // 조회수 증가
        postRepository.save(post);

        PostResponseDto.AuthorDto authorDto = PostResponseDto.AuthorDto.builder()
                .id(author.getId())
                .nickname(author.getNickname())
                .profileImagePath(author.getProfileImagePath()) // 필요하면 Null 체크
                .build();

        PostResponseDto postResDto = PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .author(authorDto)
                .postThumbnailPath(post.getPostThumbnailPath())
                .countRecommendation(post.getCountRecommendation())
                .recommendationCreatedAt(recommendationCreatedAt)
                .countView(post.getCountView())
                .countComment(countComment)
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .deletedAt(post.getDeletedAt())
                .build();

        return postResDto;
    }

    @Override
    public Map<String, Object> deletePost(Long postId, UserDetails userDetails) {
        Post deletePost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("post_not_found", HttpStatus.NOT_FOUND));

        if (deletePost.isDeleted()) {
            throw new CustomException("post_already_deleted", HttpStatus.BAD_REQUEST);
        }
        deletePost.delete();
        postRepository.save(deletePost);

        // Todo. 댓글도 함께 삭제 필요

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("redirectURL", commonProperties.getPostDeleteURL());

        return responseMap;
    }

}

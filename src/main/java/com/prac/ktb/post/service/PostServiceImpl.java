package com.prac.ktb.post.service;

import com.prac.ktb.common.config.CommonProperties;
import com.prac.ktb.common.exception.CustomException;
import com.prac.ktb.post.dto.PostListResponseDto;
import com.prac.ktb.post.dto.PostRequestDto;
import com.prac.ktb.post.dto.PostResponseDto;
import com.prac.ktb.post.entity.Post;
import com.prac.ktb.post.repository.PostRepository;
import com.prac.ktb.user.dto.UserResponseDto;
import com.prac.ktb.user.entity.User;
import com.prac.ktb.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommonProperties commonProperties;
    private PostResponseDto postResDto;

    @Override
    public PostResponseDto createPost(PostRequestDto postReqDto, UserDetails userDetails) {

        User user = userRepository.findById(Long.parseLong(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException("user_not_found", HttpStatus.NOT_FOUND));

        Post newPost = Post.builder()
                .title(postReqDto.getTitle())
                .author(user)
                .contents(postReqDto.getContents())
                .postThumbnailPath(postReqDto.getPostThumbnailPath())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        postRepository.save(newPost);

        return PostResponseDto.builder()
                .id(newPost.getId())
                .build();
    }

    @Override
    public PostListResponseDto getAllPosts() {
        List<Post> posts = postRepository.findByDeletedAtIsNull();

        List<PostResponseDto> postDtos = posts.stream()
                .map(post -> PostResponseDto.builder()
                                    .id(post.getId())
                                    .authorId(post.getAuthor().getId())
                                    .title(post.getTitle())
                                    .contents(post.getContents())
                                    .postThumbnailPath(post.getPostThumbnailPath())
                                    .countRecommendation(post.getCountRecommendation())
                                    .countView(post.getCountView())
                                    .createdAt(post.getCreatedAt())
                                    .modifiedAt(post.getModifiedAt())
                                    .deletedAt(post.getDeletedAt())
                                    .build())
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
        if(!updatePost.getAuthor().getId().equals(user.getId())) {
            throw new CustomException("post_unauthorized", HttpStatus.UNAUTHORIZED);
        }

        updatePost.setTitle(postReqDto.getTitle());
        updatePost.setContents(postReqDto.getContents());
        updatePost.setPostThumbnailPath(postReqDto.getPostThumbnailPath());
        updatePost.setModifiedAt(LocalDateTime.now());

    }

    @Override
    public PostResponseDto getPostById(Long postId, UserDetails userDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("post_not_found", HttpStatus.NOT_FOUND));

        PostResponseDto postResDto = PostResponseDto.builder()
                                        .id(post.getId())
                                        .title(post.getTitle())
                                        .contents(post.getContents())
                                        .authorId(post.getAuthor().getId())
                                        .postThumbnailPath(post.getPostThumbnailPath())
                                        .countRecommendation(post.getCountRecommendation())
                                        .countView(post.getCountView())
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

        if(deletePost.isDeleted()) {
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

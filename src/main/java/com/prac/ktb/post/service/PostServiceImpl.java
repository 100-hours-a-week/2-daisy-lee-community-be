package com.prac.ktb.post.service;

import com.prac.ktb.common.exception.CustomException;
import com.prac.ktb.post.dto.PostListResponseDto;
import com.prac.ktb.post.dto.PostRequestDto;
import com.prac.ktb.post.dto.PostResponseDto;
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

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
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
//                .title(newPost.getTitle())
//                .author(newPost.getAuthor())
//                .contents(newPost.getContents())
//                .postThumbnailPath(newPost.getPostThumbnailPath())
//                .createdAt(newPost.getCreatedAt())
//                .modifiedAt(newPost.getModifiedAt())
                .build();
    }

    @Override
    public PostListResponseDto getAllPosts() {
        List<Post> posts = postRepository.findByDeletedAtIsNull();

        List<PostResponseDto> postDtos = posts.stream()
                .map(post -> PostResponseDto.builder()
                                    .id(post.getId())
                                    .authorId(post.getAuthor().getId())
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
    public PostResponseDto updatePost(PostRequestDto postReqDto) {
        return null;
    }
}

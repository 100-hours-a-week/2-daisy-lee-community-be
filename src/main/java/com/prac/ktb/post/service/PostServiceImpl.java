package com.prac.ktb.post.service;

import com.prac.ktb.post.dto.PostListResponseDto;
import com.prac.ktb.post.dto.PostResponseDto;
import com.prac.ktb.post.entity.Post;
import com.prac.ktb.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private PostResponseDto postResDto;

    @Override
    public PostListResponseDto getAllPosts() {
        List<Post> posts = postRepository.findByDeletedAtIsNull();

        List<PostResponseDto> postDtos = posts.stream()
                .map(post -> PostResponseDto.builder()
                                    .id(post.getId())
                                    .authorId(post.getAuthorId())
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
}

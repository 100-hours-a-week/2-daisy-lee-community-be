package com.prac.ktb.post.service;


import com.prac.ktb.post.dto.PostListResponseDto;
import com.prac.ktb.post.dto.PostRequestDto;
import com.prac.ktb.post.dto.PostResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface PostService {
    PostResponseDto createPost(PostRequestDto postReqDto, UserDetails userDetails);
    PostListResponseDto getAllPosts();
    void updatePost(Long postId, UserDetails userDetails, PostRequestDto postReqDto);
    PostResponseDto getPostById(Long postId, UserDetails userDetails);
}

package com.prac.ktb.post.service;


import com.prac.ktb.post.dto.PostListResponseDto;
import com.prac.ktb.post.dto.PostRequestDto;
import com.prac.ktb.post.dto.PostResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostRequestDto postReqDto, UserDetails userDetails);
    PostListResponseDto getAllPosts();
    PostResponseDto updatePost(PostRequestDto postReqDto);
}

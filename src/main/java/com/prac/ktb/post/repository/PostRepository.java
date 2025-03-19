package com.prac.ktb.post.repository;

import com.prac.ktb.post.dto.PostResponseDto;
import com.prac.ktb.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post save(PostResponseDto postResDto);
    List<Post> findByDeletedAtIsNull(); // 게시물
}

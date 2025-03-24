package com.prac.ktb.post.repository;

import com.prac.ktb.post.dto.PostResponseDto;
import com.prac.ktb.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post save(PostResponseDto postResDto);
    Optional<Post> findByIdAndDeletedAtIsNull(Long postId);
    List<Post> findByDeletedAtIsNullOrderByCreatedAtDesc(); // 게시물
    // postid 로 user 찾게끔
    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :postId")
    Optional<Post> findByIdWithAuthor(@Param("postId") Long postId);

}

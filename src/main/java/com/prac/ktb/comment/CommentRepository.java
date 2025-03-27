package com.prac.ktb.comment;

import com.prac.ktb.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndDeletedAtIsNull(Long commentId);
    List<Comment> findByPostIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long postId);
    int countByPostAndDeletedAtIsNull(Post post);
}

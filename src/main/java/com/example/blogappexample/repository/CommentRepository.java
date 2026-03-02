package com.example.blogappexample.repository;

import com.example.blogappexample.domain.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByPostId(Long postId);
    List<CommentEntity> findByUserId(Long userId);
}

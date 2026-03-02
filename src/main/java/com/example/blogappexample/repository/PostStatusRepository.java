package com.example.blogappexample.repository;

import com.example.blogappexample.domain.PostStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostStatusRepository extends JpaRepository<PostStatusEntity, String> {
}

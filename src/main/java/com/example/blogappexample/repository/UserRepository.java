package com.example.blogappexample.repository;

import com.example.blogappexample.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username); // needed for authentication
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

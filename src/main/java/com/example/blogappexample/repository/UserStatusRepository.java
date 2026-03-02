package com.example.blogappexample.repository;

import com.example.blogappexample.domain.UserStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatusEntity, String> {
}

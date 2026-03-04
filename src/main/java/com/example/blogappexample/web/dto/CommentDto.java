package com.example.blogappexample.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
public record CommentDto(
    Long id,
    @NotBlank String content,
    LocalDateTime createTime,
    Long userId,
    Long postId
) {}
package com.example.blogappexample.web.dto;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PostDto (
    Long id,
    @Size(max = 200) String title,
    String content,
    LocalDateTime createTime,
    LocalDateTime updateTime,
    Long userId,
    @Size(max = 20) String status // post_status
) {}

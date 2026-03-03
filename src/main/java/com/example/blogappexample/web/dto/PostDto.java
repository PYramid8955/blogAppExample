package com.example.blogappexample.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

public class PostDto {
    private Long id;

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String content;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private Long userId;

    @NotBlank
    @Size(max = 20)
    private String status; // post_status

    public PostDto() {
    }

    public PostDto(Long id, String title, String content, LocalDateTime createTime, LocalDateTime updateTime, Long userId, String status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.userId = userId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PostDto postDto = (PostDto) o;
        return Objects.equals(id, postDto.id) && Objects.equals(title, postDto.title) && Objects.equals(content, postDto.content) && Objects.equals(createTime, postDto.createTime) && Objects.equals(updateTime, postDto.updateTime) && Objects.equals(userId, postDto.userId) && Objects.equals(status, postDto.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, createTime, updateTime, userId, status);
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                '}';
    }
}

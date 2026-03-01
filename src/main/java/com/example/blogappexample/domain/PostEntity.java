package com.example.blogappexample.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "post")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @NotBlank
    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @NotBlank
    @ManyToOne
    @JoinColumn(nullable = false)
    private PostStatusEntity status;

    @OneToMany(mappedBy = "post")
    private List<CommentEntity> comments;

    public PostEntity() {
    }

    public PostEntity(Long id, String content, LocalDateTime createTime, LocalDateTime updateTime, UserEntity user, PostStatusEntity status, List<CommentEntity> comments) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.user = user;
        this.status = status;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PostStatusEntity getStatus() {
        return status;
    }

    public void setStatus(PostStatusEntity status) {
        this.status = status;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PostEntity that = (PostEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(content, that.content) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(user, that.user) && Objects.equals(status, that.status) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, createTime, updateTime, user, status, comments);
    }

    @Override
    public String toString() {
        return "PostEntity{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", user=" + user +
                ", status=" + status +
                ", comments=" + comments +
                '}';
    }
}

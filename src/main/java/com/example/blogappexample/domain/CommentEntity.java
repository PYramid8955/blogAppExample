package com.example.blogappexample.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @NotBlank
    @CreationTimestamp
    @Column(name="create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    public CommentEntity() {
    }

    public CommentEntity(Long id, String content, LocalDateTime createTime, UserEntity user, PostEntity post) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.user = user;
        this.post = post;
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "CommentEntity{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", user=" + user +
                ", post=" + post +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(content, that.content)
                && Objects.equals(createTime, that.createTime)
                && Objects.equals(user, that.user)
                && Objects.equals(post, that.post);
    }

    @Override
    public int hashCode() { return Objects.hash(id, content, createTime, user, post); }
}

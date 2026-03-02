package com.example.blogappexample.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "post_status")
public class PostStatusEntity {
    @Id
    @Column(length = 20)
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private List<PostEntity> posts;

    public PostStatusEntity() {
    }

    public PostStatusEntity(String status, List<PostEntity> posts) {
        this.status = status;
        this.posts = posts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PostEntity> getPosts() {
        return posts;
    }

    public void setPosts(List<PostEntity> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PostStatusEntity that = (PostStatusEntity) o;
        return Objects.equals(status, that.status) && Objects.equals(posts, that.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, posts);
    }

    @Override
    public String toString() {
        return "PostStatusEntity{" +
                "status='" + status + '\'' +
                ", posts=" + posts +
                '}';
    }
}

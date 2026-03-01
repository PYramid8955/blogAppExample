package com.example.blogappexample.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user_status")
public class UserStatusEntity {
    @Id
    @Column(length = 20)
    private String status;

    @OneToMany(mappedBy = "status")
    private List<UserEntity> users;

    public UserStatusEntity() {}

    public UserStatusEntity(String status, List<UserEntity> users) {
        this.status = status;
        this.users = users;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserStatusEntity that = (UserStatusEntity) o;
        return Objects.equals(status, that.status) && Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, users);
    }

    @Override
    public String toString() {
        return "UserStatusEntity{" +
                "status='" + status + '\'' +
                ", users=" + users +
                '}';
    }
}

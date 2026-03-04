package com.example.blogappexample.service;

import com.example.blogappexample.domain.PostEntity;
import com.example.blogappexample.domain.PostStatusEntity;
import com.example.blogappexample.domain.UserEntity;
import com.example.blogappexample.exception.ResourceNotFoundException;
import com.example.blogappexample.repository.PostRepository;
import com.example.blogappexample.repository.PostStatusRepository;
import com.example.blogappexample.repository.UserRepository;
import com.example.blogappexample.web.dto.PostDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostStatusRepository postStatusRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, PostStatusRepository postStatusRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postStatusRepository = postStatusRepository;
    }

    @Transactional
    public PostDto createPost(PostDto dto) {
        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserEntity user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));
        PostStatusEntity status = postStatusRepository.findById(dto.status())
                .orElseThrow(() -> new ResourceNotFoundException("PostStatus not found: " + dto.status()));

        PostEntity post = new PostEntity();
        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setUser(user);
        post.setStatus(status);

        PostEntity saved = postRepository.save(post);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!currentUsername.equals("admin")) {
            throw new SecurityException("Special authorization required!");
        }

        return postRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostDto getPostById(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return postRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDto updatePost(Long id, PostDto dto) {
        //TODO: ADD CHECKSUMS + CHECKS TO VERIFY IF FIELD NOT NULL, THEN UPDATE (SO THAT YOU DON'T SEND OLD DATA)
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        if (!(post.getUser().getUsername().equals(currentUsername) || currentUsername.equals("admin"))) {
            throw new SecurityException("You can only update your own posts");
        }

        // If user changed, check existence
        if (!post.getUser().getId().equals(dto.userId())) {
            UserEntity user = userRepository.findById(dto.userId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.userId()));
            post.setUser(user);
        }

        PostStatusEntity status = postStatusRepository.findById(dto.status())
                .orElseThrow(() -> new ResourceNotFoundException("PostStatus not found: " + dto.status()));

        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setStatus(status);

        PostEntity updated = postRepository.save(post);
        return mapToDto(updated);
    }

    @Transactional
    public void deletePost(Long id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        if (!(post.getUser().getUsername().equals(currentUsername) || currentUsername.equals("admin"))) {
            throw new SecurityException("You can only delete your own posts");
        }

        postRepository.deleteById(id);
    }

    private PostDto mapToDto(PostEntity post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreateTime(),
                post.getUpdateTime(),
                post.getUser().getId(),
                post.getStatus().getStatus()
                );
    }
}
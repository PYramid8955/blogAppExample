package com.example.blogappexample.service;

import com.example.blogappexample.domain.PostEntity;
import com.example.blogappexample.domain.PostStatusEntity;
import com.example.blogappexample.domain.UserEntity;
import com.example.blogappexample.exception.ResourceNotFoundException;
import com.example.blogappexample.repository.PostRepository;
import com.example.blogappexample.repository.PostStatusRepository;
import com.example.blogappexample.repository.UserRepository;
import com.example.blogappexample.web.dto.PostDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
        PostStatusEntity status = postStatusRepository.findById(dto.getStatus())
                .orElseThrow(() -> new ResourceNotFoundException("PostStatus not found: " + dto.getStatus()));

        PostEntity post = new PostEntity();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUser(user);
        post.setStatus(status);

        PostEntity saved = postRepository.save(post);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
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
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        // If user changed, check existence
        if (!post.getUser().getId().equals(dto.getUserId())) {
            UserEntity user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
            post.setUser(user);
        }

        PostStatusEntity status = postStatusRepository.findById(dto.getStatus())
                .orElseThrow(() -> new ResourceNotFoundException("PostStatus not found: " + dto.getStatus()));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setStatus(status);

        PostEntity updated = postRepository.save(post);
        return mapToDto(updated);
    }

    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    private PostDto mapToDto(PostEntity post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreateTime(post.getCreateTime());
        dto.setUpdateTime(post.getUpdateTime());
        dto.setUserId(post.getUser().getId());
        dto.setStatus(post.getStatus().getStatus());
        return dto;
    }
}
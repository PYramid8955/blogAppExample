package com.example.blogappexample.service;

import com.example.blogappexample.domain.CommentEntity;
import com.example.blogappexample.domain.PostEntity;
import com.example.blogappexample.domain.UserEntity;
import com.example.blogappexample.exception.ResourceNotFoundException;
import com.example.blogappexample.repository.CommentRepository;
import com.example.blogappexample.repository.PostRepository;
import com.example.blogappexample.repository.UserRepository;
import com.example.blogappexample.web.dto.CommentDto;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public CommentDto createComment(CommentDto dto) {
        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        UserEntity user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + currentUsername));
        PostEntity post = postRepository.findById(dto.postId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + dto.postId()));

        CommentEntity comment = new CommentEntity();
        comment.setContent(dto.content());
        comment.setUser(user);
        comment.setPost(post);

        CommentEntity saved = commentRepository.save(comment);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        return commentRepository.findByPostId(postId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return commentRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long id) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        return mapToDto(comment);
    }

    @Transactional
    public CommentDto updateComment(Long id, CommentDto dto) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        if (!(comment.getUser().getUsername().equals(currentUsername) || currentUsername.equals("admin"))) {
            throw new AccessDeniedException("You can only update your own comments");
        }

        comment.setContent(dto.content());

        CommentEntity updated = commentRepository.save(comment);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteComment(Long id) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));

        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        if (!(comment.getUser().getUsername().equals(currentUsername) || currentUsername.equals("admin"))) {
            throw new AccessDeniedException("You can only delete your own comments");
        }

        commentRepository.deleteById(id);
    }

    private CommentDto mapToDto(CommentEntity comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreateTime(),
                comment.getUser().getId(),
                comment.getPost().getId()
        );
    }
}
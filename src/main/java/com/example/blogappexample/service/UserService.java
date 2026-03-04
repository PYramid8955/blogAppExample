package com.example.blogappexample.service;

import com.example.blogappexample.domain.UserEntity;
import com.example.blogappexample.domain.UserStatusEntity;
import com.example.blogappexample.exception.ResourceNotFoundException;
import com.example.blogappexample.repository.UserRepository;
import com.example.blogappexample.repository.UserStatusRepository;
import com.example.blogappexample.web.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserStatusRepository userStatusRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto createUser(UserDto dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        UserStatusEntity status = userStatusRepository.findById(dto.status())
                .orElseThrow(() -> new ResourceNotFoundException("UserStatus not found: " + dto.status()));

        UserEntity user = new UserEntity();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password())); // encrypt password
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setStatus(status);

        UserEntity saved = userRepository.save(user);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        if (!currentUsername.equals("admin")) {
            throw new SecurityException("Special authorization required!");
        }

        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToDto(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto dto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        if (!(user.getUsername().equals(currentUsername) || currentUsername.equals("admin"))) {
            throw new SecurityException("You can only update your own account");
        }

        // Check uniqueness if changed
        if (!user.getUsername().equals(dto.username()) && userRepository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        UserStatusEntity status = userStatusRepository.findById(dto.status())
                .orElseThrow(() -> new ResourceNotFoundException("UserStatus not found: " + dto.status()));

        user.setUsername(dto.username());
        // Only update password if provided (optional)
        if (dto.password() != null && !dto.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        user.setStatus(status);

        UserEntity updated = userRepository.save(user);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // get current logged-in username
        String currentUsername = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        if (!(user.getUsername().equals(currentUsername) || currentUsername.equals("admin"))) {
            throw new SecurityException("You can only delete your own account");
        }

        userRepository.delete(user);
    }

    private UserDto mapToDto(UserEntity user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                null,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStatus().getStatus()
                );
    }
}
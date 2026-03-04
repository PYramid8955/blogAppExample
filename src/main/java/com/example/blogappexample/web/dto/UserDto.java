package com.example.blogappexample.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserDto (
    Long id,

    @Size(min = 3, max = 75)
    String username,

    @Size(min = 8, max = 100)
    String password, // only for input, will be ignored in output

    @Size(max = 50)
    String firstName,

    @Size(max = 50)
    String lastName,

    @Email
    @Size(max = 100)
    String email,

    @Size(max = 20)
    String status
){}
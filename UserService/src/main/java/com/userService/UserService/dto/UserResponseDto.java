package com.userService.UserService.dto;

import com.userService.UserService.models.Role;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDto {
    private long id;
    private String name;
    private String email;
    private String token;
    private Set<Role> roles = new HashSet<>();
}

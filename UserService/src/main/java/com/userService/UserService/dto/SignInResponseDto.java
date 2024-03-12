package com.userService.UserService.dto;

import com.userService.UserService.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SignInResponseDto {
    private String message;
    private Set<Role> roles;
}

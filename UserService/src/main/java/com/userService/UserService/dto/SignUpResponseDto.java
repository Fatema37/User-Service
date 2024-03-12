package com.userService.UserService.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class SignUpResponseDto {
    private long id;
    private String name;
    private String email;
    private String token;
    private String message;
}

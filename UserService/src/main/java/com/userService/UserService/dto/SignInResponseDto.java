package com.userService.UserService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInResponseDto {
    private String message;
    private String token;
}

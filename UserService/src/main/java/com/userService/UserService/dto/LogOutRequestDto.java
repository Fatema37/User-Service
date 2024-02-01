package com.userService.UserService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogOutRequestDto {
    private String email;
    private String token;
}

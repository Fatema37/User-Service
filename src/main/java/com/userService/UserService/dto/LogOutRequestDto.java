package com.userService.UserService.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LogOutRequestDto {
    private long userId;
    private String token;
}

package com.userService.UserService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDto {

    private String email;
    private String password;
}

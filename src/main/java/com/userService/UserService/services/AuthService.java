package com.userService.UserService.services;


import com.userService.UserService.dto.LogOutRequestDto;
import com.userService.UserService.dto.LogOutResponseDto;
import com.userService.UserService.dto.SignUpRequestDto;
import com.userService.UserService.dto.ValidateResponseDto;
import com.userService.UserService.exceptions.NotFoundException;
import com.userService.UserService.models.SessionStatus;
import com.userService.UserService.models.User;

import java.util.UUID;

public interface AuthService {
    public User signUp(SignUpRequestDto signUpRequestDto) throws NotFoundException;

    public User signIn(String email, String password) throws NotFoundException;


    public void logout(String token, long userId);

    public SessionStatus validate(String token, long userId) throws NotFoundException;

}

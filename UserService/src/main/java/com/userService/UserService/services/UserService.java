package com.userService.UserService.services;

import com.userService.UserService.dto.*;
import com.userService.UserService.exceptions.NotFoundException;
import com.userService.UserService.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User signUp(SignUpRequestDto signUpRequestDto) throws NotFoundException;

    public User signIn(String email, String password) throws NotFoundException;

    public LogOutResponseDto logout(LogOutRequestDto logOutRequestDto);

    public ValidateResponseDto validate(String email, String token);


}

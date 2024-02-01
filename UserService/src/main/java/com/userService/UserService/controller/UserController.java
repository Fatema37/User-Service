package com.userService.UserService.controller;

import com.userService.UserService.dto.*;
import com.userService.UserService.exceptions.NotFoundException;
import com.userService.UserService.models.User;
import com.userService.UserService.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Component
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws NotFoundException {
        SignUpResponseDto responseDto = new SignUpResponseDto();
        try {
            User user = userService.signUp(signUpRequestDto);
            responseDto.setId(user.getId());
            responseDto.setName(user.getName());
            responseDto.setEmail(user.getEmail());
            responseDto.setToken(user.getToken());
            String message = "User created successfully.";
            responseDto.setMessage(message);
            return responseDto;
        }
        catch (NotFoundException e) {
            responseDto.setMessage(e.getMessage());
            throw e;
        }
        catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            throw e;
        }
    }

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInRequestDto signInRequestDto) throws NotFoundException {
        SignInResponseDto responseDto = new SignInResponseDto();
        try {
            User user = userService.signIn(signInRequestDto.getEmail(), signInRequestDto.getPassword());
            responseDto.setToken(user.getToken());
            String message = "User logged in successfully.";
            responseDto.setMessage(message);
            return responseDto;
        }
        catch (NotFoundException e) {
            responseDto.setMessage(e.getMessage());
            throw e;
        }
        catch (Exception e) {
            responseDto.setMessage(e.getMessage());
            throw e;
        }
    }
    @PostMapping("/logout")
    public LogOutResponseDto logout(@RequestBody LogOutRequestDto logOutRequestDto) throws NotFoundException {
       LogOutResponseDto logOutResponseDto = new LogOutResponseDto();
        try {
           logOutResponseDto = userService.logout(logOutRequestDto);

        }
        catch (Exception e) {
            logOutResponseDto.setMessage(e.getMessage());
            throw e;
        }
        return logOutResponseDto;

    }

    @PostMapping("/validate")
    public ValidateResponseDto validate(@RequestBody ValidateRequestDto validateRequestDto) throws NotFoundException {
        ValidateResponseDto validateResponseDto = new ValidateResponseDto();
        try {
            validateResponseDto = userService.validate(validateRequestDto.getEmail(), validateRequestDto.getToken());
            return validateResponseDto;
        }
        catch (Exception e) {
            validateResponseDto.setMessage(e.getMessage());
            throw e;
        }
    }

}

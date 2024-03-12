package com.userService.UserService.controller;

import com.userService.UserService.dto.*;
import com.userService.UserService.exceptions.NotFoundException;
import com.userService.UserService.models.SessionStatus;
import com.userService.UserService.models.User;
import com.userService.UserService.services.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Component
@RestController
@RequestMapping("/users")

public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) throws NotFoundException {
        SignUpResponseDto responseDto = new SignUpResponseDto();
        try {
            User user = authService.signUp(signUpRequestDto);
            responseDto.setId(user.getId());
            responseDto.setName(user.getName());
            responseDto.setEmail(user.getEmail());
            responseDto.setToken(user.getToken());
            String message = "User created successfully.";
            responseDto.setMessage(message);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
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
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto signInRequestDto) throws NotFoundException {
        SignInResponseDto responseDto = new SignInResponseDto();
        try {
            User user = authService.signIn(signInRequestDto.getEmail(), signInRequestDto.getPassword());
            responseDto.setRoles(user.getRoles());
            String message = "User logged in successfully.";
            responseDto.setMessage(message);
            MultiValueMap<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
            headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + user.getToken());

            return new ResponseEntity<SignInResponseDto>(responseDto, headers, HttpStatus.OK);
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
    public ResponseEntity<LogOutResponseDto> logout(@RequestBody LogOutRequestDto logOutRequestDto) throws NotFoundException {
        LogOutResponseDto logOutResponseDto = new LogOutResponseDto();
        try {
           authService.logout(logOutRequestDto.getToken(), logOutRequestDto.getUserId());

        }
        catch (Exception e) {
            logOutResponseDto.setMessage(e.getMessage());
            throw e;
        }
        return ResponseEntity.ok().build();

    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validate(@RequestBody ValidateRequestDto validateRequestDto) throws NotFoundException {
       SessionStatus sessionStatus;
        try {
            sessionStatus = authService.validate(validateRequestDto.getToken(), validateRequestDto.getUserId());
            return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
        }
        catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }



}

package com.userService.UserService.controller;

import com.userService.UserService.dto.*;
import com.userService.UserService.exceptions.NotFoundException;
import com.userService.UserService.models.User;
import com.userService.UserService.services.AuthService;
import com.userService.UserService.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@Component
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserDetails(@PathVariable("id") long userId) throws NotFoundException {
        UserResponseDto responseDto = new UserResponseDto();
         User user  = userService.getUserDetails(userId);
            responseDto.setId(user.getId());
            responseDto.setName(user.getName());
            responseDto.setEmail(user.getEmail());
            responseDto.setRoles(user.getRoles());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<UserResponseDto> setUserRoles(@PathVariable("id") long userId, @RequestBody SetUserRolesRequestDto requestDto) throws NotFoundException {
        UserResponseDto responseDto = new UserResponseDto();
        User user = userService.setUserRoles(userId, requestDto.getRoleIds());
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setRoles(user.getRoles());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }













}

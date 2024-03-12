package com.userService.UserService.services;

import com.userService.UserService.dto.*;
import com.userService.UserService.exceptions.NotFoundException;
import com.userService.UserService.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {

    public User getUserDetails(long userId) throws NotFoundException;

    public User setUserRoles(long userId, List<Long> roleIds) throws NotFoundException;



}

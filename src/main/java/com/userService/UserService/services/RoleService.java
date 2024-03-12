package com.userService.UserService.services;

import com.userService.UserService.models.Role;
import com.userService.UserService.repositories.RoleRepository;

public interface RoleService {
    public Role createRole(String name);

}

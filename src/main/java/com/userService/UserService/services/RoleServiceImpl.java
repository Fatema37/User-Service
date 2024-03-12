package com.userService.UserService.services;

import com.userService.UserService.models.Role;
import com.userService.UserService.repositories.RoleRepository;

public class RoleServiceImpl implements RoleService{
    private RoleRepository roleRepo;

    public RoleServiceImpl(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role createRole(String name) {
        Role role = new Role();
        role.setRole(name);
        roleRepo.save(role);
        return role;
    }
}

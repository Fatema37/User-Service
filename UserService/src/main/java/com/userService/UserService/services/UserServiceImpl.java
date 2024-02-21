package com.userService.UserService.services;

import com.userService.UserService.dto.*;
import com.userService.UserService.exceptions.NotFoundException;
import com.userService.UserService.models.Role;
import com.userService.UserService.models.User;
import com.userService.UserService.repositories.RoleRepository;
import com.userService.UserService.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 import org.apache.commons.text.RandomStringGenerator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private SignUpRequestDto signUpRequestDto;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepository roleRepo;


    @Override
    public User getUserDetails(long userId) throws NotFoundException {
        Optional<User> optionalUser = userRepo.findById(userId);
        if(optionalUser.isEmpty()){
            throw new NotFoundException("User is not found in db");
        }
        User user = optionalUser.get();
        return user;
    }

    public ValidateResponseDto validate(String email, String token) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        ValidateResponseDto validateResponseDto = new ValidateResponseDto();
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.getToken()!=null && user.getToken().equals(token)){
                validateResponseDto.setMessage("Valid token");
                validateResponseDto.setToken(user.getToken());

            }
            else {
                validateResponseDto.setMessage("Invalid token");
                validateResponseDto.setToken(token);
            }
        }
        else {
            validateResponseDto.setMessage("Invalid email");
            validateResponseDto.setToken(token);
        }
        return validateResponseDto;
    }

    @Override
    public User setUserRoles(long userId, List<Long> roleIds) throws NotFoundException {
        Optional<User> userOptional = userRepo.findById(userId);
        List<Role> roles = roleRepo.findAllByIdIn(roleIds);
        if(userOptional.isEmpty()){
          throw new NotFoundException("User is not found");
        }
        User user = userOptional.get();
        user.setRoles(Set.copyOf(roles));
        userRepo.save(user);
        return user;
    }

    private String generateRandomToken() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(Character::isLetterOrDigit)
                .build();

        return generator.generate(32); // Adjust the length as needed
    }
}

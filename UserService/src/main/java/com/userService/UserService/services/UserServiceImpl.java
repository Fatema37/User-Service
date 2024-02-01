package com.userService.UserService.services;

import com.userService.UserService.dto.*;
import com.userService.UserService.exceptions.NotFoundException;
import com.userService.UserService.models.User;
import com.userService.UserService.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 import org.apache.commons.text.RandomStringGenerator;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private SignUpRequestDto signUpRequestDto;
    @Autowired
    private UserRepo userRepo;

    @Override
    public User signUp(SignUpRequestDto signUpRequestDto) throws NotFoundException {
        Optional<User> optionalUser = userRepo.findByEmail(signUpRequestDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new NotFoundException("User already exists");
        }
        User user = new User();
        user.setName(signUpRequestDto.getName());
        user.setEmail(signUpRequestDto.getEmail());
        user.setPassword(signUpRequestDto.getPassword());
        String token = generateRandomToken();
        user.setToken(token);
        userRepo.save(user);
        return user;
    }

    @Override
    public User signIn(String email, String password) throws NotFoundException {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User user = optionalUser.get();
        if (!user.getEmail().equals(email)) {
            throw new NotFoundException("Invalid email");
        }
        if (!user.getPassword().equals(password)) {
            throw new NotFoundException("Invalid password");
        }
        String token = generateRandomToken();
        user.setToken(token);
        userRepo.save(user);

        return user;
    }

    @Override
    public LogOutResponseDto logout(LogOutRequestDto logOutRequestDto) {
        Optional<User> optionalUser = userRepo.findByEmail(logOutRequestDto.getEmail());
        LogOutResponseDto logOutResponseDto = new LogOutResponseDto();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getToken() != null && user.getToken().equals(logOutRequestDto.getToken())) {
                user.setToken(null);
                userRepo.save(user);
                logOutResponseDto.setMessage("Logged out successfully");
            } else {
                logOutResponseDto.setMessage("Invalid token for the user");

            }

        } else {
            logOutResponseDto.setMessage("Invalid email");
        }
        return logOutResponseDto;
    }

    @Override
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

    private String generateRandomToken() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(Character::isLetterOrDigit)
                .build();

        return generator.generate(32); // Adjust the length as needed
    }
}

package com.userService.UserService.Security;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.userService.UserService.models.User;
import com.userService.UserService.repositories.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@JsonDeserialize(as = CustomUserDetailsService.class)
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.findByEmail(username);
        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        return new CustomUserDetails(user);
    }
}

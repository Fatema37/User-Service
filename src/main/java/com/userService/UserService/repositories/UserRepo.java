package com.userService.UserService.repositories;

import com.userService.UserService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findById(long uuid );

    @Override
    User save(User user);

    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);

}

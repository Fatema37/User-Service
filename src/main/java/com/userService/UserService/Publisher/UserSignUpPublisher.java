package com.userService.UserService.Publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userService.UserService.Kafka.NotificationKafkaProducer;
import com.userService.UserService.models.User;
import com.userService.UserService.repositories.UserRepo;
import com.userService.UserService.services.AuthService;
import com.userService.UserService.services.AuthServiceImpl;
import com.userService.UserService.services.UserService;
import com.userService.UserService.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.kafka.core.KafkaTemplate;

@Component
public class UserSignUpPublisher {

    @Autowired
    private NotificationKafkaProducer kafkaProducer;

    private ObjectMapper objectMapper = new ObjectMapper();


    public void publishUserSignUpEvent(User user) {

        String message = user.getEmail() + "," + " Account created " + "," + "Hi " +user.getName()+" ,Your account is created successfully";
        kafkaProducer.sendNotification(message);
    }
}

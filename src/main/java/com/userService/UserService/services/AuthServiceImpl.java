package com.userService.UserService.services;

import com.userService.UserService.Publisher.UserSignUpPublisher;
import com.userService.UserService.dto.SignUpRequestDto;
import com.userService.UserService.exceptions.NotFoundException;
import com.userService.UserService.models.Session;
import com.userService.UserService.models.SessionStatus;
import com.userService.UserService.models.User;
import com.userService.UserService.repositories.SessionRepository;
import com.userService.UserService.repositories.UserRepo;
import io.jsonwebtoken.Claims;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService{
    private SignUpRequestDto signUpRequestDto;

    @Autowired
    private UserSignUpPublisher userSignUpPublisher;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SessionRepository sessionRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SecretKey secretKey;


//    public AuthServiceImpl( UserRepo userRepo, SessionRepository sessionRepo ,
//                            BCryptPasswordEncoder bCryptPasswordEncoder){
//        this.userRepo = userRepo;
//        this.sessionRepo = sessionRepo;
//        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        secretKey = Jwts.SIG.HS256.key().build();
//    }

    @Override
    public User signUp(SignUpRequestDto signUpRequestDto) throws NotFoundException {
        Optional<User> optionalUser = userRepo.findByEmail(signUpRequestDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new NotFoundException("User already exists");
        }
        User user = new User();
        user.setName(signUpRequestDto.getName());
        user.setEmail(signUpRequestDto.getEmail());

        user.setPassword(bCryptPasswordEncoder.encode(signUpRequestDto.getPassword()));
        String token = generateRandomToken();
        user.setToken(token);
        userRepo.save(user);
        // Publish UserSignUpEvent
        userSignUpPublisher.publishUserSignUpEvent(user);
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
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new NotFoundException("Invalid password");
        }
//        String token = generateRandomToken();
        Map<String, Object> jwtData = new HashMap<>();
        jwtData.put("email", email);
        jwtData.put("createdAt", new Date());
        jwtData.put("expiryAt", new Date(LocalDate.now().plusDays(3).toEpochDay()));
        String token = Jwts.builder().claims(jwtData).signWith(secretKey).compact();

        user.setToken(token);
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepo.save(session);

        return user;

    }
    @Override
    public void logout(String token, long  userId) {
        Optional<Session> sessionOptional = sessionRepo.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty()) {
            return;
        }
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepo.save(session);
    }
    @Override
    public SessionStatus validate(String token, long userId) {
        Optional<Session> sessionOptional = sessionRepo.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return SessionStatus.ENDED;
        }
        Session session = sessionOptional.get();

        if (!session.getSessionStatus().equals(SessionStatus.ACTIVE)) {
            return SessionStatus.ENDED;
        }
        Jws<Claims> claimsJws = Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

       String email = (String) claimsJws.getPayload().get("email");
        if(!session.getUser().getEmail().equals(email)){
            return  SessionStatus.ENDED;
        }
        Long expiryAt = ((Number) claimsJws.getPayload().get("expiryAt")).longValue();
        if(expiryAt < new Date().getTime()){
            return SessionStatus.ENDED;
       }

        return SessionStatus.ACTIVE;

    }

    private String generateRandomToken() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(Character::isLetterOrDigit)
                .build();

        return generator.generate(32); // Adjust the length as needed
    }
}

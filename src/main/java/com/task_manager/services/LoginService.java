package com.task_manager.services;

import com.task_manager.configurations.GmailConfig;
import com.task_manager.configurations.JwtService;
import com.task_manager.entities.RandomFourDigits;
import com.task_manager.entities.User;
import com.task_manager.exceptions.CodeHasExpiredException;
import com.task_manager.exceptions.InvalidCredentialsException;
import com.task_manager.exceptions.InvalidProvidedInfoException;
import com.task_manager.exceptions.ResourceNotFoundException;
import com.task_manager.repositories.RandomFourDigitsRepo;
import com.task_manager.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LoginService {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final GmailConfig gmailConfig;

    private final UserRepo userRepo;

    private final RandomFourDigitsRepo randomFourDigitsRepo;

    private final RegisterService registerService;

    @Autowired
    public LoginService(
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            GmailConfig gmailConfig,
            UserRepo userRepo,
            RandomFourDigitsRepo randomFourDigitsRepo,
            RegisterService registerService
    ) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.gmailConfig = gmailConfig;
        this.userRepo = userRepo;
        this.randomFourDigitsRepo = randomFourDigitsRepo;
        this.registerService = registerService;
    }

    public String login(String username, String password) throws NoSuchAlgorithmException {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
        );

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(username);
        }

        throw new InvalidCredentialsException("bad credentials");
    }

    public void sendEmailVerification(String email) throws Exception {
        int randomFourDigit = (int) (Math.random() * 9000) + 1000;

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("user not found");
        }

        String token = UUID.randomUUID().toString();

        gmailConfig.setUserEmail(email);
        gmailConfig.sendMail("Password Reset", "Hey, " +
                "\n\nYour 4 digit code is: " + randomFourDigit + ". It expires in 5 minutes." +
                "\nTo continue click on this link: http://localhost:8000/password/reset/" + token +
                "\nIf you haven't requested a password change you can safely ignore this message." +
                "\n\nBest Regards, \nThe Task Manager Team."
        );

        RandomFourDigits randomFourDigits = new RandomFourDigits();
        randomFourDigits.setNum(randomFourDigit);
        randomFourDigits.setSentTime(LocalDateTime.now());
        randomFourDigits.setSentTo(user);
        randomFourDigits.setToken(token);

        randomFourDigitsRepo.save(randomFourDigits);
    }

    public String resetPassword(String token, String password, Integer randomNum) {
        RandomFourDigits randomFourDigits = randomFourDigitsRepo.findByToken(token);

        User user = userRepo.findById(randomFourDigits.getSentTo().getUserId())
                .orElseThrow(() -> new NullPointerException("user not found"));

        if (
                Duration.between(randomFourDigits.getSentTime(), LocalDateTime.now()).toMinutes() >= 5
        ) {
            randomFourDigitsRepo.delete(randomFourDigits);
            throw new CodeHasExpiredException("this code has expired");
        }

        if (!registerService.isPasswordValid(password)) {
            throw new InvalidProvidedInfoException("invalid password");
        }

        if (!randomNum.equals(randomFourDigits.getNum())) {
            throw new InvalidProvidedInfoException("number is incorrect");
        }

        randomFourDigitsRepo.delete(randomFourDigits);

        String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        user.setPassword(pw_hash);

        userRepo.save(user);

        return "password has been changed";
    }
}

package com.task_manager.services;

import com.task_manager.entities.User;
import com.task_manager.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegisterService {

    private final UserRepo userRepo;

    @Autowired
    public RegisterService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User register(User user) throws NoSuchAlgorithmException {
        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("email is already in use");
        }

        if (userRepo.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("name is already in use");
        }

        if (!isPasswordValid(user.getPassword())) {
            throw new RuntimeException("password is invalid");
        }

        if (user.getUsername() == null) {
            throw new RuntimeException("username must be specified");
        }
        String pw_hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(pw_hash);

        return userRepo.save(user);
    }

    public boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            throw new RuntimeException("password must be at least 6 characters long");
        }

        Pattern numberPattern = Pattern.compile("[0-9]");
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Pattern lowercasePattern = Pattern.compile("[a-z]");
        Pattern specialCharacterPattern = Pattern.compile("[$&+,:;=?@#|'<>.-^*()%!]");

        Matcher number = numberPattern.matcher(password);
        Matcher uppercase = uppercasePattern.matcher(password);
        Matcher lowercase = lowercasePattern.matcher(password);
        Matcher specialCharacter = specialCharacterPattern.matcher(password);

        return number.find() && uppercase.find() && lowercase.find() && specialCharacter.find();
    }
}

package com.task_manager.controllers;

import com.task_manager.models.EmailDto;
import com.task_manager.models.ForgotPasswordDto;
import com.task_manager.models.PasswordDto;
import com.task_manager.models.UserDto;
import com.task_manager.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.security.NoSuchAlgorithmException;

@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(
                loginService.login(userDto.getUsername(), userDto.getPassword())
        );
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailDto emailDto) throws Exception {
        loginService.sendEmailVerification(emailDto.getEmail());
        return ResponseEntity.ok(
                new RedirectView("/password/reset")
        );
    }

    @PutMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return ResponseEntity.ok(
                loginService.resetPassword(
                        forgotPasswordDto.getEmail(),
                        forgotPasswordDto.getPassword(),
                        forgotPasswordDto.getNum()
                )
        );

    }
}

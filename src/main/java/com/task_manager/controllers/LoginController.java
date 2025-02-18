package com.task_manager.controllers;

import com.task_manager.models.EmailDto;
import com.task_manager.models.ForgotPasswordDto;
import com.task_manager.models.UserDto;
import com.task_manager.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(
                loginService.login(userDto.getUsername(), userDto.getPassword())
        );
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailDto emailDto) throws Exception {
        loginService.sendEmailVerification(emailDto.getEmail());
        return ResponseEntity.ok(
                "email verification sent"
        );
    }

    @PutMapping("/password/reset/{token}")
    public ResponseEntity<String> resetPassword(
            @RequestBody ForgotPasswordDto forgotPasswordDto,
            @PathVariable String token
    ) throws Exception {

        return ResponseEntity.ok(
                loginService.resetPassword(
                        token,
                        forgotPasswordDto.getPassword(),
                        forgotPasswordDto.getNum()
                )
        );

    }
}

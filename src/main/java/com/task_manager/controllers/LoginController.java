package com.task_manager.controllers;

import com.task_manager.models.UserDto;
import com.task_manager.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}

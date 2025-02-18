package com.task_manager.controllers;

import com.task_manager.UserFactory;
import com.task_manager.converters.UserConverter;
import com.task_manager.entities.*;
import com.task_manager.models.UserDto;
import com.task_manager.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
public class RegisterController {

    private final RegisterService registerService;

    private final UserConverter userConverter;

    @Autowired
    public RegisterController(RegisterService registerService, UserConverter userConverter) {
        this.registerService = registerService;
        this.userConverter = userConverter;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        User user = new UserFactory().getUser(userDto.getRole());

        return new ResponseEntity<>(
                registerService.register(
                        userConverter.convertToEntity(user, userDto)
                ), HttpStatus.CREATED
        );
    }
}

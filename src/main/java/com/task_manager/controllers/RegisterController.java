package com.task_manager.controllers;

import com.task_manager.converters.UserConverter;
import com.task_manager.entities.*;
import com.task_manager.models.UserDto;
import com.task_manager.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> register(@RequestBody UserDto userDto) throws NoSuchAlgorithmException {
        User user = null;
        switch (userDto.getRole()) {
            case Role.CUSTOMER -> {
                user = new Customer();
                break;
            }

            case Role.ADMIN ->
                throw new RuntimeException("access denied");

            case Role.EMPLOYEE -> {
                user = new Employee();
                break;
            }

            case Role.TEAM_LEAD -> {
                user = new TeamLead();
                break;
            }
        }

        return ResponseEntity.ok(
                registerService.register(
                        userConverter.convertToEntity(user, userDto)
                )
        );
    }
}

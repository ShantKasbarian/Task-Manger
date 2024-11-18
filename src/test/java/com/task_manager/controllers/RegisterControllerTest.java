package com.task_manager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.configurations.JwtFilter;
import com.task_manager.configurations.JwtService;
import com.task_manager.converters.UserConverter;
import com.task_manager.entities.Customer;
import com.task_manager.entities.Role;
import com.task_manager.entities.User;
import com.task_manager.models.UserDto;
import com.task_manager.services.RegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(RegisterController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class RegisterControllerTest {
    @MockBean
    private RegisterService registerService;

    @MockBean
    private UserConverter userConverter;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new Customer();
        user.setUserId(1L);
        user.setUsername("John");
        user.setEmail("john@example.com");
        user.setPassword("Password123+");
        user.setRole(Role.CUSTOMER);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("John");
        userDto.setEmail("john@example.com");
        userDto.setPassword("Password123+");
        userDto.setRole(Role.CUSTOMER);
    }

    @Test
    void register() throws Exception {
        mockMvc.perform(post("/register")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
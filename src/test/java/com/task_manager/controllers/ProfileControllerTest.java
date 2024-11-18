package com.task_manager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.configurations.JwtFilter;
import com.task_manager.configurations.JwtService;
import com.task_manager.entities.Customer;
import com.task_manager.entities.Role;
import com.task_manager.entities.User;
import com.task_manager.models.UserDto;
import com.task_manager.services.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ProfileController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class ProfileControllerTest {
    @MockBean
    private ProfileService profileService;

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
    void getProfile() throws Exception {
        ResultActions response = mockMvc.perform(get("/profile")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername())
                        .password(user.getPassword()).roles(user.getRole().toString()))
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
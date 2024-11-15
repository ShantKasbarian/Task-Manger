package com.task_manager.services;

import com.task_manager.entities.Customer;
import com.task_manager.entities.Role;
import com.task_manager.entities.User;
import com.task_manager.repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RegisterServiceTest {
    @InjectMocks
    private RegisterService registerService;

    @Mock
    private UserRepo userRepo;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new Customer();

        user.setUserId(1L);
        user.setUsername("John");
        user.setEmail("johnDoe@example.com");
        user.setPassword("Password123+");
        user.setRole(Role.CUSTOMER);
    }

    @Test
    void register() throws NoSuchAlgorithmException {
        when(userRepo.save(user)).thenReturn(user);

        User response = registerService.register(user);

        assertEquals(user.getUserId(), response.getUserId());
        assertEquals(user.getUsername(), response.getUsername());
    }

    @Test
    void registerShouldReturnEmailIsAlreadyInUse() {

    }

    @Test
    void registerShouldReturnInvalidPassword() throws NoSuchAlgorithmException {
        user.setPassword("123");

        assertThrows(RuntimeException.class, () -> registerService.register(user));
    }

    @Test
    void registerShouldReturnUsernameMustBeSpecified() {
        user.setUsername(null);

        assertThrows(RuntimeException.class, () -> registerService.register(user));
    }
}
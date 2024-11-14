package com.task_manager.services;

import com.task_manager.converters.EmployeeConverter;
import com.task_manager.converters.TeamLeadConverter;
import com.task_manager.converters.UserConverter;
import com.task_manager.entities.Customer;
import com.task_manager.entities.Role;
import com.task_manager.entities.User;
import com.task_manager.models.CustomerDto;
import com.task_manager.models.UserDto;
import com.task_manager.repositories.AdminRepo;
import com.task_manager.repositories.CustomerRepo;
import com.task_manager.repositories.EmployeeRepo;
import com.task_manager.repositories.TeamLeadRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProfileServiceTest {
    @InjectMocks
    private ProfileService profileService;

    @Mock
    private EmployeeRepo employeeRepo;

    @Mock
    private AdminRepo adminRepo;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private TeamLeadRepo teamLeadRepo;

    @Mock
    private EmployeeConverter employeeConverter;

    @Mock
    private UserConverter userConverter;

    @Mock
    private TeamLeadConverter teamLeadConverter;

    private UserDto customerDto;

    private User customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerDto = new UserDto();
        customerDto.setId(1L);
        customerDto.setUsername("John");
        customerDto.setEmail("johnDoe@example.com");
        customerDto.setPassword("Password123+");
        customerDto.setRole(Role.CUSTOMER);

        customer = new Customer();
        customer.setUserId(1L);
        customer.setUsername("John");
        customer.setEmail("johnDoe@example.com");
        customer.setPassword("Password123+");
        customer.setRole(Role.CUSTOMER);
    }

    @Test
    void viewProfile() {
        when(userConverter.convertToModel(customer, new UserDto()))
                .thenReturn(customerDto);

        ResponseEntity<?> responseEntity = profileService.viewProfile(customer);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
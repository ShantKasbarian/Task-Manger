package com.task_manager.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.configurations.JwtFilter;
import com.task_manager.configurations.JwtService;
import com.task_manager.converters.RequestConverter;
import com.task_manager.entities.Customer;
import com.task_manager.entities.Request;
import com.task_manager.entities.Role;
import com.task_manager.entities.User;
import com.task_manager.models.PageDto;
import com.task_manager.models.RequestDto;
import com.task_manager.repositories.RequestRepo;
import com.task_manager.services.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(RequestController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class RequestControllerTest {
    @MockBean
    private RequestService requestService;

    @MockBean
    private RequestConverter requestConverter;

    @MockBean
    private RequestRepo requestRepo;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer user;

    private Request request;

    private RequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = new Customer();
        user.setUserId(1L);
        user.setUsername("John");
        user.setEmail("john@example.com");
        user.setPassword("Password123+");
        user.setRole(Role.CUSTOMER);

        request = new Request();
        request.setRequestId(1L);
        request.setTitle("some title");
        request.setDescription("huifheiubwf wiohdiupdhq dqhiuqhbdib qdiqdniudniubq qdbduibdiuqbdidnqubn ");
        requestDto = new RequestDto();
        requestDto.setRequestId(1L);
        requestDto.setTitle("some title");
        requestDto.setDescription(request.getDescription());
    }

    @Test
    void createRequest() throws Exception {
        request.setCustomer(user);

        when(requestService.createRequest(any())).thenReturn(request);
        when(requestConverter.convertToModel(request, new RequestDto())).thenReturn(requestDto);

        mockMvc.perform(post("/request/create")
                .with(jwt())
                .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername())
                        .password(user.getPassword()).roles(Role.CUSTOMER.toString())
                )
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void getRequestById() throws Exception {
        mockMvc.perform(get("/request/1")
                .with(jwt())
                .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername())
                        .password(user.getPassword()).roles(String.valueOf(user.getRole()))
                )
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateRequest() throws Exception {
        mockMvc.perform(put("/request/update")
                .with(jwt())
                .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername())
                        .password(user.getPassword()).roles(String.valueOf(user.getRole()))
                )
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllCustomerRequests() throws Exception {
        List<RequestDto> requests = new ArrayList<>();
        requests.add(new RequestDto());
        requests.add(new RequestDto());
        requests.add(new RequestDto());

        Page<RequestDto> page = new PageImpl<>(requests);
        Pageable pageable = PageRequest.of (0, 10);
        PageDto<RequestDto> pageDto = new PageDto<>(page);

        when(requestService.getAllRequestsByCustomer((Customer) user, pageable)).thenReturn(pageDto);

        mockMvc.perform(put("/request/all")
                .with(jwt())
                .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername())
                        .password(user.getPassword()).roles(String.valueOf(user.getRole()))
                )
                .content(objectMapper.writeValueAsString(pageDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteRequest() throws Exception {
        mockMvc.perform(delete("/request/1/delete")
                .with(jwt())
                .with(SecurityMockMvcRequestPostProcessors.user(user.getUsername())
                        .password(user.getPassword()).roles(String.valueOf(user.getRole()))
                )
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllPendingRequests() {

    }

    @Test
    void takeRequest() {
    }

    @Test
    void changeStatus() {
    }

    @Test
    void rejectRequest() {
    }

    @Test
    void acceptRequest() {
    }

    @Test
    void getAllAcceptedRequests() {
    }

    @Test
    void getRequestsByTeam() {
    }
}
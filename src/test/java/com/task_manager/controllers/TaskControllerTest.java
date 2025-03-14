package com.task_manager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.configurations.JwtFilter;
import com.task_manager.configurations.JwtService;
import com.task_manager.converters.TaskConverter;
import com.task_manager.entities.Role;
import com.task_manager.entities.TaskStatus;
import com.task_manager.models.TaskDto;
import com.task_manager.repositories.RequestRepo;
import com.task_manager.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(TaskController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class TaskControllerTest {

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskConverter converter;

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

    private TaskDto task;

    @BeforeEach
    void setUp() {
        task = new TaskDto();
        task.setId(1L);
        task.setTaskStatus(TaskStatus.PENDING);
        task.setTitle("some title");
        task.setDescription("cmosmcpomcpa cnaicncos ccioamcopm mcoam");
        task.setDeadline(LocalDateTime.of(2025, 03, 01, 01, 00));
    }

    @Test
    void createTask() throws Exception {
        mockMvc.perform(post("/task/create?employeeId=1")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .with(SecurityMockMvcRequestPostProcessors.user("user")
                .password("password").roles(Role.TEAM_LEAD.toString()))
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void getTaskById() throws Exception {
        mockMvc.perform(get("/task/1")
        .with(SecurityMockMvcRequestPostProcessors.jwt())
                .with(
                        SecurityMockMvcRequestPostProcessors.user("user")
                        .password("password").roles(Role.TEAM_LEAD.toString())
                )
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void markAsDone() throws Exception {
        mockMvc.perform(put("/task/1/done")
            .with(SecurityMockMvcRequestPostProcessors.jwt())
            .with(
                    SecurityMockMvcRequestPostProcessors.user("user")
                    .password("password").roles(Role.EMPLOYEE.toString())
            )
            .content(objectMapper.writeValueAsString(task))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateTask() throws Exception {
        mockMvc.perform(put("/task/update")
            .with(SecurityMockMvcRequestPostProcessors.jwt())
            .with(
                    SecurityMockMvcRequestPostProcessors.user("user")
                    .password("password").roles(Role.TEAM_LEAD.toString())
            )
            .content(objectMapper.writeValueAsString(task))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/task/1")
                .with(SecurityMockMvcRequestPostProcessors.jwt())
                .with(
                        SecurityMockMvcRequestPostProcessors.user("user")
                        .password("password").roles(Role.TEAM_LEAD.toString())
                )
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void getTasksByEmployee() throws Exception {
        mockMvc.perform(get("/task/my")
            .with(
                SecurityMockMvcRequestPostProcessors.user("user")
                .password("password").roles(Role.EMPLOYEE.toString())
            )
        )
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getOverdueTasks() throws Exception {
        mockMvc.perform(get("/task/overdue")
                .with(
                        SecurityMockMvcRequestPostProcessors.user("user")
                        .password("password").roles(Role.EMPLOYEE.toString())
                )
        )
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getNonOverdueTasks() throws Exception {
        mockMvc.perform(get("/task/non/overdue")
            .with(
                    SecurityMockMvcRequestPostProcessors.user("user")
                    .password("password").roles(Role.EMPLOYEE.toString())
            )
        )
        .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
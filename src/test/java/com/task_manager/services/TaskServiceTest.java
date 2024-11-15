package com.task_manager.services;

import com.task_manager.converters.TaskConverter;
import com.task_manager.entities.Employee;
import com.task_manager.entities.Task;
import com.task_manager.entities.TaskStatus;
import com.task_manager.entities.Team;
import com.task_manager.models.PageDto;
import com.task_manager.models.TaskDto;
import com.task_manager.repositories.EmployeeRepo;
import com.task_manager.repositories.TaskRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepo taskRepo;

    @Mock
    private TaskConverter taskConverter;

    @Mock
    private EmployeeRepo employeeRepo;

    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        String dateTimeString = "01/12/2024 13:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        task = new Task();
        task.setTaskId(1L);
        task.setTitle("some title");
        task.setDescription("cubhb cwnciuwbnciubwncuwb wnucnwiu mwmoimc");
        task.setAssignedTime(LocalDateTime.now());
        task.setEmployee(new Employee());
        task.getEmployee().setUserId(1L);
        task.getEmployee().setTeam(new Team());
        task.getEmployee().getTeam().setTeamId(1L);
        task.setDeadline(dateTime);
    }

    @Test
    void createTask() {
        when(taskRepo.save(task)).thenReturn(task);
        when(employeeRepo.findById(1L)).thenReturn(Optional.ofNullable(task.getEmployee()));

        Task response = taskService.createTask(task, 1L, 1L);

        assertEquals(task.getEmployee(), response.getEmployee());
        assertEquals(TaskStatus.PENDING, response.getTaskStatus());
    }

    @Test
    void getTaskById() {
        when(taskRepo.findById(1L)).thenReturn(Optional.ofNullable(task));

        Task response = taskService.getTaskById(1L);

        assertEquals(task, response);
    }

    @Test
    void markAsDone() {
        when(taskRepo.findById(1L)).thenReturn(Optional.ofNullable(task));
        when(taskRepo.save(task)).thenReturn(task);

        Task response = taskService.markAsDone(1L, 1L);

        assertEquals(TaskStatus.DONE, response.getTaskStatus());
    }

    @Test
    void updateTask() {
        when(taskRepo.existsById(1L)).thenReturn(true);
        when(taskRepo.save(task)).thenReturn(task);

        Task response = taskService.updateTask(task, 1L);

        assertEquals(task.getEmployee(), response.getEmployee());
    }

    @Test
    void deleteTask() {
        when(taskRepo.findById(1L)).thenReturn(Optional.ofNullable(task));

        String response = taskService.deleteTask(1L, 1L);

        assertEquals("task has been deleted", response);
    }

    @Test
    void getTasksByEmployee() {
        task.getEmployee().setTasks(new ArrayList<>());
        task.getEmployee().getTasks().add(new Task());
        task.getEmployee().getTasks().add(new Task());

        Page<Task> page = new PageImpl<>(task.getEmployee().getTasks());
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepo.getTasksByEmployee(task.getEmployee(), pageable)).thenReturn(page);
        when(taskConverter.convertToModel(
                task.getEmployee().getTasks().get(0), new TaskDto()))
                .thenReturn(new TaskDto());
        when(taskConverter.convertToModel(
                task.getEmployee().getTasks().get(1), new TaskDto()))
                .thenReturn(new TaskDto());

        PageDto<TaskDto> response = taskService.getTasksByEmployee(task.getEmployee(), pageable);

        assertEquals(task.getEmployee().getTasks().size(), response.getContent().size());
    }

    @Test
    void getOverdueTasksByEmployee() {
        String dateTimeString = "01/11/2024 13:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        task.getEmployee().setTasks(new ArrayList<>());
        task.getEmployee().getTasks().add(new Task());
        task.getEmployee().getTasks().get(0).setDeadline(dateTime);
        task.getEmployee().getTasks().add(new Task());
        task.getEmployee().getTasks().get(1).setDeadline(dateTime);

        Page<Task> page = new PageImpl<>(task.getEmployee().getTasks());
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepo.getTasksByEmployee(task.getEmployee(), pageable)).thenReturn(page);
        when(taskConverter.convertToModel(
                task.getEmployee().getTasks().get(0), new TaskDto()))
                .thenReturn(new TaskDto());
        when(taskConverter.convertToModel(
                task.getEmployee().getTasks().get(1), new TaskDto()))
                .thenReturn(new TaskDto());

        PageDto<TaskDto> response = taskService.getOverdueTasksByEmployee(task.getEmployee(), pageable);

        assertEquals(task.getEmployee().getTasks().size(), response.getContent().size());
    }

    @Test
    void getNonOverdueTasksByEmployee() {
        String dateTimeString = "01/12/2024 13:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        task.getEmployee().setTasks(new ArrayList<>());
        task.getEmployee().getTasks().add(new Task());
        task.getEmployee().getTasks().get(0).setDeadline(dateTime);
        task.getEmployee().getTasks().add(new Task());
        task.getEmployee().getTasks().get(1).setDeadline(dateTime);

        Page<Task> page = new PageImpl<>(task.getEmployee().getTasks());
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepo.getTasksByEmployee(task.getEmployee(), pageable)).thenReturn(page);
        when(taskConverter.convertToModel(
                task.getEmployee().getTasks().get(0), new TaskDto()))
                .thenReturn(new TaskDto());
        when(taskConverter.convertToModel(
                task.getEmployee().getTasks().get(1), new TaskDto()))
                .thenReturn(new TaskDto());

        PageDto<TaskDto> response = taskService.getNonOverdueTasksByEmployee(task.getEmployee(), pageable);

        assertEquals(task.getEmployee().getTasks().size(), response.getContent().size());
    }
}
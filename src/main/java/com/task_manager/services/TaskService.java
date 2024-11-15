package com.task_manager.services;

import com.task_manager.converters.TaskConverter;
import com.task_manager.entities.Employee;
import com.task_manager.entities.Task;
import com.task_manager.entities.TaskStatus;
import com.task_manager.models.PageDto;
import com.task_manager.models.TaskDto;
import com.task_manager.repositories.EmployeeRepo;
import com.task_manager.repositories.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    private final TaskConverter taskConverter;

    private final EmployeeRepo employeeRepo;

    @Autowired
    public TaskService(
            TaskRepo taskRepo,
            TaskConverter taskConverter,
            EmployeeRepo employeeRepo
    ) {
        this.taskRepo = taskRepo;
        this.taskConverter = taskConverter;
        this.employeeRepo = employeeRepo;
    }

    public Task createTask(
            Task task,
            Long employeeId,
            Long teamId
    ) {

        if (
                task.getDescription() == null ||
                task.getDescription().trim().length() < 20
        ) {
            throw new RuntimeException("task description must be greater than 20 characters");
        }

        if (task.getTitle() == null) {
            throw new NullPointerException("title must be specified");
        }

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new NullPointerException("employee not found"));

        if (!employee.getTeam().getTeamId().equals(teamId)) {
            throw new RuntimeException("cannot assign a task to an employee from another team");
        }

        task.setTaskStatus(TaskStatus.PENDING);
        task.setAssignedTime(LocalDateTime.now());
        task.setEmployee(employee);

        return taskRepo.save(task);
    }

    public Task getTaskById(Long taskId) {
        return taskRepo.findById(taskId)
                .orElseThrow(() -> new NullPointerException("task not found"));
    }

    public Task markAsDone(Long taskId, Long employeeId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NullPointerException("task not found"));

        if (!task.getEmployee().getUserId().equals(employeeId)) {
            throw new RuntimeException("cannot mark another employee's task as done");
        }

        task.setTaskStatus(TaskStatus.DONE);
        task.setEndTime(LocalDateTime.now());

        return taskRepo.save(task);
    }

    public Task updateTask(Task task, Long teamId) {
        if (!taskRepo.existsById(task.getTaskId())) {
            throw new NullPointerException("task not found");
        }

        if (
                task.getDescription() == null ||
                        task.getDescription().trim().length() < 20
        ) {
            throw new RuntimeException("task description must be greater than 20 characters");
        }

        if (task.getTitle() == null) {
            throw new NullPointerException("title must be specified");
        }

        if (!task.getEmployee().getTeam().getTeamId().equals(teamId)) {
            throw new RuntimeException("cannot update an employee's task from another team");
        }

        return taskRepo.save(task);
    }

    public String deleteTask(Long taskId, Long teamId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NullPointerException("task not found"));

        if (!task.getEmployee().getTeam().getTeamId().equals(teamId)) {
            throw new RuntimeException("cannot delete a task from another team");
        }

        taskRepo.delete(task);

        return "task has been deleted";
    }

    public PageDto<TaskDto> getTasksByEmployee(Employee employee, Pageable pageable) {
        Page<Task> page = taskRepo.getTasksByEmployee(employee, pageable);

        List<TaskDto> tasks = page.stream()
                .map(task -> taskConverter.convertToModel(task, new TaskDto()))
                .toList();

        PageDto<TaskDto> pageDto = new PageDto<>();
        pageDto.setContent(tasks);
        pageDto.setPageNo(pageable.getPageSize());
        pageDto.setPageSize(page.getSize());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setEmpty(page.isEmpty());

        return pageDto;
    }

    public PageDto<TaskDto> getOverdueTasksByEmployee(Employee employee, Pageable pageable) {
        Page<Task> page = taskRepo.getTasksByEmployee(employee, pageable);

        List<TaskDto> tasks = page.stream()
                .filter(task -> task.getDeadline().isBefore(LocalDateTime.now()))
                .map(task -> taskConverter.convertToModel(task, new TaskDto()))
                .collect(Collectors.toList());

        PageDto<TaskDto> pageDto = new PageDto<>();
        pageDto.setContent(tasks);
        pageDto.setPageNo(pageable.getPageSize());
        pageDto.setPageSize(page.getSize());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setEmpty(page.isEmpty());

        return pageDto;
    }

    public PageDto<TaskDto> getNonOverdueTasksByEmployee(Employee employee, Pageable pageable) {
        Page<Task> page = taskRepo.getTasksByEmployee(employee, pageable);

        List<TaskDto> tasks = page.stream()
                .filter(taskDto -> taskDto.getDeadline().isAfter(LocalDateTime.now()))
                .map(task -> taskConverter.convertToModel(task, new TaskDto()))
                .collect(Collectors.toList());

        PageDto<TaskDto> pageDto = new PageDto<>();
        pageDto.setContent(tasks);
        pageDto.setPageNo(pageable.getPageSize());
        pageDto.setPageSize(page.getSize());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setEmpty(page.isEmpty());

        return pageDto;
    }
}

package com.task_manager.controllers;

import com.task_manager.converters.TaskConverter;
import com.task_manager.entities.Employee;
import com.task_manager.entities.Task;
import com.task_manager.entities.TeamLead;
import com.task_manager.models.PageDto;
import com.task_manager.models.TaskDto;
import com.task_manager.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    private final TaskConverter taskConverter;

    @Autowired
    public TaskController(TaskService taskService, TaskConverter taskConverter) {
        this.taskService = taskService;
        this.taskConverter = taskConverter;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('TEAM_LEAD')")
    public ResponseEntity<TaskDto> createTask(
            @RequestBody TaskDto taskDto,
            @RequestParam Long employeeId,
            Authentication authentication
    ) {
        TeamLead teamLead = (TeamLead) authentication.getPrincipal();
        Task task = taskConverter.convertToEntity(new Task(), taskDto);
        return new ResponseEntity<>(
                taskConverter.convertToModel(
                        taskService.createTask(task, employeeId, teamLead.getTeam().getTeamId()),
                        new TaskDto()
                ), HttpStatus.CREATED
        );
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('TEAM_LEAD')")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(
                taskConverter.convertToModel(
                        taskService.getTaskById(taskId), new TaskDto()
                )
        );
    }

    @PutMapping("/{taskId}/done")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<TaskDto> markAsDone(
            Authentication authentication,
            @PathVariable Long taskId
    ) {
        Employee employee = (Employee) authentication.getPrincipal();
        return ResponseEntity.ok(
                taskConverter.convertToModel(
                        taskService.markAsDone(taskId, employee.getUserId()),
                        new TaskDto()
                )
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('TEAM_LEAD')")
    public ResponseEntity<TaskDto> updateTask(
            @RequestBody TaskDto taskDto,
            Authentication authentication
    ) {
        TeamLead teamLead = (TeamLead) authentication.getPrincipal();

        Task task = taskConverter.convertToEntity(new Task(), taskDto);

        return ResponseEntity.ok(
                taskConverter.convertToModel(
                        taskService.updateTask(task, teamLead.getTeam().getTeamId()),
                        new TaskDto()
                )
        );
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('TEAM_LEAD')")
    public ResponseEntity<?> deleteTask(
            Authentication authentication,
            @PathVariable Long taskId
    ) {
        TeamLead teamLead = (TeamLead) authentication.getPrincipal();

        return new ResponseEntity<>(
                taskService.deleteTask(taskId,teamLead.getTeam().getTeamId()),
                HttpStatus.NO_CONTENT
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PageDto<TaskDto>> getTasksByEmployee(
            Authentication authentication,
            @RequestParam(value = "num", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {

        Employee employee = (Employee) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(pageNum, size);

        return ResponseEntity.ok(taskService.getTasksByEmployee(employee, pageable));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PageDto<TaskDto>> getOverdueTasks(
            Authentication authentication,
            @RequestParam(value = "num", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        Employee employee = (Employee) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(pageNum, size);

        return ResponseEntity.ok(
                taskService.getOverdueTasksByEmployee(employee, pageable)
        );
    }

    @GetMapping("/non/overdue")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<PageDto<TaskDto>> getNonOverdueTasks(
            Authentication authentication,
            @RequestParam(value = "num", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        Employee employee = (Employee) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(pageNum, size);

        return ResponseEntity.ok(
                taskService.getNonOverdueTasksByEmployee(employee, pageable)
        );
    }


}

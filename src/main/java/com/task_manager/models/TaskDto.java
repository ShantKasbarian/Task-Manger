package com.task_manager.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.task_manager.entities.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private Long id;

    private String title;

    private String Description;

    private TaskStatus taskStatus;

    private LocalDateTime assignedTime;

    private LocalDateTime endTime;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime deadline;
}

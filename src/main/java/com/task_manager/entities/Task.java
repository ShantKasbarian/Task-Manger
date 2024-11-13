package com.task_manager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus taskStatus;

    @Column(name = "assigned_time")
    private LocalDateTime assignedTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "deadline")
    private LocalDateTime deadline;
}

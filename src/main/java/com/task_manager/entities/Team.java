package com.task_manager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "name", unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TeamStatus teamStatus;

    @OneToMany(mappedBy = "team")
    private List<Employee> employees;

    @OneToMany(mappedBy = "team")
    private List<Request> requests;
}

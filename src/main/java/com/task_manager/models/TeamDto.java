package com.task_manager.models;

import com.task_manager.entities.TeamStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {

    private Long teamId;

    private String name;

    private TeamStatus teamStatus;
}

package com.task_manager.converters;

import com.task_manager.entities.Team;
import com.task_manager.models.TeamDto;
import org.springframework.stereotype.Component;

@Component
public class TeamConverter implements Converter<Team, TeamDto>{
    @Override
    public Team convertToEntity(Team entity, TeamDto model) {
        entity.setTeamId(model.getTeamId());
        entity.setName(model.getName());
        entity.setTeamStatus(model.getTeamStatus());
        return entity;
    }

    @Override
    public TeamDto convertToModel(Team entity, TeamDto model) {
        model.setTeamId(entity.getTeamId());
        model.setName(entity.getName());
        model.setTeamStatus(model.getTeamStatus());
        return model;
    }
}

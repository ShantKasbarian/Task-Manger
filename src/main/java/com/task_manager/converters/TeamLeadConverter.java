package com.task_manager.converters;

import com.task_manager.entities.Team;
import com.task_manager.entities.TeamLead;
import com.task_manager.models.TeamDto;
import com.task_manager.models.TeamLeadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamLeadConverter implements Converter<TeamLead, TeamLeadDto> {

    private final TeamConverter teamConverter;

    @Autowired
    public TeamLeadConverter(TeamConverter teamConverter) {
        this.teamConverter = teamConverter;
    }

    @Override
    public TeamLead convertToEntity(TeamLead entity, TeamLeadDto model) {
        entity.setUserId(model.getId());
        entity.setUsername(model.getUsername());
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());
        entity.setRole(model.getRole());

        if (model.getTeamDto() != null) {
            entity.setTeam(teamConverter.convertToEntity(new Team(), model.getTeamDto()));
        }

        return entity;
    }

    @Override
    public TeamLeadDto convertToModel(TeamLead entity, TeamLeadDto model) {
        model.setId(entity.getUserId());
        model.setUsername(entity.getUsername());
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPassword());
        model.setRole(entity.getRole());

        if (entity.getTeam() != null) {
            model.setTeamDto(teamConverter.convertToModel(entity.getTeam(), new TeamDto()));
        }
        return model;
    }
}

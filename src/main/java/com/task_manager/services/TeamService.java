package com.task_manager.services;

import com.task_manager.converters.TeamConverter;
import com.task_manager.entities.Employee;
import com.task_manager.entities.Team;
import com.task_manager.entities.TeamLead;
import com.task_manager.entities.TeamStatus;
import com.task_manager.models.PageDto;
import com.task_manager.models.TeamDto;
import com.task_manager.repositories.EmployeeRepo;
import com.task_manager.repositories.TeamLeadRepo;
import com.task_manager.repositories.TeamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepo teamRepo;

    private final TeamConverter teamConverter;

    private final TeamLeadRepo teamLeadRepo;

    private final EmployeeRepo employeeRepo;

    @Autowired
    public TeamService(
            TeamRepo teamRepo,
            TeamConverter teamConverter,
            TeamLeadRepo teamLeadRepo,
            EmployeeRepo employeeRepo
    ) {
        this.teamRepo = teamRepo;
        this.teamConverter = teamConverter;
        this.teamLeadRepo = teamLeadRepo;
        this.employeeRepo = employeeRepo;
    }

    public Team createTeam(Team team, TeamLead teamLead) {
        if (team.getName() == null) {
            throw new NullPointerException("team name must be specified");
        }

        if (teamLead.getTeam() != null) {
            throw new RuntimeException("team lead can only create 1 team");
        }

        team.setTeamStatus(TeamStatus.PENDING);

        Team savedTeam = teamRepo.save(team);

        teamLead.setTeam(savedTeam);
        teamLeadRepo.save(teamLead);

        return savedTeam;
    }

    public Team getTeamById(Long teamId) {
        return teamRepo.findById(teamId)
                .orElseThrow(() -> new NullPointerException("team not found"));
    }

    public Team updateTeam(Team team) {
        Team oldTeam = teamRepo.findById(team.getTeamId())
                .orElseThrow(() -> new NullPointerException("team doesn't exist"));

        if (team.getName() == null) {
            throw new NullPointerException("team name must be specified");
        }

        team.setTeamStatus(oldTeam.getTeamStatus());

        return teamRepo.save(team);
    }

    public String deleteTeam(Long teamId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new NullPointerException("team not found"));

        teamRepo.delete(team);

        return "team has been deleted";
    }

    public PageDto<TeamDto> getAllTeams(Pageable pageable) {
        Page<Team> page = teamRepo.findAll(pageable);

        List<TeamDto> teams = page.stream()
                .map(team -> teamConverter.convertToModel(team, new TeamDto()))
                .toList();

        PageDto<TeamDto> pageDto = new PageDto<>();
        pageDto.setContent(teams);
        pageDto.setPageNo(pageable.getPageSize());
        pageDto.setPageSize(page.getSize());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setEmpty(page.isEmpty());

        return pageDto;
    }

    public Team AcceptTeam(Long teamId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new NullPointerException("team not found"));

        team.setTeamStatus(TeamStatus.ACCEPTED);

        return teamRepo.save(team);
    }

    public Team rejectTeam(Long teamId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new NullPointerException("team not found"));

        team.setTeamStatus(TeamStatus.REJECTED);

        return teamRepo.save(team);
    }

    public Employee assignEmployeeToTeam(Long employeeId, Team team) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new NullPointerException("employee not found"));

        if (team == null) {
            throw new RuntimeException("cannot assign employee to team because team is null");
        }

        if (!team.getTeamStatus().equals(TeamStatus.ACCEPTED)) {
            throw new RuntimeException("team has not been accepted yet");
        }

        if (employee.getTeam() != null) {
            throw new RuntimeException("employee is already assigned to another team");
        }

        employee.setTeam(team);

        return employeeRepo.save(employee);
    }

    public Employee removeEmployeeFromTeam(Long employeeId, Long teamId) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new NullPointerException("employee not found"));

        if (!employee.getTeam().getTeamId().equals(teamId)) {
            throw new RuntimeException("employee is not from your team");
        }

        employee.setTeam(null);

        return employeeRepo.save(employee);
    }
}

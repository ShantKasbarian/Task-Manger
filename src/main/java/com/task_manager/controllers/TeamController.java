package com.task_manager.controllers;

import com.task_manager.converters.EmployeeConverter;
import com.task_manager.converters.TeamConverter;
import com.task_manager.entities.Team;
import com.task_manager.entities.TeamLead;
import com.task_manager.models.EmployeeDto;
import com.task_manager.models.PageDto;
import com.task_manager.models.TeamDto;
import com.task_manager.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    private final TeamConverter teamConverter;

    private final EmployeeConverter employeeConverter;

    @Autowired
    public TeamController(
            TeamService teamService,
            TeamConverter teamConverter,
            EmployeeConverter employeeConverter
    ) {
        this.teamService = teamService;
        this.teamConverter = teamConverter;
        this.employeeConverter = employeeConverter;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('TEAM_LEAD')")
    public ResponseEntity<TeamDto> createTeam(
            @RequestBody TeamDto teamDto,
            Authentication authentication
    ) {
        Team team = teamConverter.convertToEntity(new Team(), teamDto);
        TeamLead teamLead = (TeamLead) authentication.getPrincipal();

        return new ResponseEntity<>(
                teamConverter.convertToModel(
                        teamService.createTeam(team, teamLead), new TeamDto()
                ), HttpStatus.CREATED
        );
    }

    @GetMapping("/{teamId}")
    @PreAuthorize("hasRole('TEAM_LEAD') or hasRole('ADMIN')")
    public ResponseEntity<TeamDto> getTeamById(@PathVariable Long teamId) {
        return ResponseEntity.ok(
                teamConverter.convertToModel(
                        teamService.getTeamById(teamId), new TeamDto()
                )
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamDto> updateTeam(@RequestBody TeamDto teamDto) {
        Team team = teamConverter.convertToEntity(new Team(), teamDto);

        return ResponseEntity.ok(
                teamConverter.convertToModel(
                        teamService.updateTeam(team), new TeamDto()
                )
        );
    }

    @DeleteMapping("/{teamId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTeam(@PathVariable Long teamId) {
        return new ResponseEntity<>(
                teamService.deleteTeam(teamId), HttpStatus.NO_CONTENT
        );
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageDto<TeamDto>> getAllTeams(
            @RequestParam(value = "num", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(pageNum, size);

        return ResponseEntity.ok(teamService.getAllTeams(pageable));
    }

    @PutMapping("/{teamId}/accept")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamDto> acceptTeam(@PathVariable Long teamId) {
        return new ResponseEntity<>(
                teamConverter.convertToModel(
                        teamService.AcceptTeam(teamId), new TeamDto()
                ), HttpStatus.ACCEPTED
        );
    }

    @PutMapping("/{teamId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamDto> rejectTeam(@PathVariable Long teamId) {
        return new ResponseEntity<>(
                teamConverter.convertToModel(
                        teamService.rejectTeam(teamId), new TeamDto()
                ), HttpStatus.OK
        );
    }

    @PutMapping("/assign/employee/{employeeId}")
    @PreAuthorize("hasRole('TEAM_LEAD')")
    public ResponseEntity<EmployeeDto> assignEmployeeToTeam(
            @PathVariable Long employeeId,
            Authentication authentication
    ) {
        TeamLead teamLead = (TeamLead) authentication.getPrincipal();

        return ResponseEntity.ok(
                employeeConverter.convertToModel(
                        teamService.assignEmployeeToTeam(employeeId, teamLead.getTeam()),
                        new EmployeeDto()
                )
        );
    }

    @PutMapping("/remove/employee/{employeeId}")
    @PreAuthorize("hasRole('TEAM_LEAD')")
    public ResponseEntity<EmployeeDto> removeEmployeeFromTeam(
            @PathVariable Long employeeId,
            Authentication authentication
    ) {
        TeamLead teamLead = (TeamLead) authentication.getPrincipal();

        return new ResponseEntity<>(
                employeeConverter.convertToModel(
                        teamService.removeEmployeeFromTeam(employeeId, teamLead.getTeam().getTeamId()),
                        new EmployeeDto()
                ), HttpStatus.ACCEPTED
        );
    }
}

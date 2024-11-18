package com.task_manager.services;

import com.task_manager.converters.TeamConverter;
import com.task_manager.entities.*;
import com.task_manager.models.PageDto;
import com.task_manager.models.TeamDto;
import com.task_manager.repositories.EmployeeRepo;
import com.task_manager.repositories.TeamLeadRepo;
import com.task_manager.repositories.TeamRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TeamServiceTest {
    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepo teamRepo;

    @Mock
    private TeamConverter teamConverter;

    @Mock
    private TeamLeadRepo teamLeadRepo;

    @Mock
    private EmployeeRepo employeeRepo;

    private Team team;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        team = new Team();
        team.setTeamId(1L);
        team.setName("Team A");
        team.setRequests(new ArrayList<>());
        team.setEmployees(new ArrayList<>());
    }

    @Test
    void createTeam() {
        TeamLead teamLead = new TeamLead();
        teamLead.setUserId(1L);
        teamLead.setUsername("John");
        teamLead.setEmail("john@example.com");
        teamLead.setPassword("Password123+");
        teamLead.setRole(Role.TEAM_LEAD);

        when(teamRepo.save(team)).thenReturn(team);
        when(teamLeadRepo.save(teamLead)).thenReturn(teamLead);

        Team response = teamService.createTeam(team, teamLead);

        assertNotNull(teamLead.getTeam());
        assertEquals(TeamStatus.PENDING, response.getTeamStatus());
    }

    @Test
    void getTeamById() {
        when(teamRepo.findById(1L)).thenReturn(Optional.ofNullable(team));

        Team response = teamService.getTeamById(1L);

        assertEquals(team.getTeamId(), response.getTeamId());
        assertEquals(team.getName(), response.getName());
    }

    @Test
    void updateTeam() {
        team.setTeamStatus(TeamStatus.PENDING);

        when(teamRepo.findById(1L)).thenReturn(Optional.ofNullable(team));
        when(teamRepo.save(team)).thenReturn(team);

        Team response = teamService.updateTeam(team);

        assertEquals(team.getName(), response.getName());
        assertEquals(TeamStatus.PENDING, response.getTeamStatus());
    }

    @Test
    void deleteTeam() {
        when(teamRepo.findById(1L)).thenReturn(Optional.ofNullable(team));

        String response = teamService.deleteTeam(1L);

        assertEquals("team has been deleted", response);
    }

    @Test
    void getAllTeams() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team());
        teams.add(new Team());

        Page<Team> page = new PageImpl<>(teams);
        Pageable pageable = PageRequest.of(0, 10);

        when(teamRepo.findAll(pageable)).thenReturn(page);
        when(teamConverter.convertToModel(teams.get(0), new TeamDto()))
                .thenReturn(new TeamDto());

        when(teamConverter.convertToModel(teams.get(1), new TeamDto()))
                .thenReturn(new TeamDto());

        PageDto<TeamDto> response = teamService.getAllTeams(pageable);

        assertEquals(teams.size(), response.getContent().size());

    }

    @Test
    void acceptTeam() {
        when(teamRepo.findById(1L)).thenReturn(Optional.ofNullable(team));
        when(teamRepo.save(team)).thenReturn(team);

        Team response = teamService.AcceptTeam(1L);

        assertEquals(TeamStatus.ACCEPTED, response.getTeamStatus());
    }

    @Test
    void rejectTeam() {
        when(teamRepo.findById(1L)).thenReturn(Optional.ofNullable(team));
        when(teamRepo.save(team)).thenReturn(team);

        Team response = teamService.rejectTeam(1L);

        assertEquals(TeamStatus.REJECTED, response.getTeamStatus());
    }

    @Test
    void assignEmployeeToTeam() {
        team.setTeamStatus(TeamStatus.ACCEPTED);

        Employee employee = new Employee();
        employee.setUserId(1L);
        employee.setUsername("John");
        employee.setEmail("john@example.com");
        employee.setPassword("Password123+");
        employee.setRole(Role.EMPLOYEE);
        employee.setTasks(new ArrayList<>());

        when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepo.save(employee)).thenReturn(employee);

        Employee response = teamService.assignEmployeeToTeam(1L, team);

        assertNotNull(response.getTeam());
        assertEquals(employee.getUsername(), response.getUsername());
    }

    @Test
    void removeEmployeeFromTeam() {
        team.setTeamStatus(TeamStatus.ACCEPTED);

        Employee employee = new Employee();
        employee.setUserId(1L);
        employee.setUsername("John");
        employee.setEmail("john@example.com");
        employee.setPassword("Password123+");
        employee.setRole(Role.EMPLOYEE);
        employee.setTasks(new ArrayList<>());
        employee.setTeam(team);

        when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepo.save(employee)).thenReturn(employee);

        Employee response = teamService.removeEmployeeFromTeam(1L, 1L);

        assertNull(employee.getTeam());
    }
}
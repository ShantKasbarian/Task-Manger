package com.task_manager.services;

import com.task_manager.converters.EmployeeConverter;
import com.task_manager.converters.TeamLeadConverter;
import com.task_manager.converters.UserConverter;
import com.task_manager.entities.TeamLead;
import com.task_manager.entities.User;
import com.task_manager.models.EmployeeDto;
import com.task_manager.models.TeamLeadDto;
import com.task_manager.models.UserDto;
import com.task_manager.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final EmployeeRepo employeeRepo;

    private final TeamLeadRepo teamLeadRepo;

    private final EmployeeConverter employeeConverter;

    private final UserConverter userConverter;

    private final TeamLeadConverter teamLeadConverter;

    @Autowired
    public ProfileService(
            EmployeeRepo employeeRepo,
            TeamLeadRepo teamLeadRepo,
            EmployeeConverter employeeConverter,
            UserConverter userConverter,
            TeamLeadConverter teamLeadConverter
    ) {
        this.employeeRepo = employeeRepo;
        this.teamLeadRepo = teamLeadRepo;
        this.employeeConverter = employeeConverter;
        this.userConverter = userConverter;
        this.teamLeadConverter = teamLeadConverter;
    }

    public ResponseEntity<?> viewProfile(User user) {
        switch (user.getRole()) {
            case CUSTOMER, ADMIN -> {
                return ResponseEntity.ok(
                        userConverter.convertToModel(user, new UserDto())
                );
            }

            case EMPLOYEE -> {
                return ResponseEntity.ok(
                        employeeConverter.convertToModel(
                                employeeRepo.findById(user.getUserId()).get(), new EmployeeDto()
                        )
                );
            }

            case TEAM_LEAD -> {
                return ResponseEntity.ok(
                        teamLeadConverter.convertToModel(
                                teamLeadRepo.findById(user.getUserId()).get(), new TeamLeadDto()
                        )
                );
            }

            default -> {
                return (ResponseEntity<?>) ResponseEntity.badRequest();
            }
        }
    }
}

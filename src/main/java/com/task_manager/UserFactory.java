package com.task_manager;

import com.task_manager.entities.*;

public class UserFactory {
    public User getUser(Role role) {
        switch (role) {
            case Role.CUSTOMER:
                return new Customer();

            case Role.EMPLOYEE:
                return new Employee();

            case Role.TEAM_LEAD:
                return new TeamLead();

            default:
                throw new RuntimeException("access denied");
        }
    }
}

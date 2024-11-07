package com.task_manager.converters;

import com.task_manager.entities.Employee;
import com.task_manager.models.EmployeeDto;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter implements Converter<Employee, EmployeeDto>  {
    @Override
    public Employee convertToEntity(Employee entity, EmployeeDto model) {
        entity.setUserId(model.getId());
        entity.setUsername(model.getUsername());
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());
        entity.setRole(model.getRole());
        return entity;
    }

    @Override
    public EmployeeDto convertToModel(Employee entity, EmployeeDto model) {
        model.setId(entity.getUserId());
        model.setUsername(entity.getUsername());
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPassword());
        model.setRole(entity.getRole());
        return model;
    }
}

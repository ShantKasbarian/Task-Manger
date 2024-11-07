package com.task_manager.converters;

import com.task_manager.entities.User;
import com.task_manager.models.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserDto> {

    @Override
    public User convertToEntity(User entity, UserDto model) {
        entity.setUserId(model.getId());
        entity.setUsername(model.getUsername());
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());
        entity.setRole(model.getRole());
        return entity;
    }

    @Override
    public UserDto convertToModel(User entity, UserDto model) {
        model.setId(entity.getUserId());
        model.setUsername(entity.getUsername());
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPassword());
        model.setRole(entity.getRole());
        return model;
    }
}

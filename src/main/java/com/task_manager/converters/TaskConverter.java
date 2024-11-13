package com.task_manager.converters;

import com.task_manager.entities.Task;
import com.task_manager.models.TaskDto;
import org.springframework.stereotype.Component;

@Component
public class TaskConverter implements Converter<Task, TaskDto> {
    @Override
    public Task convertToEntity(Task entity, TaskDto model) {
        entity.setTaskId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setTaskStatus(model.getTaskStatus());
        entity.setAssignedTime(model.getAssignedTime());
        entity.setEndTime(model.getEndTime());
        entity.setDeadline(model.getDeadline());
        return entity;
    }

    @Override
    public TaskDto convertToModel(Task entity, TaskDto model) {
        model.setId(entity.getTaskId());
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setTaskStatus(entity.getTaskStatus());
        model.setAssignedTime(entity.getAssignedTime());
        model.setEndTime(entity.getEndTime());
        model.setDeadline(entity.getDeadline());
        return model;
    }
}

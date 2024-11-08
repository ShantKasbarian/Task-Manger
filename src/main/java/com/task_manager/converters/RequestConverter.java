package com.task_manager.converters;

import com.task_manager.entities.Request;
import com.task_manager.models.RequestDto;
import org.springframework.stereotype.Component;

@Component
public class RequestConverter implements Converter<Request, RequestDto> {

    @Override
    public Request convertToEntity(Request entity, RequestDto model) {
        entity.setRequestId(model.getRequestId());
        entity.setDescription(model.getDescription());
        entity.setTitle(model.getTitle());
        entity.setRequestStatus(model.getRequestStatus());
        return entity;
    }

    @Override
    public RequestDto convertToModel(Request entity, RequestDto model) {
        model.setRequestId(entity.getRequestId());
        model.setDescription(entity.getDescription());
        model.setTitle(entity.getTitle());
        model.setRequestStatus(entity.getRequestStatus());
        return model;
    }
}

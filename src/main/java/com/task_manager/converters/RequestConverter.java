package com.task_manager.converters;

import com.task_manager.entities.Request;
import com.task_manager.models.RequestDto;

public class RequestConverter implements Converter<Request, RequestDto> {

    @Override
    public Request convertToEntity(Request entity, RequestDto model) {
        entity.setRequestId(model.getRequestId());
        entity.setDescription(model.getDescription());
        entity.setTitle(model.getTitle());
        return entity;
    }

    @Override
    public RequestDto convertToModel(Request entity, RequestDto model) {
        model.setRequestId(entity.getRequestId());
        model.setDescription(entity.getDescription());
        model.setTitle(entity.getTitle());
        return model;
    }
}

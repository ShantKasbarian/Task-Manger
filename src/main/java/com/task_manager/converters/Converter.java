package com.task_manager.converters;

import org.springframework.stereotype.Component;

@Component
public interface Converter<E, M> {
    public E convertToEntity(E entity, M model);
    public M convertToModel(E entity, M model);
}

package com.task_manager.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private Long requestId;

    private String title;

    private String description;

    private CustomerDto customerDto;

    private TeamDto teamDto;

}

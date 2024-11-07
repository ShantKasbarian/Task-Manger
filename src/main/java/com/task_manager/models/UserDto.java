package com.task_manager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.task_manager.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;

    private String username;

    private String email;

    private String password;

    private Role role;

}

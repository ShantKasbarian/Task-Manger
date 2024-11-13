package com.task_manager.models;

import com.task_manager.entities.Role;
import lombok.Getter;
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

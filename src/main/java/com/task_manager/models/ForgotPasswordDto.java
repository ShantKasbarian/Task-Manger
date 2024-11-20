package com.task_manager.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordDto {
    private String email;
    private String password;
    private Integer num;
}

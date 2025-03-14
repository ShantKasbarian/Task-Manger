package com.task_manager.exceptions;

public class RequestUnauthorizedException extends RuntimeException {
    public RequestUnauthorizedException(String message) {
        super(message);
    }
}

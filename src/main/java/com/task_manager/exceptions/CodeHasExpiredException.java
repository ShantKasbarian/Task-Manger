package com.task_manager.exceptions;

public class CodeHasExpiredException extends RuntimeException {
    public CodeHasExpiredException(String message) {
        super(message);
    }
}

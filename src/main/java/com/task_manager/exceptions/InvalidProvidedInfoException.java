package com.task_manager.exceptions;

public class InvalidProvidedInfoException extends RuntimeException {
    public InvalidProvidedInfoException(String message) {
        super(message);
    }
}

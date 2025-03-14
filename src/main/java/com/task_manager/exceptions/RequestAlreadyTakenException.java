package com.task_manager.exceptions;

public class RequestAlreadyTakenException extends RuntimeException {
    public RequestAlreadyTakenException(String message) {
        super(message);
    }
}

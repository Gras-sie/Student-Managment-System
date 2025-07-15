package com.bcstudent.exception;

/**
 * Base exception class for appointment-related errors
 */
public class AppointmentException extends RuntimeException {
    private final String errorCode;

    public AppointmentException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

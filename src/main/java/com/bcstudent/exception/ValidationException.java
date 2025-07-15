package com.bcstudent.exception;

/**
 * Exception for validation failures
 */
public class ValidationException extends AppointmentException {
    public ValidationException(String errorCode, String message) {
        super(errorCode, message);
    }
}

package com.bcstudent.exception;

/**
 * Exception for database-related errors
 */
public class DatabaseException extends AppointmentException {
    public DatabaseException(String errorCode, String message) {
        super(errorCode, message);
    }
}

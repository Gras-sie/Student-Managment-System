package com.bcstudent.controller;

import com.bcstudent.model.AppointmentModel;
import com.bcstudent.model.Student;
import com.bcstudent.model.Counselor;
import com.bcstudent.util.DatabaseManager;
import com.bcstudent.util.ErrorDialog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class for managing appointments
 * Handles business logic between AppointmentModel and AppointmentView
 */
public class AppointmentController {
    private DatabaseManager dbManager;

    public AppointmentController() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Create a new appointment with validation
     * @param student The student requesting the appointment
     * @param counselor The counselor assigned
     * @param appointmentTime The scheduled time
     * @param notes Any additional notes
     * @return The created appointment or null if validation fails
     */
    public AppointmentModel createAppointment(Student student, Counselor counselor,
                                            LocalDateTime appointmentTime, String notes) throws ValidationException, DatabaseException {
        validateAppointment(student, counselor, appointmentTime);

        try {
            AppointmentModel appointment = new AppointmentModel();
            appointment.setStudent(student);
            appointment.setCounselor(counselor);
            appointment.setAppointmentTime(appointmentTime);
            appointment.setNotes(notes);
            
            dbManager.createAppointment(appointment);
            return appointment;
        } catch (Exception e) {
            throw new DatabaseException("DB_CREATE_ERROR", "Failed to create appointment: " + e.getMessage());
        }
    }

    /**
     * Validate appointment input
     * @param student The student
     * @param counselor The counselor
     * @param appointmentTime The appointment time
     * @throws ValidationException if validation fails
     */
    private void validateAppointment(Student student, Counselor counselor, LocalDateTime appointmentTime) throws ValidationException {
        if (student == null) {
            throw new ValidationException("VAL_STUDENT_NULL", "Student cannot be null");
        }
        if (counselor == null) {
            throw new ValidationException("VAL_COUNSELOR_NULL", "Counselor cannot be null");
        }
        if (appointmentTime == null) {
            throw new ValidationException("VAL_TIME_NULL", "Appointment time cannot be null");
        }
        if (appointmentTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException("VAL_TIME_PAST", "Appointment time must be in the future");
        }
    }

    /**
     * Get upcoming appointments for a student
     * @param student The student
     * @return List of upcoming appointments
     * @throws DatabaseException if database operation fails
     */
    public List<AppointmentModel> getUpcomingAppointmentsByStudent(Student student) throws DatabaseException {
        try {
            List<AppointmentModel> appointments = dbManager.getAppointmentsByStudent(student.getStudentNumber());
            return filterUpcomingAppointments(appointments);
        } catch (Exception e) {
            throw new DatabaseException("DB_FETCH_ERROR", "Failed to fetch appointments: " + e.getMessage());
        }
    }

    /**
     * Get upcoming appointments for a counselor
     * @param counselor The counselor
     * @return List of upcoming appointments
     */
    public List<AppointmentModel> getUpcomingAppointmentsByCounselor(Counselor counselor) {
        try {
            List<AppointmentModel> appointments = dbManager.getAppointmentsByCounselor(counselor.getName());
            return filterUpcomingAppointments(appointments);
        } catch (Exception e) {
            ErrorDialog.showError("Database Error", "Failed to fetch appointments: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Filter appointments to show only upcoming ones
     * @param appointments List of appointments
     * @return List of upcoming appointments
     */
    private List<AppointmentModel> filterUpcomingAppointments(List<AppointmentModel> appointments) {
        List<AppointmentModel> upcoming = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (AppointmentModel appointment : appointments) {
            if (appointment.getAppointmentTime().isAfter(now) && 
                (appointment.getStatus().equals("Scheduled") || appointment.getStatus().equals("Confirmed"))) {
                upcoming.add(appointment);
            }
        }
        return upcoming;
    }

    /**
     * Reschedule an appointment
     * @param appointmentId The appointment to reschedule
     * @param newTime New appointment time
     * @return true if rescheduled successfully, false otherwise
     */
    public boolean rescheduleAppointment(int appointmentId, LocalDateTime newTime) {
        try {
            if (newTime.isBefore(LocalDateTime.now())) {
                ErrorDialog.showError("Validation Error", "New appointment time must be in the future");
                return false;
            }

            // Confirm rescheduling with user
            if (!ErrorDialog.showConfirmation("Reschedule Appointment", 
                "Are you sure you want to reschedule this appointment to " + newTime.toString() + "?")) {
                return false;
            }

            dbManager.updateAppointmentTime(appointmentId, newTime);
            return true;
        } catch (Exception e) {
            ErrorDialog.showError("Database Error", "Failed to reschedule appointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancel an appointment
     * @param appointmentId The appointment to cancel
     * @return true if cancelled successfully, false otherwise
     */
    public boolean cancelAppointment(int appointmentId) {
        try {
            // Confirm cancellation with user
            if (!ErrorDialog.showConfirmation("Cancel Appointment", 
                "Are you sure you want to cancel this appointment?")) {
                return false;
            }

            dbManager.updateAppointmentStatus(appointmentId, "Cancelled");
            return true;
        } catch (Exception e) {
            ErrorDialog.showError("Database Error", "Failed to cancel appointment: " + e.getMessage());
            return false;
        }
    }
}

package com.bcstudent.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing an appointment
 */
public class AppointmentModel {
    private int appointmentId;
    private Student student;
    private Counselor counselor;
    private LocalDateTime appointmentTime;
    private String status;
    private String notes;

    public AppointmentModel() {
        this.status = "Scheduled";
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Counselor getCounselor() {
        return counselor;
    }

    public void setCounselor(Counselor counselor) {
        this.counselor = counselor;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "AppointmentModel{" +
                "appointmentId=" + appointmentId +
                ", student=" + student +
                ", counselor=" + counselor +
                ", appointmentTime=" + appointmentTime +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}

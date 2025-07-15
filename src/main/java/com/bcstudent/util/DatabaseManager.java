package com.bcstudent.util;

import com.bcstudent.model.AppointmentModel;
import com.bcstudent.model.Student;
import com.bcstudent.model.Counselor;
import com.bcstudent.model.Person;
import com.bcstudent.model.ContactInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class for managing database operations
 * Handles CRUD operations for appointments and other entities
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    // Database connection parameters
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/bcstudent";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";

    private DatabaseManager() {
        try {
            // Load PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Appointment CRUD operations
    public int createAppointment(AppointmentModel appointment) throws SQLException {
        String sql = "INSERT INTO appointments (student_number, counselor_name, appointment_time, status, notes) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING appointment_id";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, appointment.getStudent().getStudentNumber());
            stmt.setString(2, appointment.getCounselor().getName());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(appointment.getAppointmentTime()));
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getNotes());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("appointment_id");
            }
            throw new SQLException("Failed to create appointment");
        }
    }

    public List<AppointmentModel> getAppointmentsByStudent(String studentNumber) throws SQLException {
        List<AppointmentModel> appointments = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, c.name as counselor_name, " +
                    "s.email as student_email, s.phone as student_phone, s.address as student_address, " +
                    "c.email as counselor_email, c.phone as counselor_phone, c.address as counselor_address " +
                    "FROM appointments a " +
                    "JOIN students s ON a.student_number = s.student_number " +
                    "JOIN counselors c ON a.counselor_name = c.name " +
                    "WHERE a.student_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AppointmentModel appointment = new AppointmentModel();
                appointment.setAppointmentId(rs.getInt("appointment_id"));
                
                // Create student with complete info
                Student student = new Student(
                    rs.getString("student_name"),
                    new ContactInfo(
                        rs.getString("student_email"),
                        rs.getString("student_phone"),
                        rs.getString("student_address")
                    ),
                    studentNumber,
                    "" // Program info would need to be joined from another table
                );
                appointment.setStudent(student);
                
                // Create counselor with complete info
                Counselor counselor = new Counselor(
                    rs.getString("counselor_name"),
                    new ContactInfo(
                        rs.getString("counselor_email"),
                        rs.getString("counselor_phone"),
                        rs.getString("counselor_address")
                    ),
                    "", // Specialization would need to be joined from another table
                    "" // Office location would need to be joined from another table
                );
                appointment.setCounselor(counselor);
                
                appointment.setAppointmentTime(rs.getTimestamp("appointment_time").toLocalDateTime());
                appointment.setStatus(rs.getString("status"));
                appointment.setNotes(rs.getString("notes"));
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    public List<AppointmentModel> getAppointmentsByCounselor(String counselorName) throws SQLException {
        List<AppointmentModel> appointments = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, c.name as counselor_name, " +
                    "s.email as student_email, s.phone as student_phone, s.address as student_address, " +
                    "c.email as counselor_email, c.phone as counselor_phone, c.address as counselor_address " +
                    "FROM appointments a " +
                    "JOIN students s ON a.student_number = s.student_number " +
                    "JOIN counselors c ON a.counselor_name = c.name " +
                    "WHERE a.counselor_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, counselorName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AppointmentModel appointment = new AppointmentModel();
                appointment.setAppointmentId(rs.getInt("appointment_id"));
                
                // Create student with complete info
                Student student = new Student(
                    rs.getString("student_name"),
                    new ContactInfo(
                        rs.getString("student_email"),
                        rs.getString("student_phone"),
                        rs.getString("student_address")
                    ),
                    rs.getString("student_number"),
                    "" // Program info would need to be joined from another table
                );
                appointment.setStudent(student);
                
                // Create counselor with complete info
                Counselor counselor = new Counselor(
                    rs.getString("counselor_name"),
                    new ContactInfo(
                        rs.getString("counselor_email"),
                        rs.getString("counselor_phone"),
                        rs.getString("counselor_address")
                    ),
                    "", // Specialization would need to be joined from another table
                    "" // Office location would need to be joined from another table
                );
                appointment.setCounselor(counselor);
                
                appointment.setAppointmentTime(rs.getTimestamp("appointment_time").toLocalDateTime());
                appointment.setStatus(rs.getString("status"));
                appointment.setNotes(rs.getString("notes"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public void updateAppointmentStatus(int appointmentId, String status) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, appointmentId);
            stmt.executeUpdate();
        }
    }

    public void addNotesToAppointment(int appointmentId, String notes) throws SQLException {
        String sql = "UPDATE appointments SET notes = ? WHERE appointment_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, notes);
            stmt.setInt(2, appointmentId);
            stmt.executeUpdate();
        }
    }

    public void updateAppointmentTime(int appointmentId, LocalDateTime newTime) throws SQLException {
        String sql = "UPDATE appointments SET appointment_time = ? WHERE appointment_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(newTime));
            stmt.setInt(2, appointmentId);
            stmt.executeUpdate();
        }
    }

    public void deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        }
    }
}

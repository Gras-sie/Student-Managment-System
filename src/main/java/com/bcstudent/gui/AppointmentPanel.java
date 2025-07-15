package com.bcstudent.gui;

import com.bcstudent.controller.AppointmentController;
import com.bcstudent.model.AppointmentModel;
import com.bcstudent.model.Counselor;
import com.bcstudent.model.Student;
import com.bcstudent.util.ErrorDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Panel for managing appointments
 * Provides interface for booking, viewing, updating, and canceling appointments
 */
public class AppointmentPanel extends JPanel {
    private AppointmentController controller;
    private Student student;
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private JComboBox<Counselor> counselorComboBox;
    private JTextField notesField;
    private JButton bookButton;
    private JButton cancelButton;
    private JButton rescheduleButton;
    private JButton refreshButton;

    public AppointmentPanel() {
        controller = new AppointmentController();
        student = (Student) SessionManager.getInstance().getCurrentUser();

        // Initialize components
        initializeComponents();
        setupLayout();
        loadCounselors();
        loadAppointments();
    }

    private void initializeComponents() {
        // Table for appointments
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Appointment ID");
        tableModel.addColumn("Date & Time");
        tableModel.addColumn("Counselor");
        tableModel.addColumn("Status");
        tableModel.addColumn("Notes");

        appointmentsTable = new JTable(tableModel);
        appointmentsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        appointmentsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        appointmentsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        appointmentsTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        appointmentsTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);

        // Form for booking appointments
        counselorComboBox = new JComboBox<>();
        counselorComboBox.setPreferredSize(new Dimension(200, 30));
        notesField = new JTextField(20);
        JTextField dateTimeField = new JTextField(20);
        dateTimeField.setEditable(false);

        // Buttons
        bookButton = new JButton("Book Appointment");
        cancelButton = new JButton("Cancel Appointment");
        rescheduleButton = new JButton("Reschedule Appointment");
        refreshButton = new JButton("Refresh");

        // Add action listeners
        bookButton.addActionListener(e -> bookAppointment());
        cancelButton.addActionListener(e -> cancelAppointment());
        rescheduleButton.addActionListener(e -> rescheduleAppointment());
        refreshButton.addActionListener(e -> loadAppointments());
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(appointmentsTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.add(new JLabel("Counselor:"));
        formPanel.add(counselorComboBox);
        formPanel.add(new JLabel("Notes:"));
        formPanel.add(notesField);
        formPanel.add(new JLabel(""));
        formPanel.add(bookButton);
        add(formPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(rescheduleButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCounselors() {
        try {
            List<Counselor> counselors = controller.getAllCounselors();
            counselorComboBox.removeAllItems();
            counselors.forEach(counselorComboBox::addItem);
        } catch (Exception e) {
            ErrorDialog.showError("Loading Failed", "Failed to load counselors: " + e.getMessage());
        }
        List<Counselor> counselors = controller.getAllCounselors();
        counselorComboBox.removeAllItems();
        counselors.forEach(counselorComboBox::addItem);
    }

    private void loadAppointments() {
        if (student == null) {
            ErrorDialog.showError("Login Required", "Please log in first");
            return;
        }

        try {
            List<AppointmentModel> appointments = controller.getUpcomingAppointmentsByStudent(student);
            if (appointments == null) {
                ErrorDialog.showError("Database Error", "Failed to fetch appointments from database");
                return;
            }
            
            tableModel.setRowCount(0);
            for (AppointmentModel appointment : appointments) {
                if (appointment == null) {
                    ErrorDialog.showError("Data Error", "Invalid appointment data received");
                    continue;
                }
                
                tableModel.addRow(new Object[] {
                    appointment.getAppointmentId(),
                    appointment.getAppointmentTime(),
                    appointment.getCounselor().getName(),
                    appointment.getStatus(),
                    appointment.getNotes()
                });
            }
        } catch (Exception e) {
            ErrorDialog.showError("Loading Failed", "Failed to load appointments: " + e.getMessage());
        }
    }

    private void bookAppointment() {
        if (student == null) {
            ErrorDialog.showError("Login Required", "Please log in first");
            return;
        }

        Counselor selectedCounselor = (Counselor) counselorComboBox.getSelectedItem();
        if (selectedCounselor == null) {
            ErrorDialog.showError("Selection Required", "Please select a counselor");
            return;
        }

        LocalDateTime appointmentTime = LocalDateTime.now().plusHours(1); // Default to 1 hour from now
        String notes = notesField.getText();

        try {
            AppointmentModel appointment = controller.createAppointment(
                student, 
                selectedCounselor,
                appointmentTime,
                notes
            );
            
            if (appointment != null) {
                loadAppointments();
                ErrorDialog.showInfo("Success", "Appointment booked successfully!");
                notesField.setText("");
            }
        } catch (Exception e) {
            ErrorDialog.showError("Booking Failed", "Failed to book appointment: " + e.getMessage());
        }
    }

    private void cancelAppointment() {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow == -1) {
            ErrorDialog.showError("Selection Required", "Please select an appointment to cancel");
            return;
        }

        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        if (!ErrorDialog.showConfirmation("Cancel Appointment",
                "Are you sure you want to cancel this appointment?")) {
            return;
        }

        try {
            if (controller.cancelAppointment(appointmentId)) {
                loadAppointments();
                ErrorDialog.showInfo("Success", "Appointment cancelled successfully!");
            }
        } catch (Exception e) {
            ErrorDialog.showError("Cancellation Failed", "Failed to cancel appointment: " + e.getMessage());
        }
    }

    private void rescheduleAppointment() {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow == -1) {
            ErrorDialog.showError("Selection Required", "Please select an appointment to reschedule");
            return;
        }

        int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
        LocalDateTime currentDateTime = (LocalDateTime) tableModel.getValueAt(selectedRow, 1);
        
        // Create date chooser dialog
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        
        dateSpinner.setEditor(dateEditor);
        timeSpinner.setEditor(timeEditor);
        
        dateSpinner.setValue(currentDateTime.toLocalDate());
        timeSpinner.setValue(currentDateTime.toLocalTime());
        
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("New Date:"));
        panel.add(dateSpinner);
        panel.add(new JLabel("New Time:"));
        panel.add(timeSpinner);
        
        // Show confirmation dialog with current and new times
        String currentDateTimeStr = currentDateTime.toString();
        String newDateTimeStr = LocalDateTime.of(
            ((LocalDate) dateSpinner.getValue()),
            ((LocalTime) timeSpinner.getValue())
        ).toString();
        
        String message = String.format(
            "Current appointment time: %s\n" +
            "New appointment time: %s\n\n" +
            "Are you sure you want to reschedule this appointment?",
            currentDateTimeStr, newDateTimeStr
        );
        
        if (!ErrorDialog.showConfirmation("Reschedule Appointment", message)) {
            return;
        }
        
        LocalDateTime newDateTime = LocalDateTime.of(
            ((LocalDate) dateSpinner.getValue()),
            ((LocalTime) timeSpinner.getValue())
        );
        
        if (newDateTime.isBefore(LocalDateTime.now())) {
            ErrorDialog.showError("Invalid Time", "The new appointment time must be in the future");
            return;
        }
        
        // Validate that new time is not too close to current time
        if (newDateTime.isBefore(currentDateTime.plusMinutes(30))) {
            ErrorDialog.showError("Invalid Time", 
                "The new appointment time must be at least 30 minutes after the current time");
            return;
        }
        
        try {
            if (controller.rescheduleAppointment(appointmentId, newDateTime)) {
                loadAppointments();
                ErrorDialog.showInfo("Success", 
                    String.format("Appointment rescheduled successfully!\nNew time: %s", newDateTimeStr));
            }
        } catch (Exception e) {
            ErrorDialog.showError("Rescheduling Failed", 
                String.format("Failed to reschedule appointment: %s", e.getMessage()));
        }
    }
}

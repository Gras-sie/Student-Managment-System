package com.bcstudent.gui;

import com.bcstudent.controller.AppointmentController;
import com.bcstudent.model.AppointmentModel;
import com.bcstudent.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for managing feedback
 * Provides interface for giving feedback with star rating and comments
 */
public class FeedbackPanel extends JPanel {
    private AppointmentController controller;
    private Student student;
    private JTable feedbackTable;
    private DefaultTableModel tableModel;
    private JComboBox<AppointmentModel> appointmentComboBox;
    private JSlider ratingSlider;
    private JTextArea commentsArea;
    private JButton submitButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public FeedbackPanel() {
        controller = new AppointmentController();
        student = (Student) SessionManager.getInstance().getCurrentUser();
        initializeComponents();
        setupLayout();
        loadAppointments();
        loadFeedback();
    }

    private void initializeComponents() {
        // Table for feedback
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Appointment");
        tableModel.addColumn("Rating");
        tableModel.addColumn("Comments");
        tableModel.addColumn("Date");

        feedbackTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(feedbackTable);

        // Form for feedback
        appointmentComboBox = new JComboBox<>();
        ratingSlider = new JSlider(0, 5, 0); // 0-5 stars
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        commentsArea = new JTextArea(5, 30);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);

        // Buttons
        submitButton = new JButton("Submit Feedback");
        editButton = new JButton("Edit Feedback");
        deleteButton = new JButton("Delete Feedback");
        refreshButton = new JButton("Refresh");

        // Add action listeners
        submitButton.addActionListener(e -> submitFeedback());
        editButton.addActionListener(e -> editFeedback());
        deleteButton.addActionListener(e -> deleteFeedback());
        refreshButton.addActionListener(e -> loadFeedback());
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(feedbackTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.add(new JLabel("Appointment:"));
        formPanel.add(appointmentComboBox);
        formPanel.add(new JLabel("Rating:"));
        formPanel.add(ratingSlider);
        formPanel.add(new JLabel("Comments:"));
        formPanel.add(new JScrollPane(commentsArea));
        formPanel.add(new JLabel(""));
        formPanel.add(submitButton);
        add(formPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadAppointments() {
        if (student != null) {
            List<AppointmentModel> appointments = controller.getAppointmentsByStudent(student);
            appointmentComboBox.removeAllItems();
            
            for (AppointmentModel appointment : appointments) {
                if ("Completed".equals(appointment.getStatus())) {
                    appointmentComboBox.addItem(appointment);
                }
            }
        }
    }

    private void loadFeedback() {
        if (student != null) {
            List<AppointmentModel> appointments = controller.getAppointmentsByStudent(student);
            tableModel.setRowCount(0);
            
            for (AppointmentModel appointment : appointments) {
                if (appointment.getNotes() != null && !appointment.getNotes().isEmpty()) {
                    tableModel.addRow(new Object[] {
                        appointment.getAppointmentTime(),
                        appointment.getNotes().split("\\n")[0], // First line of notes as rating
                        appointment.getNotes().split("\\n")[1], // Second line of notes as comments
                        appointment.getAppointmentTime().toLocalDate()
                    });
                }
            }
        }
    }

    private void submitFeedback() {
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Please log in first");
            return;
        }

        AppointmentModel selectedAppointment = (AppointmentModel) appointmentComboBox.getSelectedItem();
        if (selectedAppointment == null) {
            JOptionPane.showMessageDialog(this, "Please select an appointment");
            return;
        }

        int rating = ratingSlider.getValue();
        String comments = commentsArea.getText().trim();

        if (comments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter some comments");
            return;
        }

        try {
            // Store feedback in notes field with rating and comments separated by newline
            String feedback = rating + "\n" + comments;
            controller.addNotesToAppointment(selectedAppointment.getAppointmentId(), feedback);
            loadFeedback();
            clearForm();
            JOptionPane.showMessageDialog(this, "Feedback submitted successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to submit feedback: " + e.getMessage());
        }
    }

    private void editFeedback() {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select feedback to edit");
            return;
        }

        try {
            AppointmentModel selectedAppointment = (AppointmentModel) appointmentComboBox.getSelectedItem();
            if (selectedAppointment == null) {
                JOptionPane.showMessageDialog(this, "Please select an appointment");
                return;
            }

            int rating = ratingSlider.getValue();
            String comments = commentsArea.getText().trim();

            if (comments.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter some comments");
                return;
            }

            String feedback = rating + "\n" + comments;
            controller.addNotesToAppointment(selectedAppointment.getAppointmentId(), feedback);
            loadFeedback();
            JOptionPane.showMessageDialog(this, "Feedback updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update feedback: " + e.getMessage());
        }
    }

    private void deleteFeedback() {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select feedback to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this feedback?",
                "Delete Feedback",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                AppointmentModel selectedAppointment = (AppointmentModel) appointmentComboBox.getSelectedItem();
                if (selectedAppointment != null) {
                    controller.addNotesToAppointment(selectedAppointment.getAppointmentId(), "");
                    loadFeedback();
                    JOptionPane.showMessageDialog(this, "Feedback deleted successfully!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to delete feedback: " + e.getMessage());
            }
        }
    }

    private void clearForm() {
        appointmentComboBox.setSelectedIndex(-1);
        ratingSlider.setValue(0);
        commentsArea.setText("");
    }
}

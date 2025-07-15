package com.bcstudent.gui;

import com.bcstudent.controller.AppointmentController;
import com.bcstudent.model.Counselor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for managing counselors
 * Provides interface for viewing, adding, editing, and deleting counselors
 */
public class CounselorPanel extends JPanel {
    private AppointmentController controller;
    private JTable counselorsTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField specializationField;
    private JTextField officeLocationField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public CounselorPanel() {
        controller = new AppointmentController();
        initializeComponents();
        setupLayout();
        loadCounselors();
    }

    private void initializeComponents() {
        // Table for counselors
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Specialization");
        tableModel.addColumn("Office Location");

        counselorsTable = new JTable(tableModel);
        counselorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(counselorsTable);

        // Form for counselor details
        nameField = new JTextField(20);
        specializationField = new JTextField(20);
        officeLocationField = new JTextField(20);

        // Buttons
        addButton = new JButton("Add Counselor");
        editButton = new JButton("Edit Counselor");
        deleteButton = new JButton("Delete Counselor");
        refreshButton = new JButton("Refresh");

        // Add action listeners
        addButton.addActionListener(e -> addCounselor());
        editButton.addActionListener(e -> editCounselor());
        deleteButton.addActionListener(e -> deleteCounselor());
        refreshButton.addActionListener(e -> loadCounselors());
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(counselorsTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Specialization:"));
        formPanel.add(specializationField);
        formPanel.add(new JLabel("Office Location:"));
        formPanel.add(officeLocationField);
        add(formPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCounselors() {
        List<Counselor> counselors = controller.getAllCounselors();
        tableModel.setRowCount(0);
        
        for (Counselor counselor : counselors) {
            tableModel.addRow(new Object[] {
                counselor.getName(),
                counselor.getSpecialization(),
                counselor.getOfficeLocation()
            });
        }
    }

    private void addCounselor() {
        String name = nameField.getText().trim();
        String specialization = specializationField.getText().trim();
        String officeLocation = officeLocationField.getText().trim();

        if (name.isEmpty() || specialization.isEmpty() || officeLocation.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required");
            return;
        }

        try {
            Counselor counselor = new Counselor(
                name,
                new ContactInfo("", "", ""), // Empty contact info for now
                specialization,
                officeLocation
            );

            controller.createCounselor(counselor);
            loadCounselors();
            clearForm();
            JOptionPane.showMessageDialog(this, "Counselor added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to add counselor: " + e.getMessage());
        }
    }

    private void editCounselor() {
        int selectedRow = counselorsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a counselor to edit");
            return;
        }

        String name = nameField.getText().trim();
        String specialization = specializationField.getText().trim();
        String officeLocation = officeLocationField.getText().trim();

        if (name.isEmpty() || specialization.isEmpty() || officeLocation.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required");
            return;
        }

        try {
            Counselor counselor = new Counselor(
                name,
                new ContactInfo("", "", ""),
                specialization,
                officeLocation
            );

            controller.updateCounselor(counselor);
            loadCounselors();
            clearForm();
            JOptionPane.showMessageDialog(this, "Counselor updated successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update counselor: " + e.getMessage());
        }
    }

    private void deleteCounselor() {
        int selectedRow = counselorsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a counselor to delete");
            return;
        }

        String name = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this counselor?",
                "Delete Counselor",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.deleteCounselor(name);
                loadCounselors();
                JOptionPane.showMessageDialog(this, "Counselor deleted successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to delete counselor: " + e.getMessage());
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        specializationField.setText("");
        officeLocationField.setText("");
    }

    public void loadSelectedCounselor() {
        int selectedRow = counselorsTable.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 0));
            specializationField.setText((String) tableModel.getValueAt(selectedRow, 1));
            officeLocationField.setText((String) tableModel.getValueAt(selectedRow, 2));
        }
    }
}

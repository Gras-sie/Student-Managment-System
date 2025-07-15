package com.bcstudent.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main Swing dashboard for Student Wellness Management System
 * Contains tabs for Appointments, Counselors, and Feedback
 */
public class StudentDashboard extends JFrame {
    private AppointmentPanel appointmentPanel;
    private CounselorPanel counselorPanel;
    private FeedbackPanel feedbackPanel;
    private JTabbedPane tabbedPane;

    public StudentDashboard() {
        setTitle("BC Student Wellness Management System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize panels
        appointmentPanel = new AppointmentPanel();
        counselorPanel = new CounselorPanel();
        feedbackPanel = new FeedbackPanel();

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Appointments", appointmentPanel);
        tabbedPane.addTab("Counselors", counselorPanel);
        tabbedPane.addTab("Feedback", feedbackPanel);

        // Add components to frame
        add(tabbedPane);

        // Add menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Set layout and visibility
        setLayout(new BorderLayout());
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard());
    }
}

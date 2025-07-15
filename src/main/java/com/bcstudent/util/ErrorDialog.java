package com.bcstudent.util;

import com.bcstudent.exception.AppointmentException;

import javax.swing.*;

/**
 * Utility class for showing info dialogs and confirmations
 */
public class DialogUtil {
    /**
     * Show an info dialog with the given title and message
     * @param title The dialog title
     * @param message The info message
     */
    public static void showInfo(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show an error dialog with the given title and message
     * @param title The dialog title
     * @param message The error message
     */
    public static void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show an error dialog with the given title and exception
     * @param title The dialog title
     * @param e The exception to display
     */
    public static void showError(String title, Exception e) {
        if (e instanceof AppointmentException) {
            AppointmentException appEx = (AppointmentException) e;
            String message = String.format("Error Code: %s\nMessage: %s", 
                appEx.getErrorCode(), appEx.getMessage());
            showError(title, message);
        } else {
            showError(title, e.getMessage());
        }
    }

    /**
     * Show a confirmation dialog with the given title and message
     * @param title The dialog title
     * @param message The confirmation message
     * @return true if user confirms, false otherwise
     */
    public static boolean showConfirmation(String title, String message) {
        return JOptionPane.showConfirmDialog(
            null, 
            message, 
            title, 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}

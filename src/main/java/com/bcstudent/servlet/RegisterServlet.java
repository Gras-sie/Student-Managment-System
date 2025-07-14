package com.bcstudent.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bcstudent.util.DBConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Servlet implementation class RegisterServlet
 * Handles user registration with input validation, duplicate checking,
 * password hashing, and database insertion.
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Regular expression for email validation
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters
        String studentNumber = request.getParameter("studentNumber");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate inputs
        String errorMessage = validateInputs(studentNumber, name, surname, email, phone, password, confirmPassword);
        
        if (errorMessage != null) {
            // If validation fails, redirect back to registration page with error
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Check for duplicate student number or email
        if (isDuplicate(studentNumber, email)) {
            request.setAttribute("errorMessage", "Student number or email already exists.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // Hash the password
        String hashedPassword = hashPassword(password);
        
        // Insert new user into database
        if (insertUser(studentNumber, name, surname, email, phone, hashedPassword)) {
            // Registration successful, redirect to login page
            response.sendRedirect("login.jsp");
        } else {
            // Database error
            request.setAttribute("errorMessage", "Registration failed. Please try again later.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
    
    /**
     * Validates user input
     * 
     * @param studentNumber Student number
     * @param name First name
     * @param surname Last name
     * @param email Email address
     * @param phone Phone number
     * @param password Password
     * @param confirmPassword Confirm password
     * @return Error message if validation fails, null if validation passes
     */
    private String validateInputs(String studentNumber, String name, String surname, String email, 
                                 String phone, String password, String confirmPassword) {
        // Check for empty fields
        if (studentNumber == null || studentNumber.trim().isEmpty() ||
            name == null || name.trim().isEmpty() ||
            surname == null || surname.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty()) {
            return "All fields are required.";
        }
        
        // Validate student number format (assuming it should be alphanumeric)
        if (!studentNumber.matches("^[a-zA-Z0-9]+$")) {
            return "Student number should contain only letters and numbers.";
        }
        
        // Validate email format
        if (!isValidEmail(email)) {
            return "Invalid email format.";
        }
        
        // Validate phone number (10 digits)
        if (!phone.matches("^[0-9]{10}$")) {
            return "Phone number must be 10 digits.";
        }
        
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        
        // Validate password strength
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        
        // Check for at least one uppercase letter, one lowercase letter, and one digit
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        
        if (!hasUppercase || !hasLowercase || !hasDigit) {
            return "Password must contain at least one uppercase letter, one lowercase letter, and one digit.";
        }
        
        // All validations passed
        return null;
    }
    
    /**
     * Validates email format using regex
     * 
     * @param email Email to validate
     * @return true if email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
    
    /**
     * Checks if student number or email already exists in the database
     * 
     * @param studentNumber Student number to check
     * @param email Email to check
     * @return true if duplicate exists, false otherwise
     */
    private boolean isDuplicate(String studentNumber, String email) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isDuplicate = false;
        
        try {
            conn = DBConnection.getConnection();
            
            // Check for duplicate student number
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE student_number = ?");
            stmt.setString(1, studentNumber);
            rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                isDuplicate = true;
            } else {
                // Check for duplicate email
                stmt.close();
                stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?");
                stmt.setString(1, email);
                rs.close();
                rs = stmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    isDuplicate = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) DBConnection.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return isDuplicate;
    }
    
    /**
     * Hashes the password using SHA-256
     * 
     * @param password Password to hash
     * @return Hashed password
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Fallback to plain text if hashing fails (should never happen with SHA-256)
            return password;
        }
    }
    
    /**
     * Inserts a new user into the database
     * 
     * @param studentNumber Student number
     * @param name First name
     * @param surname Last name
     * @param email Email address
     * @param phone Phone number
     * @param hashedPassword Hashed password
     * @return true if insertion was successful, false otherwise
     */
    private boolean insertUser(String studentNumber, String name, String surname, 
                              String email, String phone, String hashedPassword) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            
            // Prepare SQL statement
            String sql = "INSERT INTO users (student_number, name, surname, email, phone, password) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentNumber);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setString(6, hashedPassword);
            
            // Execute the insert
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) DBConnection.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return success;
    }
}
package com.bcstudent.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bcstudent.util.DBConnection;

/**
 * Servlet implementation class LoginServlet
 * Handles user authentication and session management.
 */
@WebServlet("/StudentWellness/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate inputs
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/StudentWellness/login.jsp?error=" + java.net.URLEncoder.encode("Username and password are required.", "UTF-8"));
            return;
        }

        // Authenticate user
        try {
            // Attempt to authenticate the user
            User user = authenticateUser(username, password);

            if (user != null) {
                // Authentication successful, create session
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("studentNumber", user.getStudentNumber());
                session.setAttribute("name", user.getName());
                session.setAttribute("surname", user.getSurname());
                session.setAttribute("email", user.getEmail());

                // Update last login timestamp
                updateLastLogin(user.getUserId());

                // Redirect to dashboard servlet
                response.sendRedirect(request.getContextPath() + "/StudentWellness/DashboardServlet");
            } else {
                // Authentication failed
                response.sendRedirect(request.getContextPath() + "/StudentWellness/login.jsp?error=" + java.net.URLEncoder.encode("Invalid username or password.", "UTF-8"));
            }
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/StudentWellness/login.jsp?error=" + java.net.URLEncoder.encode("An error occurred during login. Please try again later.", "UTF-8"));
        }
    }

    /**
     * Authenticates a user by checking credentials against the database
     * 
     * @param username Student number or email
     * @param password Password to check
     * @return User object if authentication succeeds, null otherwise
     */
    private User authenticateUser(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();

            // Prepare SQL statement to check if username is student number or email
            String sql = "SELECT user_id, student_number, name, surname, email, password " +
                         "FROM users WHERE student_number = ? OR email = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, username);

            rs = stmt.executeQuery();

            if (rs.next()) {
                // Get stored hashed password
                String storedHash = rs.getString("password");

                // Hash the provided password
                String hashedPassword = hashPassword(password);

                // Compare the hashes
                if (storedHash.equals(hashedPassword)) {
                    // Authentication successful, create user object
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setStudentNumber(rs.getString("student_number"));
                    user.setName(rs.getString("name"));
                    user.setSurname(rs.getString("surname"));
                    user.setEmail(rs.getString("email"));
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

        return user;
    }

    /**
     * Updates the last_login timestamp for a user
     * 
     * @param userId User ID to update
     */
    private void updateLastLogin(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();

            // Prepare SQL statement
            String sql = "UPDATE users SET last_login = ? WHERE user_id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(2, userId);

            // Execute the update
            stmt.executeUpdate();

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
     * Inner class to represent a user
     */
    private class User {
        private int userId;
        private String studentNumber;
        private String name;
        private String surname;
        private String email;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getStudentNumber() {
            return studentNumber;
        }

        public void setStudentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

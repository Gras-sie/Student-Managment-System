package com.bcstudent.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage PostgreSQL database connections.
 */
public class DBConnection {
    // Database connection parameters
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/bcstudent";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";
    
    // Connection pool parameters
    private static final int MAX_POOL_SIZE = 10;
    private static Connection[] connectionPool = new Connection[MAX_POOL_SIZE];
    private static boolean[] connectionInUse = new boolean[MAX_POOL_SIZE];
    
    static {
        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            // Initialize the connection pool
            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                connectionPool[i] = null;
                connectionInUse[i] = false;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC driver not found: " + e.getMessage());
        }
    }
    
    /**
     * Get a database connection from the pool.
     * 
     * @return A database connection
     * @throws SQLException If a database access error occurs
     */
    public static synchronized Connection getConnection() throws SQLException {
        // Look for an available connection in the pool
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            if (!connectionInUse[i]) {
                // If the connection doesn't exist or is closed, create a new one
                if (connectionPool[i] == null || connectionPool[i].isClosed()) {
                    connectionPool[i] = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                }
                
                // Mark the connection as in use
                connectionInUse[i] = true;
                return connectionPool[i];
            }
        }
        
        // If no connections are available, create a new one (not pooled)
        System.out.println("Warning: Connection pool is full, creating a non-pooled connection");
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
    
    /**
     * Release a connection back to the pool.
     * 
     * @param connection The connection to release
     */
    public static synchronized void releaseConnection(Connection connection) {
        // Look for the connection in the pool
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            if (connectionPool[i] == connection) {
                // Mark the connection as not in use
                connectionInUse[i] = false;
                return;
            }
        }
        
        // If the connection is not in the pool, close it
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing non-pooled connection: " + e.getMessage());
        }
    }
    
    /**
     * Close all connections in the pool.
     */
    public static synchronized void closeAllConnections() {
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            if (connectionPool[i] != null) {
                try {
                    connectionPool[i].close();
                    connectionPool[i] = null;
                    connectionInUse[i] = false;
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
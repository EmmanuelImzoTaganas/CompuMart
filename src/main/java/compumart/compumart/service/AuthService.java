package compumart.compumart.service;

import compumart.compumart.model.User;
import java.sql.*;

public class AuthService {
    private DatabaseService dbService = DatabaseService.getInstance();

    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = dbService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            System.out.println("Executing login query for: " + email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setRole(rs.getString("role"));
                System.out.println("Login successful for: " + user.getEmail());
                return user;
            } else {
                System.out.println("Login failed - no user found with these credentials");
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(User user, String password) {
        String sql = "INSERT INTO users (email, password, first_name, last_name, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, password);
            pstmt.setString(3, user.getFirstName());
            pstmt.setString(4, user.getLastName());
            pstmt.setString(5, "customer");

            System.out.println("Attempting to register user: " + user.getEmail());
            System.out.println("First name: " + user.getFirstName() + ", Last name: " + user.getLastName());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Registration query executed. Rows affected: " + rowsAffected);

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (Connection conn = dbService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            System.out.println("Email " + email + " exists: " + exists);
            return exists;

        } catch (SQLException e) {
            System.err.println("Email check error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Test database connection
    public void testConnection() {
        try (Connection conn = dbService.getConnection()) {
            System.out.println("✅ Database connection test: SUCCESS");
            System.out.println("Database URL: " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            System.err.println("❌ Database connection test: FAILED");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Check database health
    public boolean isDatabaseHealthy() {
        try (Connection conn = dbService.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT 1");
            System.out.println("✅ Database health check: PASSED");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Database health check failed: " + e.getMessage());
            return false;
        }
    }
}
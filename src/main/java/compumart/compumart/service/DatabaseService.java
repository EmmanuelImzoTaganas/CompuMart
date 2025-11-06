package compumart.compumart.service;

import java.sql.*;

public class DatabaseService {
    private static final String DB_URL = "jdbc:sqlite:compumart.db";
    private static DatabaseService instance;

    static {
        try {
            // Force driver registration
            Class.forName("org.sqlite.JDBC");
            System.out.println("✅ SQLite JDBC driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ SQLite JDBC driver not found: " + e.getMessage());
            System.err.println("Make sure sqlite-jdbc is in your dependencies");
        }
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        System.out.println("🔗 Database connection established to: " + DB_URL);
        return conn;
    }

    public void initializeDatabase() {
        String[] createTables = {
                """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                first_name TEXT,
                last_name TEXT,
                role TEXT DEFAULT 'customer',
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """
        };

        try (Connection conn = getConnection()) {
            System.out.println("🔄 Initializing database...");

            for (String sql : createTables) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    System.out.println("✅ Table created/verified: users");
                }
            }

            // Insert sample admin user
            insertAdminUser(conn);

            System.out.println("✅ Database initialized successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void insertAdminUser(Connection conn) throws SQLException {
        String sql = "INSERT OR IGNORE INTO users (email, password, first_name, last_name, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "admin@compumart.com");
            pstmt.setString(2, "admin123");
            pstmt.setString(3, "System");
            pstmt.setString(4, "Admin");
            pstmt.setString(5, "admin");

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Admin user created: admin@compumart.com / admin123");
            } else {
                System.out.println("ℹ️ Admin user already exists");
            }
        }
    }
}
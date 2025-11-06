package compumart.compumart.service;

import compumart.compumart.model.User;
import compumart.compumart.model.MongoConnection;
import compumart.compumart.SessionManager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.*;

public class AuthService {
    private MongoDatabase database;
    private MongoCollection<Document> usersCollection;

    public AuthService() {
        this.database = MongoConnection.getDatabase();
        if (database != null) {
            this.usersCollection = database.getCollection("users");
            System.out.println("✅ MongoDB AuthService initialized");
            initializeAdminUser();
        } else {
            System.err.println("❌ Failed to initialize MongoDB AuthService");
        }
    }

    public User login(String email, String password) {
        if (usersCollection == null) {
            System.err.println("❌ MongoDB collection not available");
            return null;
        }

        try {
            System.out.println("🔐 Attempting login for: " + email);

            Document userDoc = usersCollection.find(
                    and(eq("email", email), eq("password", password))
            ).first();

            if (userDoc != null) {
                User user = documentToUser(userDoc);

                // Set session
                SessionManager.getInstance().setCurrentUser(user);

                System.out.println("✅ Login successful for: " + user.getEmail());
                return user;
            } else {
                System.out.println("❌ Login failed - invalid credentials for: " + email);
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean register(User user, String password) {
        if (usersCollection == null) {
            System.err.println("❌ MongoDB collection not available");
            return false;
        }

        try {
            // Check if email already exists
            if (emailExists(user.getEmail())) {
                System.out.println("❌ Email already exists: " + user.getEmail());
                return false;
            }

            Document userDoc = new Document()
                    .append("email", user.getEmail())
                    .append("password", password)
                    .append("firstName", user.getFirstName())
                    .append("lastName", user.getLastName())
                    .append("role", "customer")
                    .append("createdAt", new java.util.Date());

            usersCollection.insertOne(userDoc);
            System.out.println("✅ User registered successfully: " + user.getEmail());
            return true;

        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExists(String email) {
        if (usersCollection == null) {
            System.err.println("❌ MongoDB collection not available");
            return false;
        }

        try {
            long count = usersCollection.countDocuments(eq("email", email));
            boolean exists = count > 0;
            System.out.println("📧 Email exists check for " + email + ": " + exists);
            return exists;
        } catch (Exception e) {
            System.err.println("❌ Email check error: " + e.getMessage());
            return false;
        }
    }

    private User documentToUser(Document doc) {
        User user = new User();
        user.setId(doc.getObjectId("_id"));
        user.setEmail(doc.getString("email"));
        user.setFirstName(doc.getString("firstName"));
        user.setLastName(doc.getString("lastName"));
        user.setRole(doc.getString("role"));
        user.setPassword(doc.getString("password"));

        // Optional fields
        if (doc.containsKey("phone")) {
            user.setPhone(doc.getString("phone"));
        }
        if (doc.containsKey("address")) {
            user.setAddress(doc.getString("address"));
        }

        return user;
    }

    private void initializeAdminUser() {
        try {
            // Check if admin user exists
            long adminCount = usersCollection.countDocuments(
                    and(eq("email", "admin@compumart.com"), eq("role", "admin"))
            );

            if (adminCount == 0) {
                Document adminDoc = new Document()
                        .append("email", "admin@compumart.com")
                        .append("password", "admin123")
                        .append("firstName", "System")
                        .append("lastName", "Admin")
                        .append("role", "admin")
                        .append("createdAt", new java.util.Date());

                usersCollection.insertOne(adminDoc);
                System.out.println("✅ Admin user created: admin@compumart.com / admin123");
            } else {
                System.out.println("ℹ️ Admin user already exists");
            }
        } catch (Exception e) {
            System.err.println("❌ Error initializing admin user: " + e.getMessage());
        }
    }

    // Test MongoDB connection
    public void testConnection() {
        try {
            if (database != null) {
                System.out.println("✅ MongoDB connection test: SUCCESS");
                System.out.println("Database: " + database.getName());

                // Test if collection exists and is accessible
                long userCount = usersCollection.countDocuments();
                System.out.println("Total users in database: " + userCount);
            } else {
                System.err.println("❌ MongoDB connection test: FAILED - No database connection");
            }
        } catch (Exception e) {
            System.err.println("❌ MongoDB connection test: FAILED - " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Check database health
    public boolean isDatabaseHealthy() {
        try {
            if (database != null) {
                database.listCollectionNames().first(); // Simple operation to test connection
                System.out.println("✅ MongoDB health check: PASSED");
                return true;
            } else {
                System.err.println("❌ MongoDB health check: FAILED - No database connection");
                return false;
            }
        } catch (Exception e) {
            System.err.println("❌ MongoDB health check failed: " + e.getMessage());
            return false;
        }
    }
}
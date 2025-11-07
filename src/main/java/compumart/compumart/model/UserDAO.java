package compumart.compumart.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import compumart.compumart.utils.MongoDBConnectionManager;

import static com.mongodb.client.model.Filters.eq;

public class UserDao {
    private final MongoCollection<Document> collection;

    /**
     * DAO constructor that dynamically chooses the database.
     * You can pass a different database if needed.
     */
    public UserDao() {
        MongoDatabase database = MongoDBConnectionManager.getDatabase("CompuMART");
        collection = database.getCollection("users");
    }

    // --- LOGIN ---
    public boolean authenticate(String email, String password) {
        Document userDoc = collection.find(eq("email", email)).first();
        if (userDoc != null) {
            String storedPassword = userDoc.getString("password");
            return password.equals(storedPassword); // Simple password comparison
        }
        return false;
    }

    // --- REGISTER ---
    public boolean createUser(String username, String email, String password, String phone) {
        Document existing = collection.find(eq("email", email)).first();
        if (existing != null) {
            return false; // Email already exists
        }

        Document newUser = new Document("username", username)
                .append("email", email)
                .append("password", password) // Store plain text password
                .append("phone", phone);

        collection.insertOne(newUser);
        return true;
    }

    // --- GET USER BY EMAIL ---
    public User getUserByEmail(String email) {
        Document userDoc = collection.find(eq("email", email)).first();
        if (userDoc != null) {
            User user = new User();
            user.setEmail(userDoc.getString("email"));
            user.setPassword(userDoc.getString("password"));
            user.setPhone(userDoc.getString("phone"));
            return user;
        }
        return null;
    }

    // --- UPDATE USER ---
    public boolean updateUser(String email, String newPassword, String newPhone) {
        Document existing = collection.find(eq("email", email)).first();
        if (existing == null) {
            return false; // User not found
        }

        Document updateFields = new Document();
        if (newPassword != null && !newPassword.isEmpty()) {
            updateFields.append("password", newPassword);
        }
        if (newPhone != null && !newPhone.isEmpty()) {
            updateFields.append("phone", newPhone);
        }

        if (!updateFields.isEmpty()) {
            Document update = new Document("$set", updateFields);
            collection.updateOne(eq("email", email), update);
            return true;
        }
        return false;
    }

    // --- DELETE USER ---
    public boolean deleteUser(String email) {
        Document result = collection.findOneAndDelete(eq("email", email));
        return result != null;
    }
}
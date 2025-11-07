package compumart.compumart.model;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class UserDAO {
    private final MongoCollection<Document> collection;

    public UserDAO() {
        MongoDatabase database = MongoConnection.getDatabase();
        collection = database.getCollection("users");
    }

    // --- LOGIN ---
    public boolean authenticate(String email, String password) {
        Document userDoc = collection.find(eq("email", email)).first();
        if (userDoc != null) {
            String storedHashedPassword = userDoc.getString("password");
            return false;
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
                .append("phone", phone);

        collection.insertOne(newUser);
        return true;
    }
}
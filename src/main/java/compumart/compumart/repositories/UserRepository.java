package compumart.compumart.repositories;

import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;
import compumart.compumart.model.User;

public class UserRepository extends BaseRepository<User> {

    public UserRepository() {
        super();
        initCollection("users");
    }

    @Override
    protected User convert(Document document) {
        User user = new User();
        user.setId(document.getObjectId("_id"));
        user.setfName(document.getString("fName"));
        user.setlName(document.getString("lName"));
        user.setEmail(document.getString("email"));
        user.setPassword(document.getString("password"));
        user.setPhone(document.getString("phone"));
        user.setAddress(document.getString("address"));
        user.setCreatedAt(document.getDate("createdAt"));
        user.setRole(document.getString("role"));
        return user;
    }


    public User findByEmail(String email) {
        Document document = this.collection.find(eq("email", email)).first();
        return document == null ? null : convert(document);
    }
}

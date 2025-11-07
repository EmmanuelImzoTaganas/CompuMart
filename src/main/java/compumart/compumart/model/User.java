package compumart.compumart.model;

import org.bson.Document;
import java.util.Date;

public class User extends BaseModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String role;
    private Date createdAt;

    // Default constructor
    public User() {
        this.role = "user";
        this.createdAt = new Date();
    }

    // Convenience constructor
    public User(String firstName, String lastName, String email, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ⚠️ Note: Store hashed passwords only in production
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // Helper method for displaying the full name
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    // Convert User to MongoDB Document
    @Override
    public Document toDocument() {
        Document doc = new Document();
        if (getId() != null) {
            doc.append("_id", getId());
        }
        doc.append("firstName", firstName)
                .append("lastName", lastName)
                .append("email", email)
                .append("password", password)
                .append("phone", phone)
                .append("address", address)
                .append("role", role)
                .append("createdAt", createdAt);
        return doc;
    }

    // Convert MongoDB Document to User
    public static User fromDocument(Document doc) {
        if (doc == null) return null;

        User user = new User();
        if (doc.containsKey("_id")) user.setId(doc.getObjectId("_id"));
        user.setFirstName(doc.getString("firstName"));
        user.setLastName(doc.getString("lastName"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));
        user.setPhone(doc.getString("phone"));
        user.setAddress(doc.getString("address"));
        user.setRole(doc.getString("role"));
        user.setCreatedAt(doc.getDate("createdAt"));
        return user;
    }

    // For debugging and logging
    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
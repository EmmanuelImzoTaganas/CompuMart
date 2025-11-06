package compumart.compumart.model;

import org.bson.types.ObjectId;

public class User {
    private ObjectId id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String phone;
    private String address;

    public User() {}

    public User(String email, String firstName, String lastName, String role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // Getters and setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isAdmin() {
        return "admin".equals(role);
    }
}
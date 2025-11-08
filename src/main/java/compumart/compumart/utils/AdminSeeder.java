package compumart.compumart.utils;

import compumart.compumart.model.User;
import compumart.compumart.repositories.UserRepository;

import java.util.Date;

public class AdminSeeder {

    private final UserRepository userRepository = new UserRepository();

    public void seedAdmin() {
        String adminEmail = "admin";

        // Check if admin already exists
        User existing = userRepository.findByEmail(adminEmail);
        if (existing != null) {
            System.out.println("Admin already exists.");
            return;
        }

        // Create admin user
        User admin = new User();
        admin.setfName("Admin");
        admin.setlName("User");
        admin.setEmail(adminEmail);
        admin.setPassword(PasswordHasher.hashPassword("1234")); // default password
        admin.setPhone("0000000001");
        admin.setAddress("Head Office");
        admin.setRole("admin");
        admin.setCreatedAt(new Date());

        userRepository.insert(admin);
        System.out.println("Admin seeded successfully.");
    }

    public void removeAdmin() {
        String adminEmail = "admin@compumart.com";
        User admin = userRepository.findByEmail(adminEmail);
        if (admin != null) {
            userRepository.delete(admin.getId());
            System.out.println("Admin removed successfully.");
        } else {
            System.out.println("Admin does not exist.");
        }
    }

    // For testing
    public static void main(String[] args) {
        AdminSeeder seeder = new AdminSeeder();
        seeder.seedAdmin();
        // seeder.removeAdmin(); // Uncomment to delete admin
    }
}

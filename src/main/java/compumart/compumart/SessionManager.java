package compumart.compumart;

import compumart.compumart.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        System.out.println("Session started for: " + (user != null ? user.getFullName() : "null"));
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public void logout() {
        System.out.println("Logging out user: " + (currentUser != null ? currentUser.getFullName() : "null"));
        currentUser = null;
    }
}
package application.session;

import application.datamodel.User;

public class UserSession {
	
    // 1. Private static instance (Singleton)
    private static UserSession instance;

    // 2. Private field to store the current user
    private User currentUser;

    // 3. Private constructor to prevent instantiation
    private UserSession() {
    }

    // 4. Public static method to get the single instance of UserSession
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // 5. Methods to set and get the user
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}

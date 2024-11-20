package application.controller;

import application.datamodel.*;
import application.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class AdminScreenController {
    private User user;
    @FXML
    public TextArea admin_username;

    // The initialize method is automatically called after the FXML file is loaded
    @FXML
    public void initialize() {
        // Make sure the user session is accessed here
    	
        user = UserSession.getInstance().getCurrentUser();
        if (user != null) {
            System.out.println(user.getUsername());
            admin_username.setText(user.getName()); // Display username on the label
        } else {
            System.out.println("No user found in session");
        }
    }
}

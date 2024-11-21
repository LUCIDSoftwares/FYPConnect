package application.controller;

import application.datamodel.*;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class AdminScreenController {
    private User user;
    @FXML
    public TextArea admin_username;

    @FXML
    public Button logoutButton;
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

    @FXML
    public void handleLogout(ActionEvent e) {
        try {
            // Clear the current user session
            UserSession.getInstance().setCurrentUser(null);
            
			Parent root = FXMLLoader.load(getClass().getResource("/resources/views/loginScreen.fxml"));
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

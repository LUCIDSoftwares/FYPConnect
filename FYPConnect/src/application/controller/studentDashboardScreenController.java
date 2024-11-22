package application.controller;

import application.datamodel.User;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class studentDashboardScreenController {
	private User user;
	
	@FXML
	public TextArea student_username;
	
	public studentDashboardScreenController() {

	}
	
    @FXML
    public void initialize() {
        // Make sure the user session is accessed here
    	
        user = UserSession.getInstance().getCurrentUser();
        if (user != null) {
            System.out.println(user.getUsername());
            student_username.setText(user.getName()); // Display username on the label
        } else {
            System.out.println("No user found in session");
        }
    }
    
    @FXML
    public void handleLogout(ActionEvent e) {
        try {
        	System.out.println("Logout Button Clicked");
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

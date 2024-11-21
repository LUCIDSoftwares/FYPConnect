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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminDashboardScreenController {
    private User user;
    
    @FXML
    public BorderPane bp;
    
    @FXML
    public AnchorPane ap;
    
    @FXML
    public TextArea admin_username;

    @FXML
    public Button logoutButton;
    
    @FXML
    public Button HomeButton;
    
    @FXML
    public Button CreateUsersButton;
    
    @FXML
    public Button UpdateUsersButton;
    
    @FXML
    public Button DeleteUsersButton;
    
    @FXML
    public Button DisplayUsersButton;
    
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
    
    @FXML
    public void home(MouseEvent me) {
    	System.out.println("Home Button Clicked");
    }
    
    @FXML
	public void createUsers(MouseEvent me) {
    	System.out.println("Create Users Button Clicked");
	}
    
    @FXML
        public void updateUsers(MouseEvent me) {
    	System.out.println("Update Users Button Clicked");
    }
    
    @FXML
	public void deleteUsers(MouseEvent me) {
System.out.println("Delete Users Button");
	}
    
    @FXML
        public void displayUsers(MouseEvent me) {
    		System.out.println("Display Users Button");
    }
    
	private void loadPage(String page) {
		try {
            Parent root = FXMLLoader.load(getClass().getResource("/resources/views/" + page + ".fxml"));
            bp.setCenter(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
    
    
    
    

}

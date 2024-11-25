package application.controller;

import application.datamodel.*;
import application.persistance.DBHandler;
import application.services.LoginService;
import application.session.UserSession;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreenController {
	
	@FXML
	public TextField usernameTextField;
	
	@FXML
	public PasswordField passwordTextField;
	public TextArea failedTextBox;
	
	
	LoginService loginService;
	
	public LoginScreenController() {
		
    }
	
	public void login(ActionEvent e) {
		String username = usernameTextField.getText();
		String password = passwordTextField.getText();
		if(username == null || username.isEmpty()) {
			failedTextBox.setText("Username is empty");
			return;
		}
		if(password == null || password.isEmpty()) {
			failedTextBox.setText("Password is empty");
			return;
		}
		
		LoginService loginService = new LoginService();
		User user = loginService.login(username, password);
		
		if (user == null) {
			failedTextBox.setText("Invalid username or password");
			return;
		}
		else {
			UserSession.getInstance().setCurrentUser(user);
			if (user instanceof Admin) {
				try {

					Parent root = FXMLLoader.load(getClass().getResource("/resources/views/adminDashboardScreen.fxml"));
					Scene scene = new Scene(root);
					Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					stage.setScene(scene);
					
					stage.show();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if(user instanceof Faculty){
				try {
					Parent root = FXMLLoader.load(getClass().getResource("/resources/views/facultyHomeScreen.fxml"));
					Scene scene = new Scene(root);
					Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					stage.setScene(scene);
					stage.show();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			else {
				try {
					Parent root = FXMLLoader.load(getClass().getResource("/resources/views/studentHomeScreen.fxml"));
					Scene scene = new Scene(root);
					Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					stage.setScene(scene);
					stage.show();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		
		
	}
	
}

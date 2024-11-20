package application.controller;

import application.services.LoginService;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreenController {
	
	@FXML
	public TextField usernameTextField;
	
	@FXML
	public TextField passwordTextField;
	
	LoginService loginService;
	
	public LoginScreenController() {
		
    }
	
	public void login(ActionEvent e) {
		
		if(usernameTextField.getText() == null || usernameTextField.getText().isEmpty()) {
			System.out.println("User Id is empty");
			return;
		}
		if(passwordTextField.getText() == null || passwordTextField.getText().isEmpty()) {
			System.out.println("password is empty");
			return;
		}
		
		
		
		
	}
	
}

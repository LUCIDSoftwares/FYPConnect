package application.controller;

import application.datamodel.User;
import application.services.AdminService;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AdminCreateUsersScreenController {

	@FXML
	private AnchorPane ap;
	@FXML
	private TextField nameTextField, usernameTextField, passwordTextField, emailTextField, cgpaTextField;
//	@FXML
//	private TextField usernameTextField;
//	@FXML
//	private TextField passwordTextField;
//	@FXML
//	private TextField emailTextField;
//	@FXML
//	private TextField cgpaTextField;
	@FXML
	private RadioButton studentRButton, facultyRButton, adminRButton;  
//	@FXML
//	private RadioButton facultyRButton;
//	@FXML
//	private RadioButton amdinRButton;
	@FXML
	private Button createUsersButton;
	@FXML
	private TextArea messageBoxTextArea;
	
    private User user;
	
	public AdminCreateUsersScreenController() {};
	
	@FXML
	public void initialize() {
		user = UserSession.getInstance().getCurrentUser();
		if(user == null)
			System.out.println("No User in the System");
	};
	
	private void clearAll() {
		// clear all textfields
		
		nameTextField.clear();
		usernameTextField.clear();
		passwordTextField.clear();
		emailTextField.clear();
		cgpaTextField.clear();
		messageBoxTextArea.clear();
	};
//	
//	public static boolean isValidEmailAddress(String email) {
//		   boolean result = true;
//		   try {
//		      InternetAddress emailAddr = new InternetAddress(email);
//		      emailAddr.validate();
//		   } catch (AddressException ex) {
//		      result = false;
//		   }
//		   return result;
//		}
	
    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
	
	
	public void userTypeSelected(ActionEvent event) {
		if(studentRButton.isSelected())
			cgpaTextField.setEditable(true);
		else {
			cgpaTextField.setEditable(false);
		
		}
		this.clearAll();
	}
	
	public void createUser(ActionEvent event) {
		
		// validate the user type
		if(!studentRButton.isSelected() && !facultyRButton.isSelected() && !adminRButton.isSelected()) {
			messageBoxTextArea.setText("Please Select a User Type");
			return;
		}
		
		// validate user input
		String name = nameTextField.getText();
		String username = usernameTextField.getText();
		String password = passwordTextField.getText();
		String email = emailTextField.getText();
		if(name == null || name.trim().isEmpty()) {
			messageBoxTextArea.setText("Please Enter Name");
			return;
		}
		else if(username == null || username.trim().isEmpty()) {
			messageBoxTextArea.setText("Please Enter Username");
			return;
		}
		else if(password == null || password.trim().isEmpty()) {
			messageBoxTextArea.setText("Please Enter Password");
			return;
		}
		else if(email == null || email.trim().isEmpty()) {
			messageBoxTextArea.setText("Please Enter Email");
			return;
		}		
		double cgpa = 0.0;
		if(studentRButton.isSelected()) {
			try {
			cgpa = Double.parseDouble(cgpaTextField.getText());
		
			} catch (NumberFormatException e) {
				messageBoxTextArea.setText("Invalid CGPA");
				return;
			}
		}
		if(this.isValidEmailAddress(email) == false) {
			messageBoxTextArea.setText("Please Enter a Valid Email");
			return;			
		}
		
		String userType;
		if(studentRButton.isSelected())
			userType = "Student";
		else if(facultyRButton.isSelected())
			userType = "Faculty";
		else
			userType = "Admin";
		
		AdminService adminService = new AdminService();
		int userId = adminService.createUser(userType, name, username, password, email, cgpa);
		
		if(userId > 0) {
			//this.clearAll();
			messageBoxTextArea.setText("User successfully created with ID: " + userId);
		}
		else
			messageBoxTextArea.setText("User cannot be created. Invalid Credentials");
	
	}
	
}

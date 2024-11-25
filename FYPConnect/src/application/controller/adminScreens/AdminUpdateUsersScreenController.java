package application.controller.adminScreens;

import application.services.AdminService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AdminUpdateUsersScreenController {

    @FXML
    private AnchorPane ap;
    @FXML
    private TextField cgpaTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextArea messageBoxTextArea;
    @FXML
    private Button updateUserButton;
    @FXML
    private TextField usernameTextField;
    
    
    private AdminService adminService;
    
    public AdminUpdateUsersScreenController() {}
    
    @FXML
    public void initialize() {
    	this.adminService =  new AdminService();
    }
    
    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    
    @FXML
    void updateUser(ActionEvent event) {
    	
    	String username = this.usernameTextField.getText();
    	if(username == null || username.trim().isEmpty() || username.trim().isBlank()) {
			messageBoxTextArea.setText("Please Enter Username");
			return;
    	}
    	username = username.trim();
    	
    	String name = this.nameTextField.getText();
    	if(name != null && !name.trim().isEmpty() && !name.trim().isBlank()) {
    		name = name.trim();
    	}
    	else
    		name = null;
    	
    	String email = this.emailTextField.getText();
    	if(email != null && !email.trim().isEmpty() && !email.trim().isBlank()) {
    		email = email.trim();
    		if(this.isValidEmailAddress(email) == false) {
    			this.messageBoxTextArea.setText("Invalid Email address can not update");
    			return;
    		}
    	}
    	else
    		email = null;
    	
    	String password = this.passwordTextField.getText();
    	if(password != null && !password.trim().isEmpty() && !password.trim().isBlank())
    		password = password.trim();
    	else
    		password = null;
    	
    	double cgpa = -1;
    	
    	try {
    		if(cgpaTextField.getText() != null && !cgpaTextField.getText().trim().isEmpty())
    			cgpa = Double.parseDouble(cgpaTextField.getText());
		} catch (NumberFormatException e) {
			messageBoxTextArea.setText("Invalid CGPA");
			return;
		}
    	
    	int flag; 
    			
    	if(name != null || password != null || email != null || cgpa >= 0.0)
    		flag = this.adminService.updateUser(username, name, email, password, cgpa);
    	else {
    		this.messageBoxTextArea.setText("No information added to updated");
    		return;
    	}
    	
    	if(flag == -10)
    		this.messageBoxTextArea.setText("Cannot Update the GPA for a Faculty Member. Enter valid Credentials");
    	else if(flag == -5)
    		this.messageBoxTextArea.setText("Integrity Constraint Violation. You have entered values which "
    				+ "are potentially duplicate. Check the Display Users Tab for better information");
    	else if(flag == -3)
    		this.messageBoxTextArea.setText("Unable to connect to the Database");
    	else if(flag == -2)
    		this.messageBoxTextArea.setText("User does not exist with the given Username");
    	else if(flag == -1)
    		this.messageBoxTextArea.setText("User exists, but is an Admin. Can not update an Admin");
    	else if(flag == 0)
    		this.messageBoxTextArea.setText("Unable to update User");
    	else
    		this.messageBoxTextArea.setText("User Updated Successfully with the provided details");
    }

}


package application.controller.adminScreens;

import application.services.AdminService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AdminDeleteUsersScreenController {

    @FXML
    private AnchorPane ap;
    @FXML
    private Button deleteUserButton;
    @FXML
    private TextArea messageBoxTextArea;
    @FXML
    private TextField userIdTextField;
    @FXML
    private TextField usernameTextField;

    public AdminDeleteUsersScreenController() {}
    
    private AdminService adminService;
    
    @FXML
    public void initialize() {
    	this.adminService = new AdminService();
    }
    
    @FXML
    void deleteUser(ActionEvent event) {
    	this.messageBoxTextArea.clear();
    	
    	int userId = -1;
    	
    	if(this.userIdTextField.getText() != null && !this.userIdTextField.getText().trim().isBlank()
    			&& !this.userIdTextField.getText().trim().isEmpty()) {
    		
    		try {
    			userId = Integer.parseInt(this.userIdTextField.getText());
    		}
    		catch (NumberFormatException e) {
    			this.messageBoxTextArea.setText("You have entered incorret format for the User Id");
    			return;
    		}
    	}
    	
    	String username = this.usernameTextField.getText();
    	if(username != null && !username.trim().isBlank() && !username.trim().isEmpty())
    		username = username.trim();
    	else
    		username = null;
    	
    	if(userId <= 0 && username == null) {
    		this.messageBoxTextArea.setText("Please either enter User Id or Username to delete a User");
    		return;
    	}
    	else if(userId > 0 && username != null) {
    		this.messageBoxTextArea.setText("Please only enter one identifying credential");
    		return;
    	}
    	else if(userId > 0) {
    		
    		int flag = this.adminService.deleteUserbyId(userId);
    		if(flag == -1)
    			this.messageBoxTextArea.setText("Issue in Database. Unable to proceed");
    		else if(flag == 0)
    			this.messageBoxTextArea.setText("No User exists in the System with the User Id");
    		else if(flag == 1)
    			this.messageBoxTextArea.setText("The User is an Administrator. You can not delete and Administrator");
    		else if(flag == 2)
    			this.messageBoxTextArea.setText("The User is involved in a Project. Can not delete it");
    		else if(flag == 3)
    			this.messageBoxTextArea.setText("The User is in a group. Can not delete it");
    		else
    			this.messageBoxTextArea.setText("User successfully deleted with the given User Id");

    	}
    	else {
    		int flag = this.adminService.deleteUserByUsername(username);
    		if(flag == -1)
    			this.messageBoxTextArea.setText("Issue in Database. Unable to proceed");
    		else if(flag == 0)
    			this.messageBoxTextArea.setText("No User exists in the System with the Username");
    		else if(flag == 1)
    			this.messageBoxTextArea.setText("The User is an Administrator. You can not delete and Administrator");
    		else if(flag == 2)
    			this.messageBoxTextArea.setText("The User is involved in a Project. Can not delete it");
    		else if(flag == 3)
    			this.messageBoxTextArea.setText("The User is in a group. Can not delete it");
    		else
    			this.messageBoxTextArea.setText("User successfully deleted with the given Username");
    	}
    	
    }

}


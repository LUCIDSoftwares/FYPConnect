package application.controller;

import java.util.ArrayList;

import application.datamodel.Admin;
import application.datamodel.Student;
import application.datamodel.User;
import application.services.AdminService;
import application.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class AdminDisplayUsersScreenController {
	
    @FXML
    private AnchorPane ap;
    @FXML
    private RadioButton studentRButton, facultyRButton, adminRButton;
    @FXML
    private Button usernameSearchButton, userTypeSearchButton;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private TableView<User> userInfoTableView;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private ToggleGroup userType;
    @FXML
    private TextField usernameSearchTextField;

    private TableColumn<User, Double> cgpaColumn;
    
    private AdminService adminService;
    //private Admin admin;
    
    ObservableList<User> list = null;
    
    public AdminDisplayUsersScreenController() {};
    
    public void initialize() {
    	
    	nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
    	passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
    	emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
    	usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
    	userIdColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
    	
    	this.cgpaColumn = new TableColumn<>("CGPA");
    	this.cgpaColumn.setCellValueFactory(new PropertyValueFactory<User, Double>("cgpa"));
    	
    	this.adminService = new AdminService();
    	
    };
    
    
    
    public void populateByUsername(ActionEvent event) {
    	
    	// clear the table
    	userInfoTableView.getItems().clear();
    
    	// turn off the radio buttons
    	this.studentRButton.setSelected(false);
    	this.facultyRButton.setSelected(false);
    	this.adminRButton.setSelected(false);
    	
    	// get username entered by user
    	if(this.usernameSearchTextField.getText() == null || this.usernameSearchTextField.getText().trim().isEmpty()) {
    		this.messageTextArea.setText("Please Enter a Username");
    		return;
    	}
    	
    	String username = this.usernameSearchTextField.getText().trim();
    	
    	// get user from the database if it exists
    	User user = this.adminService.getUserByUsername(username);
    	if(user == null) {
    		this.messageTextArea.setText("No User exists in the System with the given Username");
    		return;
    	}
    	
    	// now make appropriate changes to display the user accordingly;
    	if(user instanceof Student) {
    		// nothing here
    		if(!userInfoTableView.getColumns().contains(this.cgpaColumn))
    			userInfoTableView.getColumns().add(this.cgpaColumn);
    	}
    	else {
        	// remove the extra table column
        	if(userInfoTableView.getColumns().contains(this.cgpaColumn))
        		userInfoTableView.getColumns().remove(this.cgpaColumn);
    	}
    	
    	// pass user data
    	this.list = FXCollections.observableArrayList(user);
    	
    	// display the data now
    	userInfoTableView.setItems(list);
    	this.messageTextArea.clear();
    }
    
    // will populate the TableView with the information of a particular user type    
    public void populateByUserType(ActionEvent event) {
    	
    	// clear the table
    	userInfoTableView.getItems().clear();
    
    	// clear if it is not empty
    	this.usernameSearchTextField.clear();
    	
    	// first check if a radio button has been selected
    	if(!this.studentRButton.isSelected() && !this.facultyRButton.isSelected() && !this.adminRButton.isSelected()) {
    		this.messageTextArea.setText("Please Select a User Type");
    		return;
    	}
    		
    	// get selected choice
    	String userType;
    	if(this.studentRButton.isSelected())
    		userType = "Student";
    	else if(this.facultyRButton.isSelected())
    		userType = "Faculty";
    	else
    		userType = "Admin";
    	
    	// get user from the database and convert it into an observable list
    	ArrayList<User> userArrayList = this.adminService.getUserArrayList(userType);
    	if(userArrayList == null) {
    		this.messageTextArea.setText("No Users exist in the System with the selected User Type");
    		return;
    	}
    	else
    		this.list = FXCollections.observableArrayList(adminService.getUserArrayList(userType));

    	// now make appropriate changes to display the user accordingly;
    	if(userType.equalsIgnoreCase("Student")) {
    		// if the users are of student, then add the cgpa column else remove it
    		if(!userInfoTableView.getColumns().contains(this.cgpaColumn))
    			userInfoTableView.getColumns().add(this.cgpaColumn);
    	}
    	else {
        	// remove the extra table column
        	if(userInfoTableView.getColumns().contains(this.cgpaColumn))
        		userInfoTableView.getColumns().remove(this.cgpaColumn);
    	}
    	
    	// display the data now
    	userInfoTableView.setItems(list);
    	this.messageTextArea.clear();
    }
    

}

package application.controller.studentViews;

import java.util.ArrayList;

import application.datamodel.Project;
import application.datamodel.Resource;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;
import application.services.ConcreteUserFactory;
import application.services.ProjectHandler;
import application.services.ResourceHandler;
import application.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ProjectScreenController {

    @FXML
    private TableColumn<Project, String> DescriptionColumn;
    @FXML
    private AnchorPane ap;
    @FXML
    private RadioButton allAvailableProjectsRButton, projectDeclinedRequestsRButton, pendingProjectRequestRButton;
    @FXML
    private Button displayProjectButton;
    @FXML
    private ToggleGroup projectDisplayType;
    @FXML
    private TableView<Project> projectTableView;
    @FXML
    private TableColumn<Project, String> facultyNameColumn;
    @FXML
    private TableColumn<Project, String> facultyUsernameColumn;
    @FXML
    private TableColumn<Project, Integer> projectIdColumn;
    @FXML
    private TextField projectNameTextField;
    @FXML
    private Button projectRequestButton;
    @FXML
    private Button proposalDownloadButton;
    @FXML
    private TableColumn<Project, String> titleColumn;
    @FXML
    private TextArea promptTextArea;
    
    private ObservableList<Project> list = null;
    private boolean isTheUserAGroupLeader, isInAGroup, isGroupComplete;
    
    private ProjectHandler projectHandler;
    
    public ProjectScreenController() {}
    
    @FXML
    public void initialize() {
    	this.projectIdColumn.setCellValueFactory(new PropertyValueFactory<Project, Integer>("id"));
    	this.titleColumn.setCellValueFactory(new PropertyValueFactory<Project, String>("title"));
    	this.DescriptionColumn.setCellValueFactory(new PropertyValueFactory<Project, String>("description"));
    	this.facultyNameColumn.setCellValueFactory(new PropertyValueFactory<Project, String>("faculty_name"));
    	this.facultyUsernameColumn.setCellValueFactory(new PropertyValueFactory<Project, String>("faculty_username"));
    	
    	
    	this.projectHandler = new ProjectHandler();
    	this.allAvailableProjectsRButton.setSelected(true);
    	this.displayProjects(null);
    
    	
    	PersistanceHandler dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
    	

    	// Check if the given person is in a group
    	String groupName = dbHandler.getGroupName(UserSession.getInstance().getCurrentUser().getUsername());
    	if(groupName.equalsIgnoreCase("No Group Selected") == true) {
    		this.isInAGroup = false; 
    		this.isGroupComplete = false;
    		this.isTheUserAGroupLeader = false;
    		this.projectRequestButton.setDisable(true);
    		return;
    	}
    	else
    		this.isInAGroup = true;

    	// check if the group is complete
    	String[] groupMembers = dbHandler.getGroupMembers(groupName);
    	if(groupMembers[0].equalsIgnoreCase("No Leader Found") == true) {
    		this.isGroupComplete = false;
    		this.isTheUserAGroupLeader = false;
    		this.projectRequestButton.setDisable(true);
    		return;
    	}
    	else
    		isGroupComplete = true;
    	
    	// check if the given person is the group leader
    	if(dbHandler.isGroupLeader(UserSession.getInstance().getCurrentUser().getUsername()) == true)
    		this.isTheUserAGroupLeader = true;
    	else
    		this.isTheUserAGroupLeader = false;
    	
    	if(this.isTheUserAGroupLeader == false)
    		this.projectRequestButton.setDisable(true);
    }
    
    public void displayProjects(MouseEvent me) {
    	this.promptTextArea.clear();
    	this.projectTableView.getItems().clear();
    	
    	ArrayList<Project> projectArrayList = null;
		String projectTitle = this.projectNameTextField.getText();
		if(projectTitle == null || projectTitle.isBlank() || projectTitle.isEmpty() || projectTitle.equals("")) {
			projectTitle = null;
			this.projectNameTextField.clear();
		}
		else
			projectTitle = projectTitle.trim();
    	
    	if(this.allAvailableProjectsRButton.isSelected()) {
    		projectArrayList = this.projectHandler.getAllAvailableProjects(projectTitle);
    		if(this.isTheUserAGroupLeader == true)
    			this.projectRequestButton.setDisable(false);
    	}
    	// display all the projects against which the user has sent a mentorship request
    	else if(this.pendingProjectRequestRButton.isSelected()) {
    		projectArrayList = this.projectHandler.getAllProjectsWithMentorshipRequest(projectTitle);
    		this.projectRequestButton.setDisable(true);
    	}
    	else {
    		projectArrayList = this.projectHandler.getAllProjectsWithDeclinedRequests(projectTitle);
    		this.projectRequestButton.setDisable(true);
    	}
    	
    	this.displayProjectsInTable(projectArrayList);
    }
    
    public void displayProjectsInTable(ArrayList<Project> availableProjectsArrayList) {
    	if(availableProjectsArrayList == null) {
    		this.promptTextArea.setText("No Resources in the System. Nothing to Display");
    		return;
    	}
  	
    	this.list = FXCollections.observableArrayList(availableProjectsArrayList);  	
    	projectTableView.setItems(list);
    	
    	return;
    }
    
    public void displayAllAvailableProjects(ArrayList<Project> availableProjectsArrayList) {

    	
    	if(availableProjectsArrayList == null) {
    		this.promptTextArea.setText("No Resources in the System. Nothing to Display");
    		return;
    	}
  	
    	this.list = FXCollections.observableArrayList(availableProjectsArrayList);  	
    	projectTableView.setItems(list);
    	
    	return;
    }

    @FXML
    public void sendProjectRequest(MouseEvent me) {
    	// first get the project selected by the user
    	Project project = this.projectTableView.getSelectionModel().getSelectedItem();
    	
    	// if the user has not selected a row
    	if(project == null) {
    		this.promptTextArea.setText("Please select a Project to send a Request against");
    		return;
    	}
    	
    	// un-select the choice
    	this.projectTableView.getSelectionModel().clearSelection();
    	
    	this.projectHandler.createMentorshipRequest(project.getId());
    	
    	// refresh the table
    	this.displayProjects(null);
    }
    
    public void downloadProjectProposal(MouseEvent me) {
    	// get the selected resource
    	Project project = this.projectTableView.getSelectionModel().getSelectedItem();
    	
    	// if the user has not selected a row
    	if(project == null) {
    		this.promptTextArea.setText("Please select a Project to Download its Proposal");
    		return;
    	}
    	
    	// un-select the choice
    	this.projectTableView.getSelectionModel().clearSelection();
    	
    	ResourceHandler resourceHandler = new ResourceHandler(); 
    	
    	// download the file
    	String newFilePath = resourceHandler.downloadResource(project.getTitle(), project.getDoc_link());
    	if(newFilePath == null)
    		this.promptTextArea.setText("Unable to Download File. Please Try Again Later");
    	else
    		this.promptTextArea.setText("The Project Proposal has been downloaded successfully."
    				+ "You can see the file at " + newFilePath);
    }
    
}


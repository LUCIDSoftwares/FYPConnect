 package application.controller.studentViews;

import application.datamodel.Project;
import application.services.ProjectHandler;
import application.services.ResourceHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ProjectInfoScreenController {

    @FXML
    private AnchorPane ap;
    @FXML
    private Button downloadButton;
    @FXML
    private TextField nameTextArea;
    @FXML
    private TextArea projectDescriptionTextArea;
    @FXML
    private TextField projectIdTextArea;
    @FXML
    private TextField projectTitleTextArea;
    @FXML
    private TextField usernameTextArea;
    @FXML
    private TextArea promptTextArea;
    
    public ProjectInfoScreenController() {}
    
    private Project project;
    
    @FXML
    public void initialize() {
    	ProjectHandler projectHandler = new ProjectHandler();
    	
    	this.project = projectHandler.getProjectInformation();
    	
    	this.projectIdTextArea.setText(Integer.toString(this.project.getId()));
    	this.projectTitleTextArea.setText(this.project.getTitle());
    	this.projectDescriptionTextArea.setText(this.project.getDescription());
    	this.usernameTextArea.setText(this.project.getFaculty_username());
    	this.nameTextArea.setText(this.project.getFaculty_name());
    }
    
    @FXML
    public void downloadProposal(MouseEvent me) {
    	ResourceHandler resourceHandler = new ResourceHandler(); 
    	
    	// download the file
    	String newFilePath = resourceHandler.downloadResource(project.getTitle(), project.getDoc_link());
    	if(newFilePath == null)
    		this.promptTextArea.setText("Unable to Download File. Please Try Again Later");
    	else
    		this.promptTextArea.setText("The Project Proposal has been downloaded successfully."
    				+ "You can see the file at " + newFilePath);
    	
    	return;
    }

}


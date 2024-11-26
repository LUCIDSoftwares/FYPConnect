package application.controller;

import java.util.ArrayList;

import application.datamodel.Mentorship_Request;
import application.datamodel.Project;
import application.services.ProjectHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class HandleMentorshipScreenController {

    @FXML
    private RadioButton acceptedRButton;
    @FXML
    private Button accpetButton;
    @FXML
    private AnchorPane ap;
    @FXML
    private Button declineButton;
    @FXML
    private RadioButton declinedRButton;
    @FXML
    private TableColumn<Mentorship_Request, Integer> groupIdColumn;
    @FXML
    private TableColumn<Mentorship_Request, String> groupNameColumn;
    @FXML
    private TableColumn<Mentorship_Request, Integer> leaderIdColumn;
    @FXML
    private TableColumn<Mentorship_Request, Integer> member1IdColumn;
    @FXML
    private TableColumn<Mentorship_Request, Integer> member2IdColumn;
    @FXML
    private TableColumn<Mentorship_Request, Integer> mentIdColumn;
    @FXML
    private ToggleGroup mentorshipType;
    @FXML
    private RadioButton pendingRButton;
    @FXML
    private TableColumn<Mentorship_Request, Integer> projIdColumn;
    @FXML
    private TableColumn<Mentorship_Request, String> projectTitleColumn;
    @FXML
    private TableView<Mentorship_Request> requestTableView;
    @FXML
    private TextArea textArea;
    private ObservableList<Mentorship_Request> list = null;
    private ProjectHandler projectHandler;


    public HandleMentorshipScreenController() {}
    
    @FXML
    public void initialize() {
    	this.mentIdColumn.setCellValueFactory(new PropertyValueFactory<Mentorship_Request, Integer>("ID"));
    	this.projIdColumn.setCellValueFactory(new PropertyValueFactory<Mentorship_Request, Integer>("Project_ID"));
    	this.groupIdColumn.setCellValueFactory(new PropertyValueFactory<Mentorship_Request, Integer>("GroupID"));
    	this.groupNameColumn.setCellValueFactory(new PropertyValueFactory<Mentorship_Request, String>("groupName"));
    	this.projectTitleColumn.setCellValueFactory(new PropertyValueFactory<Mentorship_Request, String>("projectTitle"));
    	this.leaderIdColumn.setCellValueFactory(new PropertyValueFactory<Mentorship_Request, Integer>("leaderId"));
    	this.member1IdColumn.setCellValueFactory(new PropertyValueFactory<Mentorship_Request, Integer>("member1Id"));
    	this.member2IdColumn.setCellValueFactory(new PropertyValueFactory<Mentorship_Request, Integer>("member2Id"));
    	
    	this.projectHandler = new ProjectHandler();
    	this.pendingRButton.setSelected(true);
    	this.displayPassedMentorshipRequests(this.projectHandler.getAllPendingMentorshipRequest());
    	
    }
    
    public void displayPassedMentorshipRequests(ArrayList<Mentorship_Request> mentArray) {
    	if(mentArray == null) {
    		this.textArea.setText("No Relevant Requests to Show");
    		return;
    	}
    	
    	this.textArea.clear();
    	this.requestTableView.getItems().clear();
    	
    	this.list = FXCollections.observableArrayList(mentArray);  	
    	requestTableView.setItems(list);
    }
    
    @FXML
    public void displayMentorshipRequests(ActionEvent event) {
    	ArrayList<Mentorship_Request> mentArray = null;
    	if(this.acceptedRButton.isSelected())
    		mentArray = this.projectHandler.getAllAcceptedMentorshipRequest();
    	else if(this.declinedRButton.isSelected())
    		mentArray = this.projectHandler.getAllDeclinedMentorshipRequest();
    	else
    		mentArray = this.projectHandler.getAllPendingMentorshipRequest();
    	
    	this.displayPassedMentorshipRequests(mentArray);
    }
    
    @FXML
    void accepMentorShipRequest(ActionEvent event) {
    	this.textArea.clear();
    	
    	Mentorship_Request request = this.requestTableView.getSelectionModel().getSelectedItem();
    	
    	if(request == null) {
    		this.textArea.setText("Please select a Mentorship request before accpeting or declining");
    		return;
    	}
    	
    	// un-select the choice
    	this.requestTableView.getSelectionModel().clearSelection();
    	
    	if(this.projectHandler.acceptMentorshipRequest(request.getID()))
    		this.textArea.setText("Request Accepted Successfully");
    	
    	// refresh the table
    	this.displayMentorshipRequests(null);
    }

    @FXML
    void declineMentorshipRequest(ActionEvent event) {
    	this.textArea.clear();
    	
    	Mentorship_Request request = this.requestTableView.getSelectionModel().getSelectedItem();
    	
    	if(request == null) {
    		this.textArea.setText("Please select a Mentorship request before accpeting or declining");
    		return;
    	}
    	
    	// un-select the choice
    	this.requestTableView.getSelectionModel().clearSelection();
    	
    	if(this.projectHandler.declineMentorshipRequest(request.getID()))
    		this.textArea.setText("Request Declined Successfully");
    	
    	// refresh the table
    	this.displayMentorshipRequests(null);
    }

}


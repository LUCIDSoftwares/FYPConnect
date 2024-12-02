package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import application.datamodel.User;
import application.persistance.DBHandler;
import application.session.UserSession;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeStudentsController {

    @FXML
    private ComboBox<String> deliverableComboBox;

    @FXML
    private ComboBox<String> groupNameComboBox;

    @FXML
    private TextField submissionContentLinkTextField;

    @FXML
    private TextField submissionTimeTextField;

    @FXML
    private TextField gradeTextField;

    @FXML
    private TextArea remarksTextArea;

    @FXML
    private Button submitButton;

    private DBHandler dbHandler;
    private int selectedDeliverableId = -1;
    private int selectedGroupId = -1;
    
    int facultyID = UserSession.getInstance().getCurrentUser().getId();


    @FXML
    public void initialize() {
        dbHandler = new DBHandler();
        dbHandler.establishConnection();
        loadDeliverables();
        loadGroups();
    }

    private void loadDeliverables() {
        try {
            ResultSet rs = dbHandler.getDeliverablesByFaculty(facultyID); 
            while (rs.next()) {
                deliverableComboBox.getItems().add(rs.getString("description"));
                deliverableComboBox.setUserData(rs.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadGroups() {
        try {
            ResultSet rs = dbHandler.getGroups();
            while (rs.next()) {
                groupNameComboBox.getItems().add(rs.getString("name"));
                groupNameComboBox.setUserData(rs.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onDeliverableSelected() {
        String selectedDescription = deliverableComboBox.getValue(); 

        selectedDeliverableId = dbHandler.getDeliverableId(selectedDescription);
		/*
		 * fetchSubmissionDetails();
		 */    }

    @FXML
    private void onGroupSelected() {
    	String selectedname = groupNameComboBox.getValue(); 
        selectedGroupId = dbHandler.getGroupId(selectedname);
		/*
		 * fetchSubmissionDetails();
		 */    }
    private void fetchSubmissionDetails() {
    	
    	System.out.println("hello1"+selectedDeliverableId+selectedGroupId);
    	
    	
        if (selectedDeliverableId != -1 && selectedGroupId != -1) {
            try {
                ResultSet rs = dbHandler.getSubmissionContentLink(selectedDeliverableId, selectedGroupId);
                if (rs.next()) {
                    submissionContentLinkTextField.setText(rs.getString("content_link"));
                    submissionTimeTextField.setText(rs.getString("submission_time"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onSubmit(ActionEvent event) {
        String grade = gradeTextField.getText();
        String remarks = remarksTextArea.getText();

        if (selectedDeliverableId != -1 && selectedGroupId != -1 && !grade.isEmpty()) {
            try {
                boolean success = dbHandler.saveFeedback(selectedGroupId, facultyID, selectedDeliverableId, grade, remarks);
                if (success) {
                    showAlert("Success", "Feedback saved successfully.");
                } else {
                    showAlert("Error", "Failed to save feedback.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Validation Error", "Please fill all required fields.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @FXML
    private void fetchSubmissionDetails(ActionEvent event) {
        if (deliverableComboBox.getValue() != null && groupNameComboBox.getValue() != null) {
        	onGroupSelected();
        	onDeliverableSelected();
        	fetchSubmissionDetails();
        } else {
            showAlert("Error", "Please select both a deliverable and a group.");
        }
    }

}

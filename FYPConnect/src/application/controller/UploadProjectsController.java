package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.persistance.DBHandler;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UploadProjectsController implements Initializable {

    @FXML
    private TextField titleField; // Matches fx:id="titleField" in FXML

    @FXML
    private TextArea descriptionField; // Matches fx:id="descriptionField" in FXML

    @FXML
    private TextField docLinkField; // Matches fx:id="docLinkField" in FXML

	/*
	 * @Override public void initialize(URL location, ResourceBundle resources) { //
	 * Any necessary initialization logic (currently unused) }
	 */
	/*
	 * @FXML
	 */    public void initialize(URL location, ResourceBundle resources) {
        titleField.setOnMouseClicked(event -> System.out.println("Title field clicked"));
        descriptionField.setOnMouseClicked(event -> System.out.println("Description field clicked"));
        docLinkField.setOnMouseClicked(event -> System.out.println("Doc Link field clicked"));
		/*
		 * submitButton.setOnMouseClicked(event ->
		 * System.out.println("Submit button clicked"));
		 */    }

    @FXML
    void handleSubmit(ActionEvent event) {
        // Get inputs from the form
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String docLink = docLinkField.getText().trim();

        // Validate required fields
        if (title.isEmpty() || description.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Title and Description are required!");
            return;
        }

        try {
            // Get faculty ID from the user session
            int facultyID = UserSession.getInstance().getCurrentUser().getId();

            // Save to the database
            DBHandler dbHandler = new DBHandler();
            boolean isSaved = dbHandler.saveProject(title, description, docLink, facultyID);

            if (isSaved) {
                showAlert(AlertType.INFORMATION, "Success", "Project uploaded successfully!");
                clearFields(); // Clear fields after successful submission
            } else {
                showAlert(AlertType.ERROR, "Database Error", "Failed to upload the project. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void clearFields() {
        // Clear all input fields
        titleField.clear();
        descriptionField.clear();
        docLinkField.clear();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

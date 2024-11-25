package application.controller;

import application.persistance.DBHandler;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UploadResourcesController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField filePathField;

    private final DBHandler dbHandler;

    public UploadResourcesController() {
        this.dbHandler = new DBHandler();
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        // Retrieve user session details
        String uploaderUsername = UserSession.getInstance().getCurrentUser().getUsername();

        // Retrieve input values
        String title = titleField.getText();
        String description = descriptionField.getText();
        String filePath = filePathField.getText();

        // Input validation
        if (title.isEmpty() || description.isEmpty() || filePath.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        // Save resource to the database
        boolean isUploaded = dbHandler.saveResource(title, description, filePath, uploaderUsername);
        if (isUploaded) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Resource uploaded successfully.");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload resource. Please try again.");
        }
    }

    // Utility to clear input fields after successful submission
    private void clearFields() {
        titleField.clear();
        descriptionField.clear();
        filePathField.clear();
    }

    // Utility to display alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

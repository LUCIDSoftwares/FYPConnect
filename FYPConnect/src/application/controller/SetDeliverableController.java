package application.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import application.persistance.DBHandler;
import application.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SetDeliverableController {

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private TextField docLinkField;
    @FXML
    private Button submitbutton;

    private DBHandler dbHandler = new DBHandler();

    @FXML
    public void handleSubmit() {
        // Retrieve input values
        String description = descriptionField.getText().trim();
        LocalDate deadline = deadlinePicker.getValue();
        String docLink = docLinkField.getText().trim();

        if (description.isEmpty() || deadline == null) {
            showAlert("Error", "Description and Deadline are required.", Alert.AlertType.ERROR);
            return;
        }

        // Get the current faculty ID from the user session
        int facultyID = UserSession.getInstance().getCurrentUser().getId();

        // Convert LocalDate to LocalDateTime and format to match MySQL DATETIME
        LocalDateTime deadlineWithTime = deadline.atStartOfDay(); // Adds a default time (00:00:00)
        String formattedDeadline = deadlineWithTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Save to database
        boolean isSuccess = dbHandler.saveDeliverable(formattedDeadline, description, docLink, facultyID);
        if (isSuccess) {
            showAlert("Success", "Deliverable set successfully.", Alert.AlertType.INFORMATION);
            clearFields();
        } else {
            showAlert("Error", "Failed to set deliverable. Try again.", Alert.AlertType.ERROR);
        }
    }


    private void clearFields() {
        descriptionField.clear();
        deadlinePicker.setValue(null);
        docLinkField.clear();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

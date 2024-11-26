package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SetTimelineController {

    // FXML references
    @FXML
    private TextField quarter1Details;
    @FXML
    private TextField quarter2Details;
    @FXML
    private TextField quarter3Details;
    @FXML
    private TextField quarter4Details;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    // File path where the details will be saved
    private static final String FILE_PATH = "src/timeline_details.txt";

    @FXML
    private void initialize() {
        // Save button action
        saveButton.setOnAction(event -> saveTimelineDetails());

        // Cancel button action (Optional: Clear fields or close the form)
        cancelButton.setOnAction(event -> clearFields());
    }

    /**
     * Saves the timeline details into a file, overwriting any existing content.
     */
    private void saveTimelineDetails() {
        String quarter1 = quarter1Details.getText().trim();
        String quarter2 = quarter2Details.getText().trim();
        String quarter3 = quarter3Details.getText().trim();
        String quarter4 = quarter4Details.getText().trim();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("Quarter 1: " + quarter1 + System.lineSeparator());
            writer.write("Quarter 2: " + quarter2 + System.lineSeparator());
            writer.write("Quarter 3: " + quarter3 + System.lineSeparator());
            writer.write("Quarter 4: " + quarter4 + System.lineSeparator());

            showAlert(Alert.AlertType.INFORMATION, "Success", "Timeline details saved successfully!");

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save timeline details!");
            e.printStackTrace();
        }
    }

    /**
     * Clears the text fields.
     */
    private void clearFields() {
        quarter1Details.clear();
        quarter2Details.clear();
        quarter3Details.clear();
        quarter4Details.clear();
    }

    /**
     * Displays an alert box with the given parameters.
     * @param alertType The type of alert (e.g., INFORMATION, ERROR).
     * @param title The title of the alert box.
     * @param message The message to display.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

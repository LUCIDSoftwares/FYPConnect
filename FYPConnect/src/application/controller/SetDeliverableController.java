package application.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import application.persistance.DBHandler;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class SetDeliverableController {

	private File selectedFile; 			// to store the selected file
	@FXML
	private Button uploadDocumentButton;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker deadlinePicker;
    @FXML
    private TextField docLinkField;
    @FXML
    private Button submitbutton;

    private DBHandler dbHandler = new DBHandler();

    // function to be called when the user tries to upload a document
    @FXML
    public void uploadDocument(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Upload");
        
        // set filters for file types (optional)
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Files", "*.*"),
            new FileChooser.ExtensionFilter("Text Files", "*.txt"),
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        
        // Open file chooser dialog
        selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No File Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a file to upload.");
            alert.showAndWait();
        }
        else {
        	docLinkField.setText(selectedFile.getName());
        	Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("File Selected");
            alert.setHeaderText(null);
            alert.setContentText("File selected: " + selectedFile.getName());
            alert.showAndWait();
        }     	
    }
    
    @FXML
    public void handleSubmit() {
        // Retrieve input values
        String description = descriptionField.getText().trim();
        LocalDate deadline = deadlinePicker.getValue();
        //String docLink = docLinkField.getText().trim();

        // input validation
        if (description.isEmpty() || deadline == null) {
            showAlert("Error", "Description and Deadline are required.", Alert.AlertType.ERROR);
            return;
        }
        // Get the current faculty ID from the user session
        int facultyID = UserSession.getInstance().getCurrentUser().getId();

        String filePath = null;
        
        // if the file is given, then do operations accordingly
        if(this.selectedFile != null) {
            
        	// generate a unique filename using a time stamp
            String originalFileName = selectedFile.getName();
            // extract the extension
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            // extract the file name excluding the extension
            originalFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            originalFileName = originalFileName.replaceAll("\\s","");
            // get time stamp in the given format
            String timestamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
            String newFileName = originalFileName + timestamp + extension;

    		// Define the path to copy the file to
            String resourcesPath = "/resources";
            File resourcesDir = new File(resourcesPath );
            if (!resourcesDir.exists()) {
                resourcesDir.mkdir(); 		// Create resources folder if it doesn't exist
            }
            File destinationFile = new File(resourcesDir, newFileName);
        	 
            // copy the file accordingly
            try {
                // copy the file to the resources folder
                java.nio.file.Files.copy(selectedFile.toPath(), destinationFile.toPath());
                System.out.println("File copied successfully to: " + destinationFile.getAbsolutePath());

                // save the resource details to the database through the PersistenceHandler
                filePath = resourcesPath + "/" + newFileName;
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("File Upload Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to copy the file. Please try again.");
                alert.showAndWait();
            }
        }
        
        // Convert LocalDate to LocalDateTime and format to match MySQL DATETIME
        LocalDateTime deadlineWithTime = deadline.atStartOfDay(); // Adds a default time (00:00:00)
        String formattedDeadline = deadlineWithTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Save to database
        boolean isSuccess = dbHandler.saveDeliverable(formattedDeadline, description, filePath, facultyID);
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

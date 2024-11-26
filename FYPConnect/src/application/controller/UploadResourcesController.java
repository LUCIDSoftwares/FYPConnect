package application.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import application.persistance.DBHandler;
import application.services.ResourceHandler;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class UploadResourcesController {

    private File selectedFile; // To store the selected file
	@FXML
	private Button uploadFileButton;
    @FXML
    private TextField titleField;
//    @FXML
//    private TextField promptText2;
    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField filePathField;

    private final DBHandler dbHandler;

    public UploadResourcesController() {
        this.dbHandler = new DBHandler();
    }

    @FXML
    public void uploadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Upload");
        
        // Set filters for file types (optional)
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
        
        //else (selectedFile != null) {
        else {
        	filePathField.setText(selectedFile.getName());
        	Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("File Selected");
            alert.setHeaderText(null);
            alert.setContentText("File selected: " + selectedFile.getName());
            alert.showAndWait();

        } 
//        else {
//            Alert alert = new Alert(AlertType.WARNING);
//            alert.setTitle("No File Selected");
//            alert.setHeaderText(null);
//            alert.setContentText("Please select a file to upload.");
//            alert.showAndWait();
//        }
    }
    
    @FXML
    private void handleSubmit(ActionEvent event) {
    	// Retrieve user session details
        String uploaderUsername = UserSession.getInstance().getCurrentUser().getUsername();

        // Retrieve input values
        String title = titleField.getText();
        String description = descriptionField.getText();
        //String filePath = filePathField.getText();

        // Input validation
        if (title.isEmpty() || description.isEmpty() ){
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }
        else {
        	title = title.trim();
        	description = description.trim();
        }
        
        // ensure file has been uploaded
        if (selectedFile == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No File Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a file first.");
            alert.showAndWait();
            return;
        }

        
        /////////// agar issue ho tou ye remove kardo
        ResourceHandler resourceHandler = new ResourceHandler();
        boolean isUploaded = resourceHandler.handleSubmit(title, description, selectedFile);
        
        if (isUploaded) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Submission Successful");
            alert.setHeaderText(null);
            alert.setContentText("File uploaded and resource recorded successfully.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save resource details to the database.");
            alert.showAndWait();
        }
        ///////////
        
        
        // once merged do all of this in the ResourceHandler Class
        // pass title, description, and selected file
        
        // generate a unique filename using a time stamp
//        String originalFileName = selectedFile.getName();
//        // extract the extension
//        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
//        // extract the file name excluding the extension
//        originalFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
//        originalFileName = originalFileName.replaceAll("\\s","");
//        // get time stamp in the given format
//        String timestamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
//        String newFileName = originalFileName + timestamp + extension;
//
//		// Define the path to copy the file to
//        String resourcesPath = "/resources";
//        File resourcesDir = new File(resourcesPath );
//        if (!resourcesDir.exists()) {
//            resourcesDir.mkdir(); 		// Create resources folder if it doesn't exist
//        }
//        File destinationFile = new File(resourcesDir, newFileName);
//        
//        
//        try {
//            // copy the file to the resources folder
//            java.nio.file.Files.copy(selectedFile.toPath(), destinationFile.toPath());
//            System.out.println("File copied successfully to: " + destinationFile.getAbsolutePath());
//
//            // save the resource details to the database through the PersistenceHandler
//            String filePath = resourcesPath + "/" + newFileName;
//            //Date currentTimestamp = new Date();
//            
//            //boolean isSaved = dbHandler.saveSubmission(delId, this.groupId, currentTimestamp, fileLink);
//            boolean isUploaded = dbHandler.saveResource(title, description, filePath, uploaderUsername);
//            
//            if (isUploaded) {
//                Alert alert = new Alert(AlertType.INFORMATION);
//                alert.setTitle("Submission Successful");
//                alert.setHeaderText(null);
//                alert.setContentText("File uploaded and resource recorded successfully.");
//                alert.showAndWait();
//            } else {
//                Alert alert = new Alert(AlertType.ERROR);
//                alert.setTitle("Database Error");
//                alert.setHeaderText(null);
//                alert.setContentText("Failed to save resource details to the database.");
//                alert.showAndWait();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("File Upload Error");
//            alert.setHeaderText(null);
//            alert.setContentText("Failed to copy the file. Please try again.");
//            alert.showAndWait();
//        }

//        // Save resource to the database
//        boolean isUploaded = dbHandler.saveResource(title, description, filePath, uploaderUsername);
//        if (isUploaded) {
//            showAlert(Alert.AlertType.INFORMATION, "Success", "Resource uploaded successfully.");
//            clearFields();
//        } else {
//            showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload resource. Please try again.");
//        }
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

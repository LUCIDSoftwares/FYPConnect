package application.controller;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import application.persistance.DBHandler;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UploadProjectsController implements Initializable {
	
	private File selectedFile; // To store the selected file
    @FXML
    private TextField titleField; // Matches fx:id="titleField" in FXML
	@FXML
	private Button uploadFileButton;
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
	public void uploadFile(ActionEvent event) {
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Select File to Upload");
	        
	        // Set filters for file types (optional)
		 fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("All Files", "*.*"),
	        new FileChooser.ExtensionFilter("Text Files", "*.txt"),
	        new FileChooser.ExtensionFilter("PDF Files", "*.pdf") );
	        
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
    void handleSubmit(ActionEvent event) {
        // Get inputs from the form
    	String title;
    	String description;
//        String title = titleField.getText().trim();
//        String description = descriptionField.getText().trim();
//        String docLink = docLinkField.getText().trim();

        // Validate required fields
        if(titleField.getText() == null || titleField.getText().trim().isBlank() ||
        	descriptionField.getText() == null || descriptionField.getText().trim().isBlank()) {
            showAlert(AlertType.ERROR, "Input Error", "Title and Description are required!");
            return;
        }
        else {
        	title = titleField.getText().trim();
        	description = descriptionField.getText().trim();
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
//        // Validate required fields
//        if (title.isEmpty() || description.isEmpty()) {
//            showAlert(AlertType.ERROR, "Input Error", "Title and Description are required!");
//            return;
//        }

        // Get faculty ID from the user session
        int facultyID = UserSession.getInstance().getCurrentUser().getId();
        
        // once merged do all of this in the ResourceHandler Class
        // pass title, description, facultyID and selected file
        
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
        
        try {
            // copy the file to the resources folder
            java.nio.file.Files.copy(selectedFile.toPath(), destinationFile.toPath());
            System.out.println("File copied successfully to: " + destinationFile.getAbsolutePath());

            // save the resource details to the database through the PersistenceHandler
            String filePath = resourcesPath + "/" + newFileName;
        	
            // Save to the database
        	DBHandler dbHandler = new DBHandler();
            boolean isSaved = dbHandler.saveProject(title, description, filePath, facultyID);

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

package application.controller.studentViews;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import application.datamodel.Deliverable;
import application.datamodel.Resource;
import application.datamodel.User;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;
import application.services.DeliverableHandler;
import application.services.ResourceHandler;
import application.session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class submitDelController {
    private File selectedFile; // To store the selected file
    private String groupId;       // Group ID (already in the class)
    private int deliverableId;
    PersistanceHandler dbHandler;
    
    @FXML
    private TableColumn<Deliverable, LocalDateTime> DeadlineColumn;

    @FXML
    private TableColumn<Deliverable, Integer> Del_IdColumn;

    @FXML
    private TableColumn<Deliverable, String> DescriptionColumn;

    @FXML
    private Button SubmitButton;

    @FXML
    private Button download_deliverable_button;

    @FXML
    private TableView<Deliverable> resourceTableView;

    @FXML
    private Button selectFileButton;

    @FXML
    public TextArea promptTextArea;
    
    private ObservableList<Deliverable> list = null;
    
    @FXML
    private TableColumn<Deliverable, String> uploaderUsernameColumn;

    private DeliverableHandler resourceHandler;
    
    User user;
    
    @FXML
    private TextField promptText2;
    
	public submitDelController() {
		this.resourceHandler = new DeliverableHandler();
	}
    
    @FXML
    public void initialize() {
    	user = UserSession.getInstance().getCurrentUser();
    	this.Del_IdColumn.setCellValueFactory(new PropertyValueFactory<Deliverable, Integer>("deliverableId"));
    	this.DescriptionColumn.setCellValueFactory(new PropertyValueFactory<Deliverable, String>("description"));
    	this.DeadlineColumn.setCellValueFactory(new PropertyValueFactory<Deliverable, LocalDateTime>("deadline"));
    	this.uploaderUsernameColumn.setCellValueFactory(new PropertyValueFactory<Deliverable, String>("facultyId"));
    	 dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
    	resourceHandler = new DeliverableHandler();
    	ArrayList<Deliverable> resourceArrayList = resourceHandler.getAllDeliverables();
    	//deliverableId = resourceArrayList.get(0).getDeliverableId();
    	groupId = dbHandler.getGroupName(user.getUsername());
    	
    	this.populateTableWithResources(resourceArrayList);
    }
    
    public void populateTableWithResources(ArrayList<Deliverable> resourceArrayList) {
        
    	resourceTableView.getItems().clear();
    
    	if(resourceArrayList == null) {
    		promptTextArea.setText("No Resources in the System. Nothing to Display");
    		return;
    	}
  	
    	this.list = FXCollections.observableArrayList(resourceArrayList);  	
    	resourceTableView.setItems(list);
    	
    	return;
    }
    
    @FXML
    void SelectFile(ActionEvent event) {
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
        promptText2.setText("File selected: " + selectedFile.getName());
        if (selectedFile != null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("File Selected");
            alert.setHeaderText(null);
            alert.setContentText("File selected: " + selectedFile.getName());
            alert.showAndWait();

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No File Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a file to upload.");
            alert.showAndWait();
        }

        
    }
    
    @FXML
    void Submit(ActionEvent event) {
        if (selectedFile == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No File Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a file first.");
            alert.showAndWait();
            return;
        }
		if (resourceTableView.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No Resource Selected");
			alert.setHeaderText(null);
			alert.setContentText("Please select a deliverable first.");
			alert.showAndWait();
			return;
		}
        int delId = this.resourceTableView.getSelectionModel().getSelectedItem().getDeliverableId();
        // Generate a unique filename using a timestamp
        String originalFileName = selectedFile.getName();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String timestamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        String newFileName = "submission_" + timestamp + extension;

        String resourcesPath = "/resources";
		// Define the path to copy the file to
        File resourcesDir = new File(resourcesPath );
        if (!resourcesDir.exists()) {
            resourcesDir.mkdir(); // Create resources folder if it doesn't exist
        }
        File destinationFile = new File(resourcesDir, newFileName);

        try {
            // Copy the file to the resources folder
            java.nio.file.Files.copy(selectedFile.toPath(), destinationFile.toPath());
            System.out.println("File copied successfully to: " + destinationFile.getAbsolutePath());

            // Save the submission details to the database through the PersistenceHandler
            String fileLink = resourcesPath + "/" + newFileName;
            Date currentTimestamp = new Date();
        	
        	
        	
            boolean isSaved = dbHandler.saveSubmission(delId, this.groupId, currentTimestamp, fileLink);

            if (isSaved) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Submission Successful");
                alert.setHeaderText(null);
                alert.setContentText("File uploaded and submission recorded successfully.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to save submission details to the database.");
                alert.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("File Upload Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to copy the file. Please try again.");
            alert.showAndWait();
        }
    }


    @FXML
    void downloadDeliverable(ActionEvent event) {
    	downloadResource();
    }
    
    // will download the selected resource
    public void downloadResource() {
    	// get the selected resource
    	Deliverable rs = this.resourceTableView.getSelectionModel().getSelectedItem();
    	
    	// if the user has not selected a row
    	if(rs == null) {
    		this.promptTextArea.setText("Please select a Resource to Download");
    		return;
    	}
    	
    	// un-select the choice
    	this.resourceTableView.getSelectionModel().clearSelection();
    	
    	//convert int to string function in java: String.valueOf(int)
    	
    	// download the file
    	String newFilePath = this.resourceHandler.downloadResource(String.valueOf(rs.getDeliverableId()), rs.getDocLink());
    	if(newFilePath == null)
    		this.promptTextArea.setText("Unable to Download File. Please Try Again Later");
    	else
    		this.promptTextArea.setText("The Resource has been downloaded successfully."
    				+ "You can see the file at " + newFilePath);
    	
    	return;
    }

}

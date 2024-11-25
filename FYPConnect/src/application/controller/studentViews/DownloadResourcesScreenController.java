package application.controller.studentViews;

import java.util.ArrayList;

import application.datamodel.Resource;
import application.services.ResourceHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class DownloadResourcesScreenController {

	@FXML
    private AnchorPane ap;
	@FXML
	private Button downloadFileButton, searchButton;
	@FXML
	private TextField titleTextField;
	@FXML
	private TextArea promptTextArea;
    @FXML
    private TableView<Resource> resourceTableView;
	@FXML
    private TableColumn<Resource, String> descriptionColumn;
    @FXML
    private TableColumn<Resource, Integer> resourceIdColumn;
    @FXML
    private TableColumn<Resource, String> titleColumn;
    @FXML
    private TableColumn<Resource, String> uploaderUsernameColumn;
    
    private ObservableList<Resource> list = null;
    private ResourceHandler resourceHandler;
    
    public DownloadResourcesScreenController() {}
    
    @FXML
    public void initialize() {
    	this.resourceIdColumn.setCellValueFactory(new PropertyValueFactory<Resource, Integer>("resourceId"));
    	this.titleColumn.setCellValueFactory(new PropertyValueFactory<Resource, String>("title"));
    	this.descriptionColumn.setCellValueFactory(new PropertyValueFactory<Resource, String>("description"));
    	this.uploaderUsernameColumn.setCellValueFactory(new PropertyValueFactory<Resource, String>("uploaderUsername"));
    	 
    	resourceHandler = new ResourceHandler();
    	ArrayList<Resource> resourceArrayList = resourceHandler.getAllResources();
    	
    	this.populateTableWithResources(resourceArrayList);
    }
	
    
    // get the list of files with the matching title and then populate the table accordingly
    public void searchFilesByTitle(ActionEvent event) {
    	this.promptTextArea.clear();

    	ArrayList<Resource> resourceArrayList = null;
    	String resourceTitle = this.titleTextField.getText();
    	    	
    	if(resourceTitle == null || resourceTitle.isBlank() || resourceTitle.isEmpty() || resourceTitle.equals("")) {
    		resourceArrayList = this.resourceHandler.getAllResources();
    		this.titleTextField.clear();
    	}
    	else
    		resourceArrayList = this.resourceHandler.getResourcesByTitle(resourceTitle.trim());
    	
    	this.populateTableWithResources(resourceArrayList);
    	
    	return;
    }
    
    // populate the table with the provided list of resources
    public void populateTableWithResources(ArrayList<Resource> resourceArrayList) {
    
    	resourceTableView.getItems().clear();
    	
    	if(resourceArrayList == null) {
    		this.promptTextArea.setText("No Resources in the System. Nothing to Display");
    		return;
    	}
  	
    	this.list = FXCollections.observableArrayList(resourceArrayList);  	
    	resourceTableView.setItems(list);
    	
    	return;
    }
    
    // will download the selected resource
    public void downloadResource(ActionEvent event) {
    	// get the selected resource
    	Resource rs = this.resourceTableView.getSelectionModel().getSelectedItem();
    	
    	// if the user has not selected a row
    	if(rs == null) {
    		this.promptTextArea.setText("Please select a Resource to Download");
    		return;
    	}
    	
    	// un-select the choice
    	this.resourceTableView.getSelectionModel().clearSelection();
    	
    	// download the file
    	String newFilePath = this.resourceHandler.downloadResource(rs.getTitle(), rs.getFilePath());
    	if(newFilePath == null)
    		this.promptTextArea.setText("Unable to Download File. Please Try Again Later");
    	else
    		this.promptTextArea.setText("The Resource has been downloaded successfully."
    				+ "You can see the file at " + newFilePath);
    	
    	return;
    }
    
    public void runButton() {
    	Resource rs = this.resourceTableView.getSelectionModel().getSelectedItem();
    	this.resourceTableView.getSelectionModel().clearSelection();
    	
    	
    	System.out.println(rs.getFilePath());
    }
    
}

package application.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import application.datamodel.User;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FacultyScreenController {
	private User user;

	
	  @FXML 
	  public BorderPane bp;
	 

	  @FXML
	  private AnchorPane contentplane;

	/*
	 * @FXML public TextArea faculty_username;
	 */
	@FXML
	public Button CloseButton;

	@FXML
	public Button DashboardButton;

	@FXML
	public Button UploadprojectsButton;

	@FXML
	public Button SetDeliverableButton;
	
	@FXML
	public Button UpdateResourceButton;

	@FXML
	public Button GradeStudentsButton;

	@FXML
	public Button SettimelineButton;

	@FXML
	public Button MentorshipreqButton;
	
	@FXML
	public TextField faculty_username;
	

	

	// The initialize method is automatically called after the FXML file is loaded
	@FXML
	public void initialize() {
		// Make sure the user session is accessed here

		user = UserSession.getInstance().getCurrentUser();
		if (user != null) {
			System.out.println(user.getUsername());
			/*
			 * faculty_username.setText(user.getName()); // Display username on the label
			 */ } else {
			System.out.println("No user found in session");
		}
		faculty_username.setText(user.getName());
		
		
	}

	@FXML
	public void handleLogout(ActionEvent e) {
		try {
			System.out.println("Logout Button Clicked");
			// Clear the current user session
			UserSession.getInstance().setCurrentUser(null);
			clearSessionFile();
			Parent root = FXMLLoader.load(getClass().getResource("/resources/views/loginScreen.fxml"));
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public static void clearSessionFile() {
        File file = new File("src/application/session/lastSession.log");
        try {
            // Open FileWriter in overwrite mode (default)
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(""); // Writing an empty string clears the file
            fileWriter.close();
            System.out.println("Session cleared successfully");
        } catch (IOException e) {
            System.out.println("Error clearing session: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
	@FXML
	public void Dashboard1(ActionEvent me) {
		bp.setCenter(contentplane);
		System.out.println("Dashboard Button Clicked");
	}

	@FXML
	void handleUploadProject(ActionEvent event) {
		System.out.println("Upload Projects Button Clicked");

		// Load the Upload Projects FXML
		loadPage("uploadProjects");
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/uploadProjects.fxml"));
//			Parent root = loader.load();
//
//			// Create a new scene for the Upload Projects screen
//			Scene uploadProjectScene = new Scene(root);
//
//			// Get the current stage
//			Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
//
//			// Set the new scene
//			currentStage.setScene(uploadProjectScene);
//			currentStage.setTitle("Upload Project");
	}

	@FXML
	public void UploadProjects(ActionEvent me) {
		System.out.println("Upload Projects Button Clicked");
	    loadPage("uploadProjects");

		/*
		 * SceneLoader.loadScene("/resources/views/uploadprojects.fxml", event); //
		 * Utility to load scenes
		 */
	}

	@FXML
	public void UploadResources(ActionEvent me) {
		System.out.println("Update Resources Button Clicked");
	    loadPage("uploadResources");

	}

	@FXML
	public void GradeStudents(ActionEvent me) {
		System.out.println("Grade Students Button");
	    loadPage("Gradestudents");

	}
	
	@FXML
	public void Setdeliverable1(ActionEvent me) {
		System.out.println("Set deliverable Button Clicked");
	    loadPage("setdeliverable");
	}


	@FXML
	public void SetTimeline(ActionEvent me) {
		System.out.println("Set Timeline Button");
	    loadPage("setTimeline");

	}
	
	@FXML
	public void showMentorship(ActionEvent event) {
		loadPage("handleMentorshipScreen");
	}

	private void loadPage(String resource) {
	    Parent root = null;
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/views/" + resource + ".fxml"));
	        root = loader.load();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return; // Exit the method if loading fails
	    }

	    if (bp == null) {
	        System.out.println("Error: BorderPane 'bp' is null. Check FXML binding.");
	        return;
	    }
	    
	    bp.setCenter(root);
	}


	/*
	 * private void loadPage(String page) { try { Parent root =
	 * FXMLLoader.load(getClass().getResource("/resources/views/" + page +
	 * ".fxml")); bp.setCenter(root); } catch (Exception ex) { ex.printStackTrace();
	 * } }
	 */
}
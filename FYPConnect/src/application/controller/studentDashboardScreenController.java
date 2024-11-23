package application.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import application.datamodel.User;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class studentDashboardScreenController {
	private User user;
	PersistanceHandler dbHandler;

	@FXML
	public Button HomeButton;

	@FXML
	public Button CreateGroupButton;

	@FXML
	public Button JoinGroupButton;

	@FXML
	public AnchorPane ap;

	@FXML
	public BorderPane bp;

	@FXML
	public TextArea student_username;

	public studentDashboardScreenController() {

	}

	@FXML
	public void initialize() {
		// Make sure the user session is accessed here
		dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
		user = UserSession.getInstance().getCurrentUser();
		if (user != null) {
			System.out.println(user.getUsername());
			student_username.setText(user.getName()); // Display username on the label
		} else {
			System.out.println("No user found in session");
		}
		if (dbHandler.getGroupName(user.getUsername()) != "No Group Selected"
				&& dbHandler.isGroupLeader(user.getUsername())) {
			CreateGroupButton.setText("Delete Group");
		} else {
			CreateGroupButton.setText("Create Group");
		}
		HomeButton.fire();
		loadPage("studentHome");
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
	public void home(MouseEvent me) {
		System.out.println("Home Button Clicked");
		if (dbHandler.getGroupName(user.getUsername()) != "No Group Selected"
				&& dbHandler.isGroupLeader(user.getUsername())) {
			CreateGroupButton.setText("Delete Group");
		} else {
			CreateGroupButton.setText("Create Group");
		}
		loadPage("studentHome");

	}

	@FXML
	public void createGroups(MouseEvent me) {
		System.out.println("Create Groups Button Clicked");
		// if user is already in a group, then they cannot create a group, instead will
		// load delete group page
		if (dbHandler.getGroupName(user.getUsername()) != "No Group Selected"
				&& dbHandler.isGroupLeader(user.getUsername())) {
			CreateGroupButton.setText("Delete Group");
			loadPage("deleteGroups");
		} else {
			CreateGroupButton.setText("Create Group");
			loadPage("createGroups");
		}

	}

	@FXML
	public void joinGroups(MouseEvent me) {
	    System.out.println("Join Groups Button Clicked");

	    // Check if the user is already in a group
	    String groupName = dbHandler.getGroupName(user.getUsername());
	    boolean isLeader = dbHandler.isGroupLeader(user.getUsername());
	    String[] groupMembers = dbHandler.getGroupMembers(user.getUsername());

	    // Case 1: User is not already in a group but not a leader
	    if (groupName.equals("No Group Selected") && !isLeader) {
	        loadPage("joinGroups");
	        return;
	    }

	    // Case 2: User is a leader and the group is not full
	    if (isLeader && groupMembers[0].equals("No Leader Found")) {
	        loadPage("joinGroups");
	        return;
	    }

	    // Default case: Disable the button if neither condition is met
	    JoinGroupButton.setDisable(true);
	    JoinGroupButton.setText("-Join Groups-");
	}

	@FXML
	public void displayResources(MouseEvent me) {
		this.loadPage("downloadResourcesScreen");
	}
	
	private void loadPage(String resource) {
		try {
			Parent root = FXMLLoader
					.load(getClass().getResource("/resources/views/studentViews/" + resource + ".fxml"));
			bp.setCenter(root);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}

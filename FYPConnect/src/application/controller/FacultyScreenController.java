package application.controller;

import application.datamodel.User;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FacultyScreenController {
	private User user;

	/*
	 * @FXML public BorderPane bp;
	 */

	/*
	 * @FXML public AnchorPane ap;
	 */

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
	public Button UpdateResourceButton;

	@FXML
	public Button GradeStudentsButton;

	@FXML
	public Button SettimelineButton;

	@FXML
	public Button MentorshipreqButton;

	// The initialize method is automatically called after the FXML file is loaded
	@FXML
	public void initialize() {
		// Make sure the user session is accessed here

		user = UserSession.getInstance().getCurrentUser();
		if (user != null) {
			System.out.println(user.getUsername());
			/*
			 * faculty_username.setText(user.getName()); // Display username on the label
			 */		} else {
			System.out.println("No user found in session");
		}
	}

	@FXML
	public void handleLogout(ActionEvent e) {
		try {
			System.out.println("close Button Clicked");
			// Clear the current user session
			UserSession.getInstance().setCurrentUser(null);

			Parent root = FXMLLoader.load(getClass().getResource("/resources/views/loginScreen.fxml"));
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@FXML
	public void Dashboard1(ActionEvent me) {
		System.out.println("Dashboard Button Clicked");
	}

	@FXML
	public void UploadProjects(ActionEvent me) {
		System.out.println("Upload Projects Button Clicked");
	}

	@FXML
	public void UploadResources(ActionEvent me) {
		System.out.println("Update Resources Button Clicked");
	}

	@FXML
	public void GradeStudents(ActionEvent me) {
		System.out.println("Grade Students Button");
	}

	@FXML
	public void SetTimeline(ActionEvent me) {
		System.out.println("Set Timeline Button");
	}

	
	/*
	 * private void loadPage(String page) { try { Parent root =
	 * FXMLLoader.load(getClass().getResource("/resources/views/" + page +
	 * ".fxml")); bp.setCenter(root); } catch (Exception ex) { ex.printStackTrace();
	 * } }
	 */
	 
}
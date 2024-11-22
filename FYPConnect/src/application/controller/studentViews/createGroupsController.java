package application.controller.studentViews;

import application.datamodel.User;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;
import application.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class createGroupsController {
	User user;
	PersistanceHandler dbHandler;
	
	
	@FXML
	public TextField groupNameText;
	
	@FXML
	public TextField student1Text;
	
	@FXML
	public TextField student2Text;
	
	@FXML
	public Button createGroupButton;
	
	@FXML
	public Text invalid_invite;
	
	@FXML
	public void initialize() {
		user = UserSession.getInstance().getCurrentUser();
		dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
	}
	
	@FXML
	public void createGroup() {
		String groupName = groupNameText.getText();
		String student1 = student1Text.getText();
		String student2 = student2Text.getText();


		
		// Check if student1 is not null, not empty, and not the same as the current user's username
		if (student1 != null && !student1.isEmpty()) {
		    if (student1.equals(user.getUsername())) { // If student1 is the same as the current user
		        System.out.println("You cannot invite yourself.");
		        invalid_invite.setText("You cannot invite yourself.");
		        return; // Exit the method
		    }

		    // Proceed to check if the student exists
		    User user1 = dbHandler.getStudent(student1);
		    if (user1 == null) {
		        System.out.println("Student roll number is invalid");
		        invalid_invite.setText("Student roll number is invalid");
		        return; // Exit the method
		    }
		}

		// Check if student2 is not null, not empty, and not the same as the current user's username
		if (student2 != null && !student2.isEmpty()) {
		    if (student2.equals(user.getUsername())) { // If student2 is the same as the current user
		        System.out.println("You cannot invite yourself.");
		        invalid_invite.setText("You cannot invite yourself.");
		        return; // Exit the method
		    }

		    // Proceed to check if the student exists
		    User user2 = dbHandler.getStudent(student2);
		    if (user2 == null) {
		        System.out.println("Student roll number is invalid");
		        invalid_invite.setText("Student roll number is invalid");
		        return; // Exit the method
		    }
		}

		if (groupName == null || groupName.isEmpty()) {
			System.out.println("Group name is empty");
			invalid_invite.setText("Group name is empty");
			return;
		}

		//Check if the group name already exists
		if (dbHandler.checkGroupExists(groupName)) {
			System.out.println("Group name already exists");
			invalid_invite.setText("Group name already exists");
			return;
		}
		
		invalid_invite.setText("");

		dbHandler.createGroup(groupName, user.getUsername());
		invalid_invite.setStyle("-fx-fill: green;");
		invalid_invite.setText("Group created successfully");
		
		createGroupButton.setDisable(true);
		
		
		if (student1 != null && !student1.isEmpty()) {
			
			dbHandler.sendInvite(groupName, student1);
		}
		if (student2 != null && !student2.isEmpty()) {
            
            dbHandler.sendInvite(groupName, student2);
		}
	}
	
}

package application.controller.studentViews;

import application.datamodel.User;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.DBHandler;
import application.persistance.PersistanceHandler;
import application.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class deleteGroup {
	User user;
	PersistanceHandler dbHandler;
	String groupName;
	
	@FXML
	public Text group_name;
	
	
	
	@FXML
	public Text invalid_invite;
	
	
	@FXML
	public Button deleteGroupButton;
	
	
	
	@FXML
	public void initialize() {
		user = UserSession.getInstance().getCurrentUser();
		dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
		groupName = dbHandler.getGroupName(user.getUsername());
		group_name.setText(groupName);
		
		//if group members are not null, write that they are not null on the screen and disable the delete group button
		if (dbHandler.getGroupMembers(groupName)[0] != "No Leader Found") {
			invalid_invite.setText("More than one member in the group. Cannot delete group.");
			deleteGroupButton.setDisable(true);
		}
		
		
		
	}
	
	
	@FXML
	public void deleteGroup1() {
		
		
		
		dbHandler.deleteGroup(groupName);
		
		invalid_invite.setText("Group Deleted");
	}
	
	
	
	
	
	
}

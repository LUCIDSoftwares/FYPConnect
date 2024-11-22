package application.controller.studentViews;

import application.datamodel.User;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;
import application.session.UserSession;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class studentHomeController {

	User user;
	PersistanceHandler dbHandler;
	String groupName;
	String projectName;
	String supervisorName;

	User supervisor;

	@FXML
	public Text roll_name;

	@FXML
	public Text group_name;

	@FXML
	public Text project_name;

	@FXML
	public Text supervisor_name;

	@FXML
	public Text group_members_title;
	
	@FXML
	public Text group_leader;
	
	@FXML
	public Text group_st1;
	
	@FXML
	public Text group_st2;
	
	String[] groupMembers = new String[3];
	
	@FXML
	public void initialize() {

		user = UserSession.getInstance().getCurrentUser();
		if (user != null) {
			roll_name.setText(user.getUsername() + " - " + user.getName());

			dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
			groupName = dbHandler.getGroupName(user.getUsername());
			projectName = dbHandler.getProjectName(groupName);
			supervisor = dbHandler.getSupervisor(groupName);
			groupMembers = dbHandler.getGroupMembers(groupName);
			
			group_name.setText(groupName);
			project_name.setText(projectName);
			if (supervisor != null)
				supervisor_name.setText(supervisor.getName());
			if(!(groupMembers[0] == "No Leader Found" ||
            groupMembers[1] == "No Student 1 Found" ||
            groupMembers[2] == "No Student 2 Found")) {
				group_leader.setText(groupMembers[0]);
				group_st1.setText(groupMembers[1]);
				group_st2.setText(groupMembers[2]);
				
				if (group_leader.getText().equals(user.getName())) {
					group_leader.setStyle("-fx-fill: red;");
				}
				else if (group_st1.getText().equals(user.getName())) {
					group_st1.setStyle("-fx-fill: red;");
				} 
				else if (group_st2.getText().equals(user.getName())) {
					group_st2.setStyle("-fx-fill: red;");
				}
			}
			else {
				group_members_title.setText("No Group Members Found");
			}
			
		} else {
			System.out.println("No user found in session");
		}

	}
	
	
}

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
	public void initialize() {

		user = UserSession.getInstance().getCurrentUser();
		if (user != null) {
			roll_name.setText(user.getUsername() + " - " + user.getName());

			dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
			groupName = dbHandler.getGroupName(user.getUsername());
			projectName = dbHandler.getProjectName(groupName);
			supervisor = dbHandler.getSupervisor(groupName);
			group_name.setText(groupName);
			project_name.setText(projectName);
			if (supervisor != null)
				supervisor_name.setText(supervisor.getName());
		} else {
			System.out.println("No user found in session");
		}

	}
}

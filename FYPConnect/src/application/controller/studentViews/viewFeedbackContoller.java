package application.controller.studentViews;

import java.util.ArrayList;

import application.datamodel.Deliverable;
import application.datamodel.User;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;
import application.services.DeliverableHandler;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class viewFeedbackContoller {

	User user;
	PersistanceHandler dbHandler;
	DeliverableHandler resourceHandler = new DeliverableHandler();
	
    @FXML
    private TextArea commentText;

    @FXML
    private TextArea del_description;

    @FXML
    private TextField del_uploader;

    @FXML
    private ComboBox<String> deliverableCombo;

    @FXML
    private TextField gradeText;

    @FXML
    private Text noFeedbackText;

    @FXML
    private TextField supervisortext;
    
    private String group_name;
	ArrayList<Deliverable> resourceArrayList;
	
	@FXML
	public void initialize() {
	    // Initialize the resourceArrayList if it is null
	    if (resourceArrayList == null) {
	        resourceArrayList = new ArrayList<>();
	    }

	    // Initialize the resourceHandler if it is null
	    if (resourceHandler == null) {
	        resourceHandler = new DeliverableHandler();
	    }

	    // Initialize the dbHandler
	    dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");

	    // Fetch the current user from the session
	    user = UserSession.getInstance().getCurrentUser();

	    // Check if the user is not null
	    if (user == null) {
	        System.err.println("User session is not initialized. Cannot proceed.");
	        return; // Exit the method to avoid further null pointer issues
	    }

	    // Get the group name associated with the user
	    group_name = dbHandler.getGroupName(user.getUsername());

	    if (group_name == null) {
	        System.err.println("Group name could not be retrieved for user: " + user.getUsername());
	        return;
	    }

	    // Fetch all submitted deliverables for the group
	    resourceArrayList = resourceHandler.getAllSubmittedDeliverables(group_name);

	    if (resourceArrayList == null || resourceArrayList.isEmpty()) {
	        System.out.println("No deliverables found for group: " + group_name);
	        deliverableCombo.getItems().clear(); // Ensure combo box is empty
	        return;
	    }

	    // Populate the combo box with deliverables
	    deliverableCombo.getItems().clear();
	    for (Deliverable resource : resourceArrayList) {
	        deliverableCombo.getItems().add(resource.getDeliverableId() + ": " + resource.getDescription());
	    }
	}

	public viewFeedbackContoller() {
	    // Ensure handlers and lists are initialized
	    resourceHandler = new DeliverableHandler();
	    resourceArrayList = new ArrayList<>();
	}



	@FXML
	void handleDelSelect(ActionEvent event) {
		String selectedDeliverable = deliverableCombo.getValue();
        String[] parts = selectedDeliverable.split(":");
        int del_id = Integer.parseInt(parts[0]);
        Deliverable del = resourceArrayList.stream().filter(d -> d.getDeliverableId() == del_id).findFirst().orElse(null);
        del_description.setText(del.getDescription());
        del_uploader.setText(del.getFacultyId());
        commentText.setText(dbHandler.getFeedback(del_id, group_name));
		if (dbHandler.getFeedback(del_id, group_name) == null) {
			noFeedbackText.setText("No feedback available for this deliverable.");
			return;
		}
        gradeText.setText(dbHandler.getGrade(del_id, group_name));
        supervisortext.setText(dbHandler.getGrader(del_id, group_name));
    }
	


}

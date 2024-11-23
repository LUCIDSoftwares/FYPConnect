package application.controller.studentViews;

import application.datamodel.User;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;
import application.session.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

public class joinGroupsController {
	
	@FXML
	public ComboBox<String> invitatonsCombo;
	
	@FXML
	public ComboBox<String> groups_Combo;
	
	@FXML
	public Text invite_text_1;
	
	@FXML
	public Text invite_text_2;
	
	@FXML
	public Text request_text_1;
	
	@FXML
	public Text request_text_2;
	
	@FXML
	public Button acceptInviteButton;
	
	@FXML
	public Button declineInviteButton;
	
	@FXML
	public Button sendRequestButton;
	
	PersistanceHandler dbHandler;
	User user;
	
	boolean isLeader;
	
	@FXML
	public void initialize() {
		// get all the invitations and requests for the user and display them in the
		// combo boxes
		dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
		// check it the user is not a group leader
		//if the user is a leader, he will get requests from other people to join group in this combo box
		//if the user is not a leader, he will get invitations to join group in this combo box
		user = UserSession.getInstance().getCurrentUser();
		
		//checking if the user is a leader
		isLeader = dbHandler.isGroupLeader(user.getUsername());
		if (isLeader) {
			// get all the requests to join the group
			String[] requests = dbHandler.getGroupRequests(user.getUsername());
			if (requests[0] == null) {
				request_text_1.setText("No Requests to join your group");
			} else {
				for (String request : requests) {
					if(request != null)
					invitatonsCombo.getItems().add(request);
				}
			}
		} else {
			// get all the invitations for the user
			String[] invitations = dbHandler.getGroupInvitations(user.getUsername());
			if (invitations[0] == null) {
				invite_text_1.setText("No Invitations to join a group");
			} else {
				for (String invitation : invitations) {
					if(invitation != null)
					invitatonsCombo.getItems().add(invitation);
				}
			}
		}
		
		//send group request section
		
		//get all the groups where a user can be a member, and the user is already a member of any group, do not show anything
		String[] groups = dbHandler.getAvailableGroups();
		if (groups[0] == null) {
			request_text_2.setText("No groups available to join");
		} else {
			for (String group : groups) {
				if(group != null)
				groups_Combo.getItems().add(group);
			}
		}
		
		
	}
	
	//when a user select a value in the combobox, display its details in the text
	@FXML
	public void handleInvitationComboBoxSelection(ActionEvent event) {
		//if the user is a leader, display the request details
		if(isLeader) {
			User request = dbHandler.getStudent(invitatonsCombo.getValue());
			invite_text_1.setText("Student Name: " + request.getName());
			invite_text_2.setText("Student roll number: " + request.getUsername());
		}
		else {
			// if the user is not a leader, display the invitation details
			String groupName = invitatonsCombo.getValue();
			invite_text_1.setText("Group Name: " + groupName);
			String groupLeader = dbHandler.getGroupLeader(groupName);
			invite_text_2.setText("Leader Roll number: " + groupLeader);
		}
	
	}
		
	
	
	
	@FXML
	public void acceptInvite() {
		if (invitatonsCombo.getValue() == null || invitatonsCombo.getValue().isEmpty()) {
		    invite_text_1.setText("Error");
		    invite_text_2.setText("No group selected. Please select a group.");
		    return;
		}
		
		// if the user is a leader, accept the request
		if (isLeader) {
			String selectedperson = invitatonsCombo.getValue(); // Fetch selected group from ComboBox
			if (selectedperson != null && !selectedperson.isEmpty()) {
			    boolean isAccepted = dbHandler.acceptRequest(dbHandler.getGroupName(user.getUsername()), selectedperson); // Use the corrected method

			    if (isAccepted) {
			        invite_text_1.setText("Request Accepted");
			        invite_text_2.setText( selectedperson + " is now a member of your group.");
			    } else {
			        invite_text_1.setText("Request Not Accepted");
			        invite_text_2.setText("Unable to join the group. Please try again or contact support.");
			    }
			} else {
			    invite_text_1.setText("No Group Selected");
			    invite_text_2.setText("Please select a group from the dropdown.");
			}

		} else {
			// if the user is not a leader, accept the invitation
			boolean result = dbHandler.acceptInvite(invitatonsCombo.getValue(), user.getUsername());
			if (result) {
			    invite_text_1.setText("Invitation Accepted");
			    invite_text_2.setText("You are now a member of the group");
			} else {
			    invite_text_1.setText("Error");
			    invite_text_2.setText("Unable to join the group. It may be full.");
			}
		}
	}
	
	@FXML
	public void declineInvite() {
		if (invitatonsCombo.getValue() == null || invitatonsCombo.getValue().isEmpty()) {
			invite_text_1.setText("Error");
			invite_text_2.setText("No group selected. Please select a group.");
			return;
		}

		// if the user is a leader, decline the request
		if (isLeader) {
			boolean isDeclined = dbHandler.declineRequest(dbHandler.getGroupName(user.getUsername()),
					invitatonsCombo.getValue());
			if (isDeclined) {
				invite_text_1.setText("Request Declined");
				invite_text_2.setText("");
			} else {
				invite_text_1.setText("Error");
				invite_text_2.setText("Unable to decline the request. Please try again.");
			}
		} else {
			// if the user is not a leader, decline the invitation
			dbHandler.declineInvite(invitatonsCombo.getValue(), user.getUsername());
			invite_text_1.setText("Invitation Declined");
			invite_text_2.setText("");
		}
	}
	
	@FXML
	public void sendRequest() {
		if (groups_Combo.getValue() == null || groups_Combo.getValue().isEmpty()) {
			request_text_1.setText("Error");
			request_text_2.setText("No group selected. Please select a group.");
			return;
		}
		// send a request to join the group
		boolean result = dbHandler.sendRequest(groups_Combo.getValue(), user.getUsername());
		if (result) {
			request_text_1.setText("Request Sent");
			request_text_2.setText("Waiting for the group leader to accept your request");
		} else {
			request_text_1.setText("Error");
			request_text_2.setText("Unable to send request. You may already be a member of the group.");
		}
	}
	
}

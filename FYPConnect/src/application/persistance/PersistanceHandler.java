package application.persistance;

import java.util.Date;
import java.util.ArrayList;

<<<<<<< Updated upstream
import application.datamodel.Deliverable;
=======
import application.datamodel.Project;
>>>>>>> Stashed changes
import application.datamodel.Resource;
import application.datamodel.User;

public abstract class PersistanceHandler {

	public abstract void createUser(User user);
	
	public abstract User retrieveUser(String username, String password);
	
	public abstract User retrieveUser(String username);
	
	public abstract int getNumOfUsers();
	
	public abstract int getNumOfGroups();
	
	public abstract int getNumOfProjects();
	
	public abstract String getGroupName(String Username);
	
	public abstract String getProjectName(String GroupName);
	
	public abstract User getSupervisor(String GroupName);
	
	public abstract String[] getGroupMembers(String GroupName);
	
	public abstract void createGroup(String GroupName, String Username);
	
	public abstract void sendInvite(String GroupName, String Username);
	
	public abstract User getStudent(String Username);
	
	public abstract boolean checkGroupExists(String GroupName);
	
	public abstract void deleteGroup(String GroupName);
	
	public abstract boolean isGroupLeader(String Username);
	
	public abstract String[] getGroupRequests(String Username);
	
	public abstract String[] getGroupInvitations(String Username);
	
	public abstract String[] getAvailableGroups();
	
	public abstract String getGroupLeader(String GroupName);
	
//<<<<<<< Updated upstream
	public abstract boolean acceptInvite(String GroupName, String Username);
	
	public abstract boolean declineInvite(String GroupName, String Username);
	
	public abstract boolean sendRequest(String GroupName, String Username);
	
	public abstract boolean acceptRequest(String GroupName, String Username);
	
	public abstract boolean declineRequest(String GroupName, String Username);
//=======
	public abstract ArrayList<Resource> getAllResources();
	
	public abstract ArrayList<Resource> getResourcesByTitle(String title);
	
<<<<<<< Updated upstream
	public abstract ArrayList<Deliverable> getAllDeliverables();
	
	public abstract ArrayList<Deliverable> getAllSubmittedDeliverables(String group_name);
	
	public abstract boolean saveSubmission(int deliverableId, String groupId, Date submissionTime, String filePath);
	
	public abstract String getFeedback(int deliverableId, String group_name);
	
	public abstract String getGrade(int deliverableId, String group_name);
	
	public abstract String getGrader(int deliverableId, String group_name);
=======
	public abstract ArrayList<Project> getAllAvailableProjects();
	
	public abstract ArrayList<Project> getAllAvailableProjects(String projectTitle, int groupId);
	
	public abstract ArrayList<Project> getAllProjectsWithMentorshipRequest(String projectTitle, int groupId);
	
	public abstract ArrayList<Project> getAllProjectsWithDeclinedRequests(String projectTitle, int groupId);
	
	public abstract int getGroupId(int userId);
	
	public abstract void createMentorshipRequest(int projectId, int groupId);
>>>>>>> Stashed changes
	
//>>>>>>> Stashed changes
}

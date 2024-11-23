package application.persistance;

import application.datamodel.User;

public abstract class PersistanceHandler {

	public abstract void createUser(User user);
	
	public abstract User retrieveUser(String username, String password);
	
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
	
	public abstract boolean acceptInvite(String GroupName, String Username);
	
	public abstract boolean declineInvite(String GroupName, String Username);
}

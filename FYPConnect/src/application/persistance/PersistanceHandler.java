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
}

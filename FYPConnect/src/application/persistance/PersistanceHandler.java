package application.persistance;

import application.datamodel.User;

public abstract class PersistanceHandler {

	public abstract int createUser(User user);
	
	public abstract User retrieveUser(String username, String password);
	
	public abstract int getNumOfUsers();
	
	public abstract int getNumOfGroups();
	
	public abstract int getNumOfProjects();
	
}

package application.persistance;

import java.util.ArrayList;

import application.datamodel.User;

public abstract class PersistanceHandler {

	public abstract int createUser(User user);
	
	public abstract User retrieveUser(String username, String password);
	
	public abstract User retrieveUser(String username);
	
	public abstract int getNumOfUsers();
	
	public abstract int getNumOfGroups();
	
	public abstract int getNumOfProjects();
	
	public abstract ArrayList<User> getUserArrayListByType(String userType);
	
}

package application.persistance;

import application.datamodel.User;

public abstract class PersistanceHandler {

	public abstract void createUser(User user);
	
	public abstract User retrieveUser(String username, String password);
	
	public abstract int getNumOfUsers();
	
}

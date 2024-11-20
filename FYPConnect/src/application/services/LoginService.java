package application.services;

import application.datamodel.User;
import application.persistance.DBHandler;

public class LoginService {
	DBHandler dbHandler;
	
	
	public User login(String username, String password) {
		
		dbHandler = new DBHandler();
		User user = this.dbHandler.retrieveUser(username, password);
		
		return user;
	}
	
	
}

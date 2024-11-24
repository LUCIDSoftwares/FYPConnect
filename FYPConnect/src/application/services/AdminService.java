package application.services;

import java.util.ArrayList;

import application.datamodel.User;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;

public class AdminService {
	PersistanceHandler dbHandler;
	
	public AdminService() {
		this.dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
	}
	
	public int createUser(String userType, String name, String username, String password, String email, double cgpa) {
		
		User user = ConcreteUserFactory.getInstance().createUser(userType, name, username, password, email, cgpa);
		int userId = dbHandler.createUser(user);
		return userId;
		
	};
	
	public ArrayList<User> getUserArrayList(String userType) {
		
		ArrayList<User> userArrayList = null;
		userArrayList = this.dbHandler.getUserArrayListByType(userType);
		
		return userArrayList;
	}
	
	public User getUserByUsername(String username) {
		User user = this.dbHandler.retrieveUser(username);
		return user;
	}
	
	public int updateUser(String username, String name, String email, String password, double cgpa) {
		
		return this.dbHandler.updateUser(username, name, email, password, cgpa);
	}
	
	public int deleteUserbyId(int userId) {
		
		return this.dbHandler.deleteUserById(userId);
	}
	
	public int deleteUserByUsername(String username) {
		
		return this.dbHandler.deleteUserByUsername(username);
	}
	
}

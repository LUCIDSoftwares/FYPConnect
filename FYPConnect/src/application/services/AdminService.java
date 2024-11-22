package application.services;

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
	
	
}

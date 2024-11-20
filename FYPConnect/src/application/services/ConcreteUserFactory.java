package application.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import application.datamodel.Admin;
import application.datamodel.Faculty;
import application.datamodel.Student;
import application.datamodel.User;

public class ConcreteUserFactory extends UserFactory {

	@Override
	public User createUser(ResultSet result) {
		User user = null;
		try {
			if(result.getString(4).equalsIgnoreCase("Student") == true) {
				user = new Student(result.getInt(1), result.getString(2), result.getString(3), result.getString(6), result.getString(7), result.getDouble(5));
			}
			else if(result.getString(4).equalsIgnoreCase("Faculty") == true){
				user = new Faculty(result.getInt(1), result.getString(2), result.getString(3), result.getString(6), result.getString(7));
			}
			else {
				user = new Admin(result.getInt(1), result.getString(2), result.getString(3), result.getString(6), result.getString(7));
			}
		} catch (SQLException e) {
			System.out.println("Exception thrown in the createUser(ResultSet) method of the UserFactory class");
			e.printStackTrace();
		}
		return user;
	}
	
	@Override
	public User createUser(String userType) {
		User user = null;
		if(userType.equalsIgnoreCase("Student") == true) {
			user = new Student();
		}
		else if(userType.equalsIgnoreCase("Faculty") == true) {
			user = new Faculty();
		}
		else {
			user = new Admin(); 
		}
		return user;
	}

}

package application.services;

import java.sql.ResultSet;
import java.sql.SQLException;

import application.datamodel.Admin;
import application.datamodel.Faculty;
import application.datamodel.Student;
import application.datamodel.User;

public class ConcreteUserFactory extends UserFactory {

	// used for implementing the singleton pattern
	private static ConcreteUserFactory instance;
	
	private ConcreteUserFactory() {
		// used for implementing the singleton pattern
	}
	
	public static synchronized ConcreteUserFactory getInstance() {
		if(instance == null) {
			instance = new ConcreteUserFactory();
		}
		return instance;
	}
	
	@Override
	public User createUser(ResultSet result) {
	    User user = null;
	    try {
	        // Get the user type from the ResultSet
	        String userType = result.getString("usertype"); // Column name 'usertype' used here for better readability

	        if (userType == null) {
	            throw new SQLException("User type is null in the ResultSet.");
	        }

	        // Handle different user types
	        if (userType.equalsIgnoreCase("Student")) {
	            // Extract data for Student
	            int id = result.getInt("ID");
	            String name = result.getString("name");
	            String password = result.getString("password");
	            String username = result.getString("username");
	            String email = result.getString("email");
	            double cgpa = result.getDouble("cgpa");

	            // Create Student object
	            user = new Student(id, name, password, username, email, cgpa);
	        } else if (userType.equalsIgnoreCase("Faculty")) {
	            // Extract data for Faculty
	            int id = result.getInt("ID");
	            String name = result.getString("name");
	            String password = result.getString("password");
	            String username = result.getString("username");
	            String email = result.getString("email");

	            // Create Faculty object
	            user = new Faculty(id, name, password, username, email);
	        } else if (userType.equalsIgnoreCase("Admin")) {
	            // Extract data for Admin
	            int id = result.getInt("ID");
	            String name = result.getString("name");
	            String password = result.getString("password");
	            String username = result.getString("username");
	            String email = result.getString("email");

	            // Create Admin object
	            user = new Admin(id, name, password, username, email);
	        } else {
	            throw new SQLException("Unrecognized user type: " + userType);
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

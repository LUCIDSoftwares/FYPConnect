package application.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	
	@Override
	public User createUser(String userType, String name, String username, String password, String email, double cgpa) {
		User user = null;
		if(userType.equalsIgnoreCase("Student") == true) {
			user = new Student(name, username, password, email, cgpa);
		}
		else if(userType.equalsIgnoreCase("Faculty") == true) {
			user = new Faculty(name, username, password, email);
		}
		else {
			user = new Admin(name, username, password, email); 
		}
		return user;
	}

	
	@Override
	public ArrayList<User> createUserArrayList(String userType, ResultSet result) {
		ArrayList<User> userArrayList = null;
		
		// used for identifying user type
		// done so as to avoid using string comparison function repeatedly. Should in my opinion be better for performance
		int type;
		if(userType.equalsIgnoreCase("Student"))
			type = 0;
		else if(userType.equalsIgnoreCase("Faculty"))
			type = 1;
		else
			type = 2;
		
		try {
			// read the first row
			if(result.next()) {
				userArrayList = new ArrayList<>();
				User user = null;
	            int id = result.getInt("ID");
	            String name = result.getString("name");
	            String password = result.getString("password");
	            String username = result.getString("username");
	            String email = result.getString("email");
	            
	            if(type == 0) {
		            double cgpa = result.getDouble("cgpa");
		            user = new Student(id, name, password, username, email, cgpa);
	            }
	            else if(type == 1)
		            user = new Faculty(id, name, password, username, email);
	            else
		            user = new Faculty(id, name, password, username, email);
	            
	            userArrayList.add(user);
				
	            // now read the remaining list of rows
				while(result.next()) {
					user = null;
					id = result.getInt("ID");
		            name = result.getString("name");
		            password = result.getString("password");
		            username = result.getString("username");
		            email = result.getString("email");
		            
		            if(type == 0) {
			            double cgpa = result.getDouble("cgpa");
			            user = new Student(id, name, password, username, email, cgpa);
		            }
		            else if(type == 1)
			            user = new Faculty(id, name, password, username, email);
		            else
			            user = new Faculty(id, name, password, username, email);
		            
		            userArrayList.add(user);
				}
				
			}
		} catch (SQLException e) {
			System.out.println("Exception thrown in the createUserList(String, ResultSet) method of the ConcreteUserFactory Class");
			e.printStackTrace();
		}
		
		return userArrayList;
	}
	
}

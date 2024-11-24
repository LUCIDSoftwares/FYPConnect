package application.persistance;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import application.datamodel.Admin;
import application.datamodel.Faculty;
import application.datamodel.Student;
import application.datamodel.User;
import application.services.*;

// setup mysql with respect to eclipse then uncomment the following code
public class DBHandler extends PersistanceHandler{
	private Connection connection;
	private boolean connectionFlag;
	private String dbName = System.getenv("DB_name");
	private String dbUsername = System.getenv("DB_USERNAME");
	private String dbPassword = System.getenv("DB_PASSWORD");
	
// 	if Database doesn't connect try uncommenting the line below
// 	Class.forName("com.mysql.cj.jdbc.Driver");	
	
	public DBHandler() {
		this.connection = null;
		this.connectionFlag = false;
	}
	
	public boolean establishConnection() {
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, dbUsername, dbPassword);
			this.connectionFlag = true;
			//Statement stmt = con.createStatement();
			//System.out.println("Inserting records");
			//String sql = "INSERT INTO checker values (47)";
			//stmt.executeUpdate(sql);	
			//con.close();
		
		} catch (SQLException e) {
			System.out.println("Exception thrown in the establishConnection method of the DBHandler Class");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	private boolean closeConnection() {
		try {
			this.connection.close();
			this.connection = null;
			this.connectionFlag = false;
		} catch (SQLException e) {
			System.out.println("Exception thrown in the closeConnection method of the DBHandler Class");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	@Override
	public int createUser(User user) {
		if(this.establishConnection() == false)
			return -1;
		
		String sqlQuery1 = "INSERT INTO User (name, password, usertype, cgpa, username, email) VALUES (?, ?, ?, ?, ?, ?);";
		int userId = -1;
		
		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, user.getName());
			preparedStatement1.setString(2, user.getPassword());
			
			if(user instanceof Admin) {
				preparedStatement1.setString(3, "Admin");
				preparedStatement1.setDouble(4, 0.0);
			}
			else if(user instanceof Faculty) {
				preparedStatement1.setString(3, "Faculty");
				preparedStatement1.setDouble(4, 0.0);
			}
			else if(user instanceof Student) {
				preparedStatement1.setString(3, "Student");
				preparedStatement1.setDouble(4, ((Student) user).getCgpa());
			}
			
			preparedStatement1.setString(5, user.getUsername());
			preparedStatement1.setString(6, user.getEmail());
			
			if(preparedStatement1.executeUpdate() <= 0) {
				System.out.println("Can not create a user");
			}
			else {
				// get the ID of the user just created and inserted into the database
				String sqlQuery2 = "SELECT MAX(ID)\r\n"
						+ "FROM User;";
				preparedStatement1 = this.connection.prepareStatement(sqlQuery2);
				ResultSet result1 = preparedStatement1.executeQuery();
				if(result1.next()) {
					userId = result1.getInt(1);
				}
			}
				
			
		} catch (SQLException e) {
			// integrity constraint violation exception is already handled by code so no need to throw exception
		    if(e instanceof SQLIntegrityConstraintViolationException == false) {
				System.out.println("Exception thrown in the createUser(User) method of the DBHandler Class");
				e.printStackTrace();
		    }

		}
		
		this.closeConnection();
		return userId;
	}
	
	@Override
	public User retrieveUser(String username, String password) {
		this.establishConnection();
		User user = null;
		
		String sqlQuery1 = "SELECT * \r\n"
				+ "FROM User \r\n"
				+ "WHERE username = ? \r\n"
				+ "AND password = ?;";
		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, username);
			preparedStatement1.setString(2, password);
			ResultSet result1 = preparedStatement1.executeQuery();
			if(result1.next()) {
				UserFactory concreteUserFactory = ConcreteUserFactory.getInstance();
				user = concreteUserFactory.createUser(result1);
			}
		
		} catch (SQLException e) {
			System.out.println("Exception thrown in the retrieveUser(String, String) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return user;
	}
	
	@Override
	public User retrieveUser(String username) {
		this.establishConnection();
		User user = null;
		
		String sqlQuery1 = "SELECT * \r\n"
				+ "FROM User \r\n"
				+ "WHERE username = ?;";
		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, username);
			ResultSet result1 = preparedStatement1.executeQuery();
			if(result1.next()) {
				UserFactory concreteUserFactory = ConcreteUserFactory.getInstance();
				user = concreteUserFactory.createUser(result1);
			}
		
		} catch (SQLException e) {
			System.out.println("Exception thrown in the retrieveUser(String) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return user;		
	}
	
	@Override
	public int getNumOfUsers() {
		this.establishConnection();
		int numOfUsers = 0;
		
		String sqlQuery1 = "SELECT COUNT(*)\r\n"
				+ "FROM User;";
		
		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			ResultSet result1 = preparedStatement1.executeQuery();
			if(result1.next())
				numOfUsers = result1.getInt(1);
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the getNumOfUsers() method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return numOfUsers;
	}
	
	@Override
	public int getNumOfGroups() {
		this.establishConnection();
		int numOfGroups = 0;
		
		String sqlQuery1 = "SELECT COUNT(*)\r\n"
				+ "FROM groupT;";
		
		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			ResultSet result1 = preparedStatement1.executeQuery();
			if(result1.next())
				numOfGroups = result1.getInt(1);
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the getNumOfGroups() method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return numOfGroups;
	}

	@Override
	public int getNumOfProjects() {
		this.establishConnection();
		int numOfProjects = 0;
		
		String sqlQuery1 = "SELECT COUNT(*)\r\n"
				+ "FROM project;";
		
		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			ResultSet result1 = preparedStatement1.executeQuery();
			if(result1.next())
				numOfProjects = result1.getInt(1);
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the getNumOfProjects() method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return numOfProjects;
	}

	
	@Override
	public ArrayList<User> getUserArrayListByType(String userType) {
		if(this.establishConnection() == false)
			return null;
		
		
		ArrayList<User> userArrayList = null;
//		boolean studentFlag = false;
//		if(userType.equalsIgnoreCase("Student") == true) {
//			studentFlag = true;
//		}
		String sqlQuery1 = "SELECT ID, name, password, cgpa, username, email\r\n"
				+ "FROM User\r\n"
				+ "WHERE usertype = ?;";
		try {
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setString(1, userType);
			
			ResultSet result1 = statement1.executeQuery();
			UserFactory concreteUserFactory = ConcreteUserFactory.getInstance();
			userArrayList = concreteUserFactory.createUserArrayList(userType, result1);
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the getUserArrayListByType(String) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return userArrayList;
	}
	
	
	
	
	
	public int updateUser(String username, String name, String email, String password, double cgpa) {
		if(this.establishConnection() == false)
			return -3;
		
		int flag = -2;
		
		try {
			// first check if that username exists or not and get its user type
			String sqlQuery1 = "SELECT COUNT(1), usertype\r\n"
					+ "FROM User\r\n"
					+ "WHERE username = ?;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setString(1, username);
			
			ResultSet result1 = statement1.executeQuery();
			boolean userExists = false;
			String userType = "Admin";
				
			if(result1.next()) {
				if(result1.getInt(1) >= 1) {
					userExists = true;
					flag = -1;
					userType = result1.getString(2);
				}
			}
			
			// now we can update it
			if(userExists && userType.equalsIgnoreCase("Faculty") == true && cgpa >= 0.0)
				flag = -10;
			else if(userExists && userType.equalsIgnoreCase("Admin") == false) {
				String sqlQuery2 = "UPDATE User";
				
				if(name == null)
					sqlQuery2 = sqlQuery2 + " SET name = User.name";
				else
					sqlQuery2 = sqlQuery2 + " SET name = ?";
				if(password != null)
					sqlQuery2 = sqlQuery2 + ", password = ?";
				if(email != null)
					sqlQuery2 = sqlQuery2 + ", email = ?";
				if(cgpa >= 0.0  && userType.equalsIgnoreCase("Student"))
					sqlQuery2 = sqlQuery2 + ", cgpa = ?";
				sqlQuery2 = sqlQuery2 + " WHERE username = ?;";
				
				int counter = 1;
				
				PreparedStatement statement2 = this.connection.prepareStatement(sqlQuery2);
				if(name != null) {
					statement2.setString(counter, name);
					++counter;
				}
				if(password != null) {
					statement2.setString(counter, password);
					++counter;
				}
				if(email != null) {
					statement2.setString(counter, email);
					++counter;
				}
				if(cgpa >= 0.0  && userType.equalsIgnoreCase("Student")) {
					statement2.setDouble(counter, cgpa);
					++counter;
				}
				
				statement2.setString(counter, username);
				
				if(statement2.executeUpdate() <= 0)
					flag = 0;
				else
					flag = 1;
			}
		
		} catch (SQLException e) {
			if(e instanceof SQLIntegrityConstraintViolationException == false) {
				System.out.println("Exception thrown in the updateUser(String, String, String, String, double)"
						+ " method of the DBHandler class");
				e.printStackTrace();
			}
			else
				flag = -5;
		}
		
		this.closeConnection();
		return flag;
	}
	
}

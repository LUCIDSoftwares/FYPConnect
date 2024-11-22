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
	
}

package application.persistance;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	public void createUser(User user) {
		this.establishConnection();
		
		
		this.closeConnection();
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
	
	
}

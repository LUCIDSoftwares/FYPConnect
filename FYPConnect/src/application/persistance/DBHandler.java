package application.persistance;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.datamodel.User;
import application.services.*;

// setup mysql with respect to eclipse then uncomment the following code
public class DBHandler {
	private Connection connection;
	private boolean connectionFlag;

// 	if Database doesn't connect try uncommenting the line below
// 	Class.forName("com.mysql.cj.jdbc.Driver");	
	
	public DBHandler() {
		this.connection = null;
		this.connectionFlag = false;
	}
	
	private boolean establishConnection() {
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/<enter you db name>", "<enter the localhost name>", "<enter your password>");
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
	
	public void createUser(User user) {
		this.establishConnection();
		
		
		this.closeConnection();
	}
	
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
				UserFactory concreteFactory = new ConcreteUserFactory();
				user = concreteFactory.createUser(result1);
			}
		
		} catch (SQLException e) {
			System.out.println("Exception thrown in the retrieveUser(String, String) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return user;
	}
	
	
	
}

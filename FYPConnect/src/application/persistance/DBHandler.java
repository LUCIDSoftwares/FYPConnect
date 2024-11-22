package application.persistance;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.datamodel.User;
import application.services.*;

// setup mysql with respect to eclipse then uncomment the following code
public class DBHandler extends PersistanceHandler {
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
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, dbUsername,
					dbPassword);
			this.connectionFlag = true;
			// Statement stmt = con.createStatement();
			// System.out.println("Inserting records");
			// String sql = "INSERT INTO checker values (47)";
			// stmt.executeUpdate(sql);
			// con.close();

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

		String sqlQuery1 = "SELECT * \r\n" + "FROM User \r\n" + "WHERE username = ? \r\n" + "AND password = ?;";
		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, username);
			preparedStatement1.setString(2, password);
			ResultSet result1 = preparedStatement1.executeQuery();
			if (result1.next()) {
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

		String sqlQuery1 = "SELECT COUNT(*)\r\n" + "FROM User;";

		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			ResultSet result1 = preparedStatement1.executeQuery();
			if (result1.next())
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

		String sqlQuery1 = "SELECT COUNT(*)\r\n" + "FROM groupT;";

		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			ResultSet result1 = preparedStatement1.executeQuery();
			if (result1.next())
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

		String sqlQuery1 = "SELECT COUNT(*)\r\n" + "FROM project;";

		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			ResultSet result1 = preparedStatement1.executeQuery();
			if (result1.next())
				numOfProjects = result1.getInt(1);

		} catch (SQLException e) {
			System.out.println("Exception thrown in the getNumOfProjects() method of the DBHandler Class");
			e.printStackTrace();
		}

		this.closeConnection();
		return numOfProjects;
	}

	@Override
	public String getGroupName(String Username) {
		this.establishConnection();
		String groupName = null;

		String sqlQuery1 = """
				    SELECT groupT.name
				    FROM groupT
				    JOIN User AS leaderUser ON groupT.leader = leaderUser.ID
				    JOIN User AS student1User ON groupT.student1 = student1User.ID
				    JOIN User AS student2User ON groupT.student2 = student2User.ID
				    WHERE leaderUser.username = ? OR student1User.username = ? OR student2User.username = ?;
				""";

		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, Username);
			preparedStatement1.setString(2, Username);
			preparedStatement1.setString(3, Username);
			ResultSet result1 = preparedStatement1.executeQuery();

			groupName = "No Group Selected";

			if (result1.next()) {
				// If there's a result, get the group name
				groupName = result1.getString("name");
			}

			System.out.println("Group Name: " + groupName);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.closeConnection();
		return groupName;
	}

	@Override
	public String getProjectName(String GroupName) {
        this.establishConnection();
        String projectName = null;

        String sqlQuery1 = """
                SELECT project.title
                FROM project
                JOIN groupT ON project.ID = groupT.projectID
                WHERE groupT.name = ?;
            """;

        try {
            PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
            preparedStatement1.setString(1, GroupName);
            ResultSet result1 = preparedStatement1.executeQuery();
            
            projectName = "No Project Selected";

            if (result1.next()) {
                // If there's a result, get the project name
                projectName = result1.getString("title");
            }

            System.out.println("Project Name: " + projectName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.closeConnection();
        return projectName;
	}
	
	@Override
	public User getSupervisor(String GroupName) {
		this.establishConnection();
        User supervisor = null;

        String sqlQuery1 = """
                SELECT u.ID, u.name, u.password, u.username, u.email
                FROM groupT g
                JOIN project p ON g.projectID = p.ID
                JOIN User u ON p.faculty_ID = u.ID
                WHERE g.name = ?;
            """;

        try {
            // Prepare the SQL statement (assuming sqlQuery1 retrieves supervisor details)
            PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);

            
                preparedStatement1.setString(1, GroupName);
            

            // Execute the query
            ResultSet result1 = preparedStatement1.executeQuery();

            if (result1.next()) {
                // If there's a result, create the supervisor using the factory
                UserFactory userFactory = ConcreteUserFactory.getInstance();
                supervisor = userFactory.createUser(result1);
            } else {
                // No supervisor found, handle appropriately
                System.out.println("Supervisor Name: No Supervisor Selected");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        this.closeConnection();
        return supervisor;
	}
	
}

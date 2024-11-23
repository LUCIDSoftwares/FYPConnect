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
				    LEFT JOIN User AS student1User ON groupT.student1 = student1User.ID
				    LEFT JOIN User AS student2User ON groupT.student2 = student2User.ID
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

	@Override
	public String[] getGroupMembers(String groupName) {
		this.establishConnection();
		String[] groupMembers = new String[3];

		String sqlQuery1 = """
				    SELECT leaderUser.name AS leaderName, student1User.name AS student1Name, student2User.name AS student2Name
				    FROM groupT
				    JOIN User AS leaderUser ON groupT.leader = leaderUser.ID
				    JOIN User AS student1User ON groupT.student1 = student1User.ID
				    JOIN User AS student2User ON groupT.student2 = student2User.ID
				    WHERE groupT.name = ?;
				""";

		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, groupName);
			ResultSet result1 = preparedStatement1.executeQuery();

			if (result1.next()) {
				groupMembers[0] = result1.getString("leaderName");
				groupMembers[1] = result1.getString("student1Name");
				groupMembers[2] = result1.getString("student2Name");
			} else {
				groupMembers[0] = "No Leader Found";
				groupMembers[1] = "No Student 1 Found";
				groupMembers[2] = "No Student 2 Found";
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.closeConnection();
		return groupMembers;
	}

	@Override
	public void createGroup(String groupName, String username) {
		this.establishConnection();

		// Step 1: Query to get the User ID of the leader
		String getUserIdQuery = "SELECT ID FROM User WHERE username = ?";

		// Step 2: Insert the group into the groupT table
		String insertGroupQuery = "INSERT INTO groupT (name, leader) VALUES (?, ?)";

		try {
			// Step 1: Fetch the User ID of the leader
			PreparedStatement getUserIdStmt = this.connection.prepareStatement(getUserIdQuery);
			getUserIdStmt.setString(1, username);
			ResultSet userIdResult = getUserIdStmt.executeQuery();

			if (userIdResult.next()) {
				int leaderId = userIdResult.getInt("ID"); // Leader's User ID

				// Step 2: Insert the group with the retrieved leader ID
				PreparedStatement insertGroupStmt = this.connection.prepareStatement(insertGroupQuery);
				insertGroupStmt.setString(1, groupName);
				insertGroupStmt.setInt(2, leaderId);
				insertGroupStmt.executeUpdate();

				System.out.println("Group '" + groupName + "' created successfully with leader ID: " + leaderId);
			} else {
				System.out.println("No user found with the username: " + username);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
	}

	@Override
	public void sendInvite(String groupName, String username) {
		this.establishConnection();

		// Step 1: Query to get the User ID of the student
		String getUserIdQuery = "SELECT ID FROM User WHERE username = ?";

		// Step 2: Query to get the Group ID from the groupT table using the group name
		String getGroupIdQuery = "SELECT ID FROM groupT WHERE name = ?";

		// Step 3: Insert a new row into the Gr_Inv table to send the invite
		String insertInviteQuery = "INSERT INTO Gr_Inv (GroupID, Stud_ID, Status) VALUES (?, ?, 'pending')";

		try {
			// Step 1: Fetch the User ID of the student
			PreparedStatement getUserIdStmt = this.connection.prepareStatement(getUserIdQuery);
			getUserIdStmt.setString(1, username);
			ResultSet userIdResult = getUserIdStmt.executeQuery();

			if (userIdResult.next()) {
				int studentId = userIdResult.getInt("ID"); // Student's User ID

				// Step 2: Fetch the Group ID from the groupT table
				PreparedStatement getGroupIdStmt = this.connection.prepareStatement(getGroupIdQuery);
				getGroupIdStmt.setString(1, groupName);
				ResultSet groupIdResult = getGroupIdStmt.executeQuery();

				if (groupIdResult.next()) {
					int groupId = groupIdResult.getInt("ID"); // Group's ID

					// Step 3: Insert a new row into the Gr_Inv table to send the invitation
					PreparedStatement insertInviteStmt = this.connection.prepareStatement(insertInviteQuery);
					insertInviteStmt.setInt(1, groupId); // Group ID
					insertInviteStmt.setInt(2, studentId); // Student ID
					insertInviteStmt.executeUpdate();

					System.out.println("Invite sent to '" + username + "' for group '" + groupName + "'");
				} else {
					System.out.println("No group found with the name: " + groupName);
				}
			} else {
				System.out.println("No user found with the username: " + username);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
	}

	@Override
	public User getStudent(String Username) {
		this.establishConnection();
		User user = null;

		String sqlQuery1 = """
				    SELECT *
				    FROM User
				    WHERE username = ?;
				""";

		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, Username);
			ResultSet result1 = preparedStatement1.executeQuery();

			if (result1.next()) {
				UserFactory userFactory = ConcreteUserFactory.getInstance();
				user = userFactory.createUser(result1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
		return user;
	}

	@Override
	public boolean checkGroupExists(String GroupName) {
		this.establishConnection();
		boolean groupExists = false;

		String sqlQuery1 = """
				    SELECT *
				    FROM groupT
				    WHERE name = ?;
				""";

		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, GroupName);
			ResultSet result1 = preparedStatement1.executeQuery();

			if (result1.next()) {
				groupExists = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.closeConnection();
		return groupExists;
	}

	@Override
	public void deleteGroup(String GroupName) {
		this.establishConnection();

		// SQL queries for each table
		String sqlQuery1 = "DELETE FROM Gr_Inv WHERE GroupID = (SELECT ID FROM groupT WHERE name = ?)";
		String sqlQuery2 = "DELETE FROM Gr_Req WHERE GroupID = (SELECT ID FROM groupT WHERE name = ?)";
		String sqlQuery3 = "DELETE FROM Ment_Req WHERE GroupID = (SELECT ID FROM groupT WHERE name = ?)";
		String sqlQuery4 = "DELETE FROM groupT WHERE name = ?";

		try {
			// Prepare and execute the first DELETE statement
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, GroupName);
			preparedStatement1.executeUpdate();

			// Prepare and execute the second DELETE statement
			PreparedStatement preparedStatement2 = this.connection.prepareStatement(sqlQuery2);
			preparedStatement2.setString(1, GroupName);
			preparedStatement2.executeUpdate();

			// Prepare and execute the third DELETE statement
			PreparedStatement preparedStatement3 = this.connection.prepareStatement(sqlQuery3);
			preparedStatement3.setString(1, GroupName);
			preparedStatement3.executeUpdate();

			// Prepare and execute the fourth DELETE statement
			PreparedStatement preparedStatement4 = this.connection.prepareStatement(sqlQuery4);
			preparedStatement4.setString(1, GroupName);
			preparedStatement4.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.closeConnection();
	}

	@Override
	public boolean isGroupLeader(String Username) {
		this.establishConnection();
		boolean isLeader = false;

		String sqlQuery1 = """
				    SELECT *
				    FROM groupT
				    JOIN User ON groupT.leader = User.ID
				    WHERE User.username = ?;
				""";

		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, Username);
			ResultSet result1 = preparedStatement1.executeQuery();

			if (result1.next()) {
				isLeader = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.closeConnection();
		return isLeader;
	}

	@Override
	public String[] getGroupRequests(String username) {
		this.establishConnection();
		String[] requestSenders = new String[10]; // Adjust the array size as needed

		String sqlQuery = """
				    SELECT User.username
				    FROM Gr_Req
				    JOIN groupT ON Gr_Req.GroupID = groupT.ID
				    JOIN User ON Gr_Req.Stud_ID = User.ID
				    WHERE groupT.leader = (
				        SELECT ID
				        FROM User
				        WHERE username = ?
				    );
				""";

		try {
			PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();

			int i = 0;
			while (resultSet.next()) {
				requestSenders[i] = resultSet.getString("username");
				i++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}

		return requestSenders;
	}

	@Override
	public String[] getGroupInvitations(String Username) {
		this.establishConnection();
		String[] invitations = new String[10];

		String sqlQuery1 = """
				    SELECT groupT.name
				    FROM Gr_Inv
				    JOIN groupT ON Gr_Inv.GroupID = groupT.ID
				    JOIN User ON Gr_Inv.Stud_ID = User.ID
				    WHERE User.username = ?;
				""";

		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			preparedStatement1.setString(1, Username);
			ResultSet result1 = preparedStatement1.executeQuery();

			int i = 0;
			while (result1.next()) {
				invitations[i] = result1.getString("name");
				i++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.closeConnection();
		return invitations;
	}

	@Override
	public String[] getAvailableGroups() {
		this.establishConnection();
		String[] availableGroups = new String[10];

		String sqlQuery1 = """
				    SELECT name
				    FROM groupT
				    WHERE student1 IS NULL OR student2 IS NULL;
				""";

		try {
			PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
			ResultSet result1 = preparedStatement1.executeQuery();

			int i = 0;
			while (result1.next()) {
				availableGroups[i] = result1.getString("name");
				i++;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.closeConnection();
		return availableGroups;
	}

	@Override
	public String getGroupLeader(String groupName) {
		String leaderUsername = null;

		String sqlQuery = """
				    SELECT User.username
				    FROM groupT
				    JOIN User ON groupT.leader = User.ID
				    WHERE groupT.name = ?;
				""";

		try {
			this.establishConnection();
			try (PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery)) {
				preparedStatement.setString(1, groupName);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						leaderUsername = resultSet.getString("username"); // Fetch only the username
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}

		return leaderUsername;
	}

	@Override
	public boolean acceptInvite(String groupName, String username) {
	    this.establishConnection();
	    System.out.println("Accepting invite for group '" + groupName + "' by user '" + username + "'");
	    boolean res = false;

	    // SQL query to delete the invite
	    String deleteInviteQuery = """
	        DELETE FROM Gr_Inv
	        WHERE GroupID = (SELECT ID FROM groupT WHERE name = ?)
	          AND Stud_ID = (SELECT ID FROM User WHERE username = ?)
	    """;

	    // SQL queries to check and update group slots
	    String checkStudent1Query = "SELECT student1 FROM groupT WHERE name = ?";
	    String checkStudent2Query = "SELECT student2 FROM groupT WHERE name = ?";
	    String updateStudent1Query = "UPDATE groupT SET student1 = (SELECT ID FROM User WHERE username = ?) WHERE name = ?";
	    String updateStudent2Query = "UPDATE groupT SET student2 = (SELECT ID FROM User WHERE username = ?) WHERE name = ?";

	    try {
	        // Step 1: Delete the invite
	        PreparedStatement deleteInviteStmt = this.connection.prepareStatement(deleteInviteQuery);
	        deleteInviteStmt.setString(1, groupName);
	        deleteInviteStmt.setString(2, username);
	        deleteInviteStmt.executeUpdate();

	        // Step 2: Check and update the appropriate slot
	        PreparedStatement checkStudent1Stmt = this.connection.prepareStatement(checkStudent1Query);
	        checkStudent1Stmt.setString(1, groupName);
	        ResultSet result1 = checkStudent1Stmt.executeQuery();

	        if (result1.next() && (result1.getString("student1") == null || result1.getString("student1").isEmpty())) {
	            // Update student1 if it's empty
	            PreparedStatement updateStudent1Stmt = this.connection.prepareStatement(updateStudent1Query);
	            updateStudent1Stmt.setString(1, username);
	            updateStudent1Stmt.setString(2, groupName);
	            updateStudent1Stmt.executeUpdate();
	            System.out.println("Invite accepted: " + username + " added as student1 in group '" + groupName + "'");
	            res = true;
	        } else {
	            // Check and update student2 if student1 is already filled
	            PreparedStatement checkStudent2Stmt = this.connection.prepareStatement(checkStudent2Query);
	            checkStudent2Stmt.setString(1, groupName);
	            ResultSet result2 = checkStudent2Stmt.executeQuery();

	            if (result2.next() && (result2.getString("student2") == null || result2.getString("student2").isEmpty())) {
	                PreparedStatement updateStudent2Stmt = this.connection.prepareStatement(updateStudent2Query);
	                updateStudent2Stmt.setString(1, username);
	                updateStudent2Stmt.setString(2, groupName);
	                updateStudent2Stmt.executeUpdate();
	                System.out.println("Invite accepted: " + username + " added as student2 in group '" + groupName + "'");
	                res = true;
	            } else {
	                System.out.println("Error: Both student slots are full in group '" + groupName + "'");
	                
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        this.closeConnection();
	    }
	    return res;
	}
	
	@Override
	public boolean declineInvite(String groupName, String username) {
		this.establishConnection();
		System.out.println("Declining invite for group '" + groupName + "' by user '" + username + "'");
		boolean res = false;

		// SQL query to delete the invite
	    String deleteInviteQuery = """
		        DELETE FROM Gr_Inv
		        WHERE GroupID = (SELECT ID FROM groupT WHERE name = ?)
		          AND Stud_ID = (SELECT ID FROM User WHERE username = ?)
		    """;

		try {
			// Delete the invite
			PreparedStatement deleteInviteStmt = this.connection.prepareStatement(deleteInviteQuery);
			deleteInviteStmt.setString(1, groupName);
			deleteInviteStmt.setString(2, username);
			deleteInviteStmt.executeUpdate();
			System.out.println("Invite declined: " + username + " removed from group '" + groupName + "'");
			res = true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
		return res;
	}

	
	@Override
	public boolean sendRequest(String groupName, String username) {
	    this.establishConnection();
	    System.out.println("Sending request for group '" + groupName + "' by user '" + username + "'");
	    boolean res = false;

	    // SQL query to insert the request
	    String insertRequestQuery = """
	                INSERT INTO Gr_Req (GroupID, Stud_ID, Status)
	                VALUES ((SELECT ID FROM groupT WHERE name = ?), (SELECT ID FROM User WHERE username = ?), 'pending')
	            """;

	    try {
	        // Insert the request
	        PreparedStatement insertRequestStmt = this.connection.prepareStatement(insertRequestQuery);
	        insertRequestStmt.setString(1, groupName);
	        insertRequestStmt.setString(2, username);

	        int rowsAffected = insertRequestStmt.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Request sent: " + username + " requested to join group '" + groupName + "'");
	            res = true;
	        } else {
	            System.out.println("Request could not be sent: Either the group or user does not exist.");
	        }

	    } catch (SQLException e) {
	        // Specific handling for common SQL exceptions
	        if (e.getSQLState().startsWith("23")) { // SQL state for constraint violations
	            System.out.println("Request failed: User might have already sent a request to this group.");
	        } else {
	            e.printStackTrace();
	        }
	    } finally {
	        this.closeConnection();
	    }
	    return res;
	}
	
	@Override
	public boolean acceptRequest(String groupName, String username) {
	    this.establishConnection();
	    System.out.println("Accepting request for group '" + groupName + "' by user '" + username + "'");
	    boolean res = false;

	    // SQL query to delete the request
	    String deleteRequestQuery = """
	                DELETE FROM Gr_Req
	                WHERE GroupID = (SELECT ID FROM groupT WHERE name = ?)
	                  AND Stud_ID = (SELECT ID FROM User WHERE username = ?)
	            """;

	    // SQL queries to check and update group slots
	    String checkStudent1Query = "SELECT student1 FROM groupT WHERE name = ?";
	    String checkStudent2Query = "SELECT student2 FROM groupT WHERE name = ?";
	    String updateStudent1Query = "UPDATE groupT SET student1 = (SELECT ID FROM User WHERE username = ?) WHERE name = ?";
	    String updateStudent2Query = "UPDATE groupT SET student2 = (SELECT ID FROM User WHERE username = ?) WHERE name = ?";

	    try {
	        // Step 1: Delete the request
	        PreparedStatement deleteRequestStmt = this.connection.prepareStatement(deleteRequestQuery);
	        deleteRequestStmt.setString(1, groupName);
	        deleteRequestStmt.setString(2, username);
	        int rowsDeleted = deleteRequestStmt.executeUpdate();

	        if (rowsDeleted > 0) {
	            System.out.println("Request deleted successfully for user '" + username + "' in group '" + groupName + "'");
	        } else {
	            System.out.println("Error: No matching request found for user '" + username + "' in group '" + groupName + "'");
	            this.closeConnection();
	            return res; // Exit if no matching request found
	        }

	        // Step 2: Check and update the appropriate slot
	        PreparedStatement checkStudent1Stmt = this.connection.prepareStatement(checkStudent1Query);
	        checkStudent1Stmt.setString(1, groupName);
	        ResultSet result1 = checkStudent1Stmt.executeQuery();

	        if (result1.next() && (result1.getString("student1") == null || result1.getString("student1").isEmpty())) {
	            // Update student1 if it's empty
	            PreparedStatement updateStudent1Stmt = this.connection.prepareStatement(updateStudent1Query);
	            updateStudent1Stmt.setString(1, username);
	            updateStudent1Stmt.setString(2, groupName);
	            updateStudent1Stmt.executeUpdate();
	            System.out.println("Request accepted: " + username + " added as student1 in group '" + groupName + "'");
	            res = true;
	        } else {
	            // Check and update student2 if student1 is already filled
	            PreparedStatement checkStudent2Stmt = this.connection.prepareStatement(checkStudent2Query);
	            checkStudent2Stmt.setString(1, groupName);
	            ResultSet result2 = checkStudent2Stmt.executeQuery();

	            if (result2.next() && (result2.getString("student2") == null || result2.getString("student2").isEmpty())) {
	                PreparedStatement updateStudent2Stmt = this.connection.prepareStatement(updateStudent2Query);
	                updateStudent2Stmt.setString(1, username);
	                updateStudent2Stmt.setString(2, groupName);
	                updateStudent2Stmt.executeUpdate();
	                System.out.println("Request accepted: " + username + " added as student2 in group '" + groupName + "'");
	                res = true;
	            } else {
	                System.out.println("Error: Both student slots are full in group '" + groupName + "'");
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        this.closeConnection();
	    }

	    return res;
	}

	@Override
	public boolean declineRequest(String groupName, String username) {
		this.establishConnection();
		System.out.println("Declining request for group '" + groupName + "' by user '" + username + "'");
		boolean res = false;

		// SQL query to delete the request
		String deleteRequestQuery = """
				    DELETE FROM Gr_Req
				    WHERE GroupID = (SELECT ID FROM groupT WHERE name = ?)
				      AND Stud_ID = (SELECT ID FROM User WHERE username = ?)
				""";

		try {
			// Delete the request
			PreparedStatement deleteRequestStmt = this.connection.prepareStatement(deleteRequestQuery);
			deleteRequestStmt.setString(1, groupName);
			deleteRequestStmt.setString(2, username);
			deleteRequestStmt.executeUpdate();
			System.out.println("Request declined: " + username + " removed from group '" + groupName + "'");
			res = true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeConnection();
		}
		return res;
	}
}

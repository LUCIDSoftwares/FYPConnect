package application.persistance;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.sql.Statement;

import application.datamodel.Admin;
import application.datamodel.Deliverable;
import application.datamodel.Faculty;
import application.datamodel.Mentorship_Request;
import application.datamodel.Project;
import application.datamodel.Resource;
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

	// @Override
	// public int createUser(User user) {
	// 	if(this.establishConnection() == false)
	// 		return -1;
	public boolean saveProject(String title, String description, String docLink, int facultyID) {
	    this.establishConnection();

	    String sqlQuery = "INSERT INTO project (title, description, doc_link, faculty_ID) VALUES (?, ?, ?, ?);";
	    try {
	        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
	        preparedStatement.setString(1, title);
	        preparedStatement.setString(2, description);
	        preparedStatement.setString(3, docLink.isEmpty() ? null : docLink); // Allow null for doc_link
	        preparedStatement.setInt(4, facultyID);
	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println("Exception thrown in the saveProject(String, String, String, int) method of the DBHandler Class");
	        e.printStackTrace();
	        this.closeConnection();
	        return false;
	    }

	    this.closeConnection();
	    return true;
	}
	
//	// Get group members for a specific group
//    public ResultSet getGroupMembers(String groupName) {
//        this.establishConnection();
//        String query = """
//            SELECT u.name
//            FROM GroupT g
//            INNER JOIN User u ON u.ID = g.leader
//            WHERE g.name = ?;
//        """;
//        try {
//            PreparedStatement statement = this.connection.prepareStatement(query);
//            statement.setString(1, groupName);
//            return statement.executeQuery();
//        } catch (SQLException e) {
//            System.out.println("Error fetching group members for group: " + groupName);
//            e.printStackTrace();
//        }
//        return null;
//    }
    
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
    
	public boolean saveResource(String title, String description, String filePath, String uploaderUsername) {
	    this.establishConnection();

	    String sqlQuery = "INSERT INTO Resource (title, description, uploader_username, filePath) VALUES (?, ?, ?, ?)";
	    try {
	        PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
	        preparedStatement.setString(1, title);
	        preparedStatement.setString(2, description);
	        preparedStatement.setString(3, uploaderUsername);
	        preparedStatement.setString(4, filePath);

	        preparedStatement.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println("Exception in saveResource(String, String, String, String) method");
	        e.printStackTrace();
	        this.closeConnection();
	        return false;
	    }

	    this.closeConnection();
	    return true;
	}

		public ResultSet getDeliverablesByFacultyID(int facultyID) {
		    ResultSet resultSet = null;
		    String query = "SELECT * FROM Deliverable WHERE facultyID = ?";

		    try {
		        PreparedStatement preparedStatement = connection.prepareStatement(query);
		        preparedStatement.setInt(1, facultyID);
		        resultSet = preparedStatement.executeQuery();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return resultSet;
		}


		public boolean saveDeliverable(String deadline, String description, String docLink, int facultyID) {
		    this.establishConnection();

		    String query = "INSERT INTO Deliverable (Deadline, description, doc_link, facultyID) VALUES (?, ?, ?, ?)";
		    try {
		        PreparedStatement statement = this.connection.prepareStatement(query);
		        statement.setString(1, deadline);
		        statement.setString(2, description);
		        statement.setString(3, docLink == null ? null : docLink); // Allow null for empty links
		        statement.setInt(4, facultyID);
		        statement.executeUpdate();
		    } catch (SQLException e) {
		        System.out.println("Exception in saveDeliverable() method");
		        e.printStackTrace();
		        this.closeConnection();
		        return false;
		    }

		    this.closeConnection();
		    return true;
		}

	
	public boolean submitGradeForGroup(int deliverableID, String groupName, String grade, String remarks) {
        this.establishConnection();
        String query = """
            INSERT INTO Feedback (Group_ID, Faculty_ID, Submission_ID, Grade, Remarks)
            SELECT g.ID, d.facultyID, s.ID, ?, ?
            FROM GroupT g
            INNER JOIN Submission s ON g.ID = s.groupID
            INNER JOIN Deliverable d ON s.Del_ID = d.ID
            WHERE s.Del_ID = ? AND g.name = ?;
        """;
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, grade);
            statement.setString(2, remarks);
            statement.setInt(3, deliverableID);
            statement.setString(4, groupName);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Exception in submitGradeForGroup(int, String, String, String) method of the DBHandler Class");
            e.printStackTrace();
        }
        return false;
    }
	 
	   public ResultSet getDeliverables() {
	        this.establishConnection();
	        String query = "SELECT title FROM Deliverable";
	        try {
	            Statement stmt = connection.createStatement();
	            return stmt.executeQuery(query);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	 
	   public ResultSet getSubmissionDetails(int deliverableID, String groupName) {
		    this.establishConnection();
		    String query = """
		        SELECT s.content_link, d.description AS deliverable_description, s.submission_time
		        FROM Submission s
		        INNER JOIN Deliverable d ON s.Del_ID = d.ID
		        INNER JOIN GroupT g ON s.groupID = g.ID
		        WHERE s.Del_ID = ? AND g.name = ?;
		    """;
		    try {
		        PreparedStatement statement = this.connection.prepareStatement(query);
		        statement.setInt(1, deliverableID);
		        statement.setString(2, groupName);
		        return statement.executeQuery(); // Return the ResultSet
		    } catch (SQLException e) {
		        System.out.println("Error fetching submission details.");
		        e.printStackTrace();
		    }
		    return null;
		}

	 public ResultSet getGroupNames(int deliverableID) {
	        this.establishConnection();
	        String query = """
	            SELECT g.name AS group_name
	            FROM GroupT g
	            INNER JOIN Submission s ON g.ID = s.groupID
	            WHERE s.Del_ID = ?;
	        """;
	        try {
	            PreparedStatement statement = this.connection.prepareStatement(query);
	            statement.setInt(1, deliverableID);
	            return statement.executeQuery();
	        } catch (SQLException e) {
	            System.out.println("Exception in getGroupNames(int) method of the DBHandler Class");
	            e.printStackTrace();
	        }
	        return null;
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

		String sqlQuery1 = "SELECT * \r\n" + "FROM User \r\n" + "WHERE username = ? \r\n" + "AND password = ?;";
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



	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// @Override
	// public User retrieveUser(String username) {
	// 	this.establishConnection();
	// 	User user = null;

	// 	String sqlQuery1 = "SELECT * \r\n" + "FROM User \r\n" + "WHERE username = ?;";
	// 	try {
	// 		PreparedStatement preparedStatement1 = this.connection.prepareStatement(sqlQuery1);
	// 		preparedStatement1.setString(1, username);
	// 		ResultSet result1 = preparedStatement1.executeQuery();
	// 		if (result1.next()) {
	// 			UserFactory concreteUserFactory = ConcreteUserFactory.getInstance();
	// 			user = concreteUserFactory.createUser(result1);
	// 		}

	// 	} catch (SQLException e) {
	// 		System.out.println("Exception thrown in the retrieveUser(String, String) method of the DBHandler Class");
	// 		e.printStackTrace();
	// 	}

	// 	this.closeConnection();
	// 	return user;
	// }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
				    SELECT u.ID, u.name, u.password, u.username, u.email, u.usertype
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

				if (result2.next()
						&& (result2.getString("student2") == null || result2.getString("student2").isEmpty())) {
					PreparedStatement updateStudent2Stmt = this.connection.prepareStatement(updateStudent2Query);
					updateStudent2Stmt.setString(1, username);
					updateStudent2Stmt.setString(2, groupName);
					updateStudent2Stmt.executeUpdate();
					System.out.println(
							"Invite accepted: " + username + " added as student2 in group '" + groupName + "'");
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
				System.out.println(
						"Request deleted successfully for user '" + username + "' in group '" + groupName + "'");
			} else {
				System.out.println(
						"Error: No matching request found for user '" + username + "' in group '" + groupName + "'");
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

				if (result2.next()
						&& (result2.getString("student2") == null || result2.getString("student2").isEmpty())) {
					PreparedStatement updateStudent2Stmt = this.connection.prepareStatement(updateStudent2Query);
					updateStudent2Stmt.setString(1, username);
					updateStudent2Stmt.setString(2, groupName);
					updateStudent2Stmt.executeUpdate();
					System.out.println(
							"Request accepted: " + username + " added as student2 in group '" + groupName + "'");
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
//<<<<<<< Updated upstream
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

//=======
	@Override
	public ArrayList<Resource> getAllResources() {
		if (this.establishConnection() == false)
			return null;

		ArrayList<Resource> resourceArrayList = null;

		try {
			String sqlQuery1 = "SELECT *\r\n" + "FROM Resource;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			ResultSet result1 = statement1.executeQuery();

			// if there is at least one result from the query
			if (result1.next()) {
				resourceArrayList = new ArrayList<Resource>();
				Resource resource = new Resource(result1.getInt("ID"), result1.getString("title"),
						result1.getString("description"), result1.getString("uploader_username"),
						result1.getString("filePath"));
				resourceArrayList.add(resource);

				// for the rest of the records
				while (result1.next()) {
					resource = new Resource(result1.getInt("ID"), result1.getString("title"),
							result1.getString("description"), result1.getString("uploader_username"),
							result1.getString("filePath"));
					resourceArrayList.add(resource);
				}
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown in the getAllResources() method of the DBHandler Class");
			e.printStackTrace();
		}

		this.closeConnection();
		return resourceArrayList;
	}

	@Override
	public ArrayList<Deliverable> getAllDeliverables() {
		if (this.establishConnection() == false)
			return null;

		ArrayList<Deliverable> deliverableList = null;

		try {
			String sqlQuery = """
					    SELECT *
					    FROM Deliverable;
					""";
			PreparedStatement statement = this.connection.prepareStatement(sqlQuery);
			ResultSet resultSet = statement.executeQuery();

			// get faculty username from faculty ID
			String facultyUsernameQuery = "SELECT username FROM User WHERE ID = ?";
			PreparedStatement facultyUsernameStatement = this.connection.prepareStatement(facultyUsernameQuery);

			// If there is at least one result from the query
			if (resultSet.next()) {
				deliverableList = new ArrayList<>();

				facultyUsernameStatement.setInt(1, resultSet.getInt("facultyID"));
				ResultSet facultyUsernameResultSet = facultyUsernameStatement.executeQuery();
				facultyUsernameResultSet.next();
				String facultyUsername = facultyUsernameResultSet.getString("username");

				// Map the first result
				Deliverable deliverable = new Deliverable(resultSet.getInt("ID"),
						resultSet.getTimestamp("Deadline").toLocalDateTime(), resultSet.getString("description"),
						resultSet.getString("doc_link"), facultyUsername);
				deliverableList.add(deliverable);

				// For the rest of the records
				while (resultSet.next()) {

					facultyUsernameStatement.setInt(1, resultSet.getInt("facultyID"));
					ResultSet facultyUsernameResultSet1 = facultyUsernameStatement.executeQuery();
					facultyUsernameResultSet1.next();
					facultyUsername = facultyUsernameResultSet1.getString("username");

					deliverable = new Deliverable(resultSet.getInt("ID"),
							resultSet.getTimestamp("Deadline").toLocalDateTime(), resultSet.getString("description"),
							resultSet.getString("doc_link"), facultyUsername);
					deliverableList.add(deliverable);
				}
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown in the getAllDeliverables() method of the DBHandler Class");
			e.printStackTrace();
		}

		this.closeConnection();
		return deliverableList;
	}

//	@Override
//	public ArrayList<Deliverable> getAllSubmittedDeliverables(String group_name) {
//		if (this.establishConnection() == false)
//			return null;
//
//		ArrayList<Deliverable> deliverableList = null;
//
//		try {
//			
//			// SQL query to get the group ID
//			String getGroupIdQuery = "SELECT ID FROM groupT WHERE name = ?";
//			PreparedStatement getGroupIdStmt = this.connection.prepareStatement(getGroupIdQuery);
//			getGroupIdStmt.setString(1, group_name);
//			ResultSet groupIdResult = getGroupIdStmt.executeQuery();
//			int groupId = 0;
//			if (groupIdResult.next()) {
//				groupId = groupIdResult.getInt("ID");
//			}
//			
//			//query to get the deliverables submitted by the group
//			String sqlQuery = """
//					    SELECT Deliverable.ID, Deliverable.Deadline, Deliverable.description, Deliverable.doc_link, User.username
//					    FROM Deliverable
//					    JOIN Submission ON Deliverable.ID = Submission.Del_ID
//					    JOIN groupT ON Submission.groupID = groupT.ID
//					    JOIN User ON Deliverable.facultyID = User.ID
//					    WHERE groupT.ID = ?;
//					""";
//			
//			PreparedStatement statement = this.connection.prepareStatement(sqlQuery);
//			statement.setInt(1, groupId);
//			ResultSet resultSet = statement.executeQuery();
//
//			// get faculty username from faculty ID
//			String facultyUsernameQuery = "SELECT username FROM User WHERE ID = ?";
//			PreparedStatement facultyUsernameStatement = this.connection.prepareStatement(facultyUsernameQuery);
//
//			// If there is at least one result from the query
//			if (resultSet.next()) {
//				deliverableList = new ArrayList<>();
//
//				facultyUsernameStatement.setInt(1, resultSet.getInt("facultyID"));
//				ResultSet facultyUsernameResultSet = facultyUsernameStatement.executeQuery();
//				facultyUsernameResultSet.next();
//				String facultyUsername = facultyUsernameResultSet.getString("username");
//
//				// Map the first result
//				Deliverable deliverable = new Deliverable(resultSet.getInt("ID"),
//						resultSet.getTimestamp("Deadline").toLocalDateTime(), resultSet.getString("description"),
//						resultSet.getString("doc_link"), facultyUsername);
//				deliverableList.add(deliverable);
//
//				// For the rest of the records
//				while (resultSet.next()) {
//
//					facultyUsernameStatement.setInt(1, resultSet.getInt("facultyID"));
//					ResultSet facultyUsernameResultSet1 = facultyUsernameStatement.executeQuery();
//					facultyUsernameResultSet1.next();
//					facultyUsername = facultyUsernameResultSet1.getString("username");
//
//					deliverable = new Deliverable(resultSet.getInt("ID"),
//							resultSet.getTimestamp("Deadline").toLocalDateTime(), resultSet.getString("description"),
//							resultSet.getString("doc_link"), facultyUsername);
//					deliverableList.add(deliverable);
//				}
//			}
//
//		} catch (SQLException e) {
//			System.out.println("Exception thrown in the getAllDeliverables() method of the DBHandler Class");
//			e.printStackTrace();
//		}
//
//		this.closeConnection();
//		return deliverableList;
//	}
	
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

	



	
	
	//grade students ftnality
	
	public ResultSet getDeliverablesByFaculty(int facultyId) {
        String query = "SELECT ID, description FROM Deliverable WHERE facultyID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, facultyId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getGroups() {
        String query = "SELECT ID, name FROM groupT";
        try {
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getSubmissionContentLink(int deliverableId, int groupId) {
        System.out.println("hello");

        String query = "SELECT content_link, submission_time FROM Submission WHERE Del_ID = ? AND groupID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, deliverableId);
            stmt.setInt(2, groupId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveFeedback(int groupId, int facultyId, int submissionId, String grade, String remarks) {
        String query = "INSERT INTO Feedback (Group_ID, Faculty_ID, Submission_ID, Grade, Remarks) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, groupId);
            stmt.setInt(2, facultyId);
            stmt.setInt(3, submissionId);
            stmt.setString(4, grade);
            stmt.setString(5, remarks);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getDeliverableId(String description) {
        String query = "SELECT ID FROM Deliverable WHERE description = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, description);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }

    
    public int getGroupId(String groupName) {
        String query = "SELECT ID FROM groupT WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, groupName);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
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
	
	public int deleteUserById(int userId) {
		if(this.establishConnection() == false)
			return -1;
		
		int flag = 0;
			
		try {
			// first check if the user exists in the system 
			// and if yes what is is user type
			String sqlQuery1 = "SELECT COUNT(1), usertype\r\n"
					+ "FROM User\r\n"
					+ "WHERE ID = ?;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setInt(1, userId);
			
			ResultSet result1 = statement1.executeQuery();
			boolean userExists = false;
			String userType = "Admin";
				
			if(result1.next()) {
				if(result1.getInt(1) >= 1) {
					userExists = true;
					flag = 1;
					userType = result1.getString(2);
				}
			}
			
			// user exists and is a faculty member
			if(userExists && userType.equalsIgnoreCase("Faculty")) {
				flag = 2;

				// check if they have uploaded a project
				String sqlQuery2 = "SELECT COUNT(1)\r\n"
						+ "FROM project\r\n"
						+ "WHERE faculty_ID = ?;";
				PreparedStatement statement2 = this.connection.prepareStatement(sqlQuery2);
				statement2.setInt(1, userId);
				
				ResultSet result2 = statement2.executeQuery();
				boolean hasAProjectUploaded = false;
				
				if(result2.next()) {
					if(result2.getInt(1) > 0) {
						hasAProjectUploaded = true;
						flag = 2;
					}
					
					// now we can delete the user
					if(hasAProjectUploaded == false) {
						
						String sqlQuery3 = "DELETE FROM User WHERE ID = ?;";
						PreparedStatement statement3 = this.connection.prepareStatement(sqlQuery3);
						statement3.setInt(1, userId);
				
						if(statement3.executeUpdate() <= 0)
							flag = -1;
						else 
							flag = 4;
					}
				}
				else
					flag = -1;
			}
			else if(userExists && userType.equalsIgnoreCase("Student")) {
				flag = 3;
				// check if they are in a group
				String sqlQuery2 = "SELECT COUNT(1)\r\n"
						+ "FROM groupT\r\n"
						+ "WHERE leader = ? || student1 = ? || student2 = ?;";
				PreparedStatement statement2 = this.connection.prepareStatement(sqlQuery2);
				statement2.setInt(1, userId);
				statement2.setInt(2, userId);
				statement2.setInt(3, userId);
				
				ResultSet result2 = statement2.executeQuery();
				boolean isInAGroup = false;
				
				if(result2.next()) {
					if(result2.getInt(1) > 0) {
						isInAGroup = true;
						flag = 3;
					}
					
					// now we can delete the user
					if(isInAGroup == false) {
						
						String sqlQuery3 = "DELETE FROM User WHERE ID = ?;";
						PreparedStatement statement3 = this.connection.prepareStatement(sqlQuery3);
						statement3.setInt(1, userId);
				
						if(statement3.executeUpdate() <= 0)
							flag = -1;
						else 
							flag = 4;
					}
				}
				else
					flag = -1;
			}
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the deleteUserById(int) method of the DBHandler Class");
			e.printStackTrace();
			flag = -1;
		}
		
		this.closeConnection();
		return flag;
	}
	
	public int deleteUserByUsername(String username) {
		if(this.establishConnection() == false)
			return -1;
		
		int flag = 0;
			
		try {
			// first check if the user exists in the system 
			// and if yes what is is user type
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
					flag = 1;
					userType = result1.getString(2);
				}
			}
			
			// user exists and is a faculty member
			if(userExists && userType.equalsIgnoreCase("Faculty")) {
				flag = 2;

				// check if they have uploaded a project
				String sqlQuery2 = "SELECT COUNT(1)\r\n"
						+ "FROM user AS u\r\n"
						+ "INNER JOIN project AS p\r\n"
						+ "on u.ID = p.faculty_ID\r\n"
						+ "WHERE u.username = ?;";
				PreparedStatement statement2 = this.connection.prepareStatement(sqlQuery2);
				statement2.setString(1, username);
				
				ResultSet result2 = statement2.executeQuery();
				boolean hasAProjectUploaded = false;
				
				if(result2.next()) {
					if(result2.getInt(1) >= 1) {
						hasAProjectUploaded = true;
						flag = 2;
					}
					
					// now we can delete the user
					if(hasAProjectUploaded == false) {
						
						String sqlQuery3 = "DELETE FROM User WHERE username = ?;";
						PreparedStatement statement3 = this.connection.prepareStatement(sqlQuery3);
						statement3.setString(1, username);
				
						if(statement3.executeUpdate() <= 0)
							flag = -1;
						else 
							flag = 4;
					}
				}
				else
					flag = -1;
			}
			else if(userExists && userType.equalsIgnoreCase("Student")) {
				flag = 3;
				// check if they are in a group
				String sqlQuery2 = "SELECT COUNT(1)\r\n"
						+ "FROM User AS u\r\n"
						+ "INNER JOIN groupT AS g\r\n"
						+ "ON (u.ID = g.leader OR u.ID = g.student1 OR u.ID = g.student2)\r\n"
						+ "WHERE u.username = ?;";
				PreparedStatement statement2 = this.connection.prepareStatement(sqlQuery2);
				statement2.setString(1, username);
//				statement2.setInt(2, userId);
//				statement2.setInt(3, userId);
				
				ResultSet result2 = statement2.executeQuery();
				boolean isInAGroup = false;
				
				if(result2.next()) {
					if(result2.getInt(1) >= 1) {
						isInAGroup = true;
						flag = 3;
					}
					
					// now we can delete the user
					if(isInAGroup == false) {
						
						String sqlQuery3 = "DELETE FROM User WHERE username = ?;";
						PreparedStatement statement3 = this.connection.prepareStatement(sqlQuery3);
						statement3.setString(1, username);
				
						if(statement3.executeUpdate() <= 0)
							flag = -1;
						else 
							flag = 4;
					}
				}
				else
					flag = -1;
			}
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the deleteUserById(int) method of the DBHandler Class");
			e.printStackTrace();
			flag = -1;
		}
		
		this.closeConnection();
		return flag;
	}
	
	
	@Override
	public ArrayList<Deliverable> getAllSubmittedDeliverables(String group_name) {
	    ArrayList<Deliverable> deliverableList = new ArrayList<>(); // Initialize as empty list

	    if (!this.establishConnection()) {
	        return null;
	    }

	    try {
	        // Query to get the group ID
	        String getGroupIdQuery = "SELECT ID FROM groupT WHERE name = ?";
	        PreparedStatement getGroupIdStmt = this.connection.prepareStatement(getGroupIdQuery);
	        getGroupIdStmt.setString(1, group_name);
	        ResultSet groupIdResult = getGroupIdStmt.executeQuery();

	        int groupId = 0;
	        if (groupIdResult.next()) {
	            groupId = groupIdResult.getInt("ID");
	        } else {
	            System.out.println("Group not found for name: " + group_name);
	            return deliverableList; // Return empty list if group not found
	        }

	        // Query to get the deliverables along with faculty username
	        String sqlQuery = """
	            SELECT Deliverable.ID, Deliverable.Deadline, Deliverable.description, Deliverable.doc_link, 
	                   User.username AS facultyUsername
	            FROM Deliverable
	            JOIN Submission ON Deliverable.ID = Submission.Del_ID
	            JOIN groupT ON Submission.groupID = groupT.ID
	            JOIN User ON Deliverable.facultyID = User.ID
	            WHERE groupT.ID = ?;
	        """;

	        PreparedStatement statement = this.connection.prepareStatement(sqlQuery);
	        statement.setInt(1, groupId);
	        ResultSet resultSet = statement.executeQuery();

	        // Loop through the results and map to Deliverable objects
	        while (resultSet.next()) {
	            Deliverable deliverable = new Deliverable(
	                resultSet.getInt("ID"),
	                resultSet.getTimestamp("Deadline").toLocalDateTime(),
	                resultSet.getString("description"),
	                resultSet.getString("doc_link"),
	                resultSet.getString("facultyUsername") // Directly fetch username
	            );
	            deliverableList.add(deliverable);
	        }

	    } catch (SQLException e) {
	        System.out.println("Exception occurred in getAllSubmittedDeliverables() method.");
	        e.printStackTrace();
	    } finally {
	        // Ensure connection is closed in all cases
	        this.closeConnection();
	    }

	    return deliverableList;
	}

	
	
	
	
	@Override
	public ArrayList<Resource> getResourcesByTitle(String title) {
		if (this.establishConnection() == false)
			return null;

		ArrayList<Resource> resourceArrayList = null;

		try {
			String sqlQuery1 = "SELECT *\r\n" + "FROM Resource\r\n" + "WHERE title like ?;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setString(1, "%" + title + "%");
			ResultSet result1 = statement1.executeQuery();

			// if there is at least one result from the query
			if (result1.next()) {
				resourceArrayList = new ArrayList<Resource>();
				Resource resource = new Resource(result1.getInt("ID"), result1.getString("title"),
						result1.getString("description"), result1.getString("uploader_username"),
						result1.getString("filePath"));
				resourceArrayList.add(resource);

				// for the rest of the records
				while (result1.next()) {
					resource = new Resource(result1.getInt("ID"), result1.getString("title"),
							result1.getString("description"), result1.getString("uploader_username"),
							result1.getString("filePath"));
					resourceArrayList.add(resource);
				}
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown in the getAllResources(String) method of the DBHandler Class");
			e.printStackTrace();
		}

		this.closeConnection();
		return resourceArrayList;
	}

	@Override
	public boolean saveSubmission(int deliverableId, String groupId, Date submissionTime, String filePath) {

		this.establishConnection();
		int groupIdreal = 0;
		// SQL query to get the group ID
		String getGroupIdQuery = "SELECT ID FROM groupT WHERE name = ?";
		try {
			PreparedStatement getGroupIdStmt = this.connection.prepareStatement(getGroupIdQuery);
			getGroupIdStmt.setString(1, groupId);
			ResultSet groupIdResult = getGroupIdStmt.executeQuery();
			if (groupIdResult.next()) {
				groupIdreal = groupIdResult.getInt("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String sqlQuery = "INSERT INTO Submission (Del_ID, submission_time, groupID, content_link) VALUES (?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery)) {
			preparedStatement.setInt(1, deliverableId);
			preparedStatement.setTimestamp(2, new java.sql.Timestamp(submissionTime.getTime()));
			preparedStatement.setInt(3, groupIdreal);
			preparedStatement.setString(4, filePath);

			int rowsAffected = preparedStatement.executeUpdate();
			this.closeConnection();
			return rowsAffected > 0; // Return true if the insertion was successful
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error occurred while saving submission to database.");
			this.closeConnection();
			return false;
		}

	}

	// returns the list of all projects which haven't been awarded to a group
	public ArrayList<Project> getAllAvailableProjects() {
		if(this.establishConnection() == false)
			return null;
		
		ArrayList<Project> availableProjectArrayList = null;
		
		try {
			String sqlQuery1 = "SELECT p.ID, p.title, p.description, p.doc_link, p.faculty_ID, u.name AS 'faculty_name', u.username AS 'faculty_username'\r\n"
					+ "FROM project AS p \r\n"
					+ "LEFT OUTER JOIN groupT AS g\r\n"
					+ "ON p.ID = g.projectID\r\n"
					+ "INNER JOIN User AS u\r\n"
					+ "ON p.faculty_ID = u.ID\r\n"
					+ "WHERE g.student1 IS NULL OR g.student2 IS NULL;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			
			ResultSet result1 = statement1.executeQuery();
			if(result1.next()) {
				availableProjectArrayList = new ArrayList<Project>();
				Project proj = new Project(result1.getInt("ID"),
											result1.getString("title"),
											result1.getString("description"),
											result1.getString("doc_link"),
											result1.getInt("faculty_ID"),
											result1.getString("faculty_username"),
											result1.getString("faculty_name") );
				availableProjectArrayList.add(proj);
				
				while(result1.next()) {
					proj = new Project(result1.getInt("ID"),
							result1.getString("title"),
							result1.getString("description"),
							result1.getString("doc_link"),
							result1.getInt("faculty_ID"),
							result1.getString("faculty_username"),
							result1.getString("faculty_name") );				
					availableProjectArrayList.add(proj);
				}
				
			}
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the getAllAvailableProjects() method of the DBHandler Class");
			e.printStackTrace();
		}
		
		
		this.closeConnection();
		return availableProjectArrayList;
	}
	
	public int getGroupId(int userId) {
		if(this.establishConnection() == false)
			return -1;
		int groupId = -1;
		
		try {
			String sqlQuery1 = "SELECT ID\r\n"
					+ "FROM groupT\r\n"
					+ "WHERE leader = ? || student1 = ? || student2 = ?;";
			PreparedStatement statement1;
	
			statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setInt(1, userId);
			statement1.setInt(2, userId);
			statement1.setInt(3, userId);
			
			ResultSet result1 = statement1.executeQuery();
			if(result1.next())
				groupId = result1.getInt(1);
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the getGroupId(int) mehtod of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return groupId;
	}
	
	public ArrayList<Project> getAllAvailableProjects(String projectTitle, int groupId) {
		if(this.establishConnection() == false)
			return null;
		
		ArrayList<Project> availableProjectArrayList = null;
		
		try {
//			String sqlQuery1 = "SELECT p.ID, p.title, p.description, p.doc_link, p.faculty_ID, u.name AS 'faculty_name', u.username AS 'faculty_username'\r\n"
//					+ "FROM project AS p \r\n"
//					+ "LEFT OUTER JOIN groupT AS g\r\n"
//					+ "ON p.ID = g.projectID\r\n"
//					+ "INNER JOIN User AS u\r\n"
//					+ "ON p.faculty_ID = u.ID\r\n"
//					+ "WHERE g.student1 IS NULL OR g.student2 IS NULL;";
			
			String sqlQuery1 = "SELECT p.ID, p.title, p.description, p.doc_link, p.faculty_ID, u.name AS 'faculty_name', u.username AS 'faculty_username'\r\n"
					+ "FROM project AS p\r\n"
					+ "LEFT OUTER JOIN Ment_Req AS m\r\n"
					+ "ON p.ID = m.PROJECT_ID\r\n"
					+ "INNER JOIN User AS u\r\n"
					+ "ON p.faculty_ID = u.ID\r\n"
					+ "WHERE (m.Status IS NULL\r\n"
					+ "OR (m.Status <> 'accepted' AND (m.GroupID <> ? || m.status <> 'pending')))";
			
			if(projectTitle != null)
				sqlQuery1 = sqlQuery1 + " AND p.title LIKE ?;";
			else
				sqlQuery1 = sqlQuery1 + ";";
			
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setInt(1, groupId);
			if(projectTitle != null)
				statement1.setString(2, "%" + projectTitle + "%");
			
			ResultSet result1 = statement1.executeQuery();
			if(result1.next()) {
				availableProjectArrayList = new ArrayList<Project>();
				Project proj = new Project(result1.getInt("ID"),
											result1.getString("title"),
											result1.getString("description"),
											result1.getString("doc_link"),
											result1.getInt("faculty_ID"),
											result1.getString("faculty_username"),
											result1.getString("faculty_name") );
				availableProjectArrayList.add(proj);
				
				while(result1.next()) {
					proj = new Project(result1.getInt("ID"),
							result1.getString("title"),
							result1.getString("description"),
							result1.getString("doc_link"),
							result1.getInt("faculty_ID"),
							result1.getString("faculty_username"),
							result1.getString("faculty_name") );				
					availableProjectArrayList.add(proj);
				}
				
			}
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the getAllAvailableProjects(String, int) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return availableProjectArrayList;
	}
	
	public ArrayList<Project> getAllProjectsWithMentorshipRequest(String projectTitle, int groupId) {
		if(this.establishConnection() == false)
			return null;
		
		ArrayList<Project> availableProjectArrayList = null;
		
		try {
//			String sqlQuery1 = "SELECT p.ID, p.title, p.description, p.doc_link, p.faculty_ID, u.name AS 'faculty_name', u.username AS 'faculty_username'\r\n"
//					+ "FROM project AS p \r\n"
//					+ "LEFT OUTER JOIN groupT AS g\r\n"
//					+ "ON p.ID = g.projectID\r\n"
//					+ "INNER JOIN User AS u\r\n"
//					+ "ON p.faculty_ID = u.ID\r\n"
//					+ "WHERE g.student1 IS NULL OR g.student2 IS NULL;";
			
			String sqlQuery1 = "SELECT p.ID, p.title, p.description, p.doc_link, p.faculty_ID, u.name AS 'faculty_name', u.username AS 'faculty_username'\r\n"
					+ "FROM project AS p\r\n"
					+ "INNER JOIN Ment_Req AS m\r\n"
					+ "ON p.ID = m.PROJECT_ID\r\n"
					+ "INNER JOIN User AS u\r\n"
					+ "ON p.faculty_ID = u.ID\r\n"
					+ "WHERE m.Status = 'pending'\r\n"
					+ "AND m.GroupID = ?";
			
			if(projectTitle != null)
				sqlQuery1 = sqlQuery1 + " AND p.title LIKE ?;";
			else
				sqlQuery1 = sqlQuery1 + ";";
			
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setInt(1, groupId);
			if(projectTitle != null)
				statement1.setString(2, "%" + projectTitle + "%");
			
			ResultSet result1 = statement1.executeQuery();
			if(result1.next()) {
				availableProjectArrayList = new ArrayList<Project>();
				Project proj = new Project(result1.getInt("ID"),
											result1.getString("title"),
											result1.getString("description"),
											result1.getString("doc_link"),
											result1.getInt("faculty_ID"),
											result1.getString("faculty_username"),
											result1.getString("faculty_name") );
				availableProjectArrayList.add(proj);
				
				while(result1.next()) {
					proj = new Project(result1.getInt("ID"),
							result1.getString("title"),
							result1.getString("description"),
							result1.getString("doc_link"),
							result1.getInt("faculty_ID"),
							result1.getString("faculty_username"),
							result1.getString("faculty_name") );				
					availableProjectArrayList.add(proj);
				}
				
			}
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the getAllProjectsWithMentorshipRequest(String, int) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return availableProjectArrayList;
	}
	
	public ArrayList<Project> getAllProjectsWithDeclinedRequests(String projectTitle, int groupId) {
		if(this.establishConnection() == false)
			return null;
		
		ArrayList<Project> availableProjectArrayList = null;
		
		try {
//			String sqlQuery1 = "SELECT p.ID, p.title, p.description, p.doc_link, p.faculty_ID, u.name AS 'faculty_name', u.username AS 'faculty_username'\r\n"
//					+ "FROM project AS p \r\n"
//					+ "LEFT OUTER JOIN groupT AS g\r\n"
//					+ "ON p.ID = g.projectID\r\n"
//					+ "INNER JOIN User AS u\r\n"
//					+ "ON p.faculty_ID = u.ID\r\n"
//					+ "WHERE g.student1 IS NULL OR g.student2 IS NULL;";
			
			String sqlQuery1 = "SELECT p.ID, p.title, p.description, p.doc_link, p.faculty_ID, u.name AS 'faculty_name', u.username AS 'faculty_username'\r\n"
					+ "FROM project AS p\r\n"
					+ "INNER JOIN Ment_Req AS m\r\n"
					+ "ON p.ID = m.PROJECT_ID\r\n"
					+ "INNER JOIN User AS u\r\n"
					+ "ON p.faculty_ID = u.ID\r\n"
					+ "WHERE m.Status = 'declined'\r\n"
					+ "AND m.GroupID = ?";
			
			if(projectTitle != null)
				sqlQuery1 = sqlQuery1 + " AND p.title LIKE ?;";
			else
				sqlQuery1 = sqlQuery1 + ";";
			
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setInt(1, groupId);
			if(projectTitle != null)
				statement1.setString(2, "%" + projectTitle + "%");
			
			ResultSet result1 = statement1.executeQuery();
			if(result1.next()) {
				availableProjectArrayList = new ArrayList<Project>();
				Project proj = new Project(result1.getInt("ID"),
											result1.getString("title"),
											result1.getString("description"),
											result1.getString("doc_link"),
											result1.getInt("faculty_ID"),
											result1.getString("faculty_username"),
											result1.getString("faculty_name") );
				availableProjectArrayList.add(proj);
				
				while(result1.next()) {
					proj = new Project(result1.getInt("ID"),
							result1.getString("title"),
							result1.getString("description"),
							result1.getString("doc_link"),
							result1.getInt("faculty_ID"),
							result1.getString("faculty_username"),
							result1.getString("faculty_name") );				
					availableProjectArrayList.add(proj);
				}
				
			}
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the getAllProjectsWithDeclinedRequests(String, int) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return availableProjectArrayList;
	}
	
	public void createMentorshipRequest(int projectId, int groupId) {
		if(this.establishConnection() == false)
			return;
		
		try {
			String sqlQuery1 = "INSERT INTO Ment_Req (GroupID, Project_ID, Status) VALUES (?, ?, 'pending');";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setInt(1, groupId);
			statement1.setInt(2, projectId);
			
			if(statement1.executeUpdate() <= 0)
				System.out.println("Could not create a mentorship request");
			else
				System.out.println("ban gayi mentorship request");
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the createMentorshipRequest(int, int) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
	}
	
	@Override
	public String getFeedback(int deliverableId, String groupName) {
	    this.establishConnection();
	    String feedback = null;

	    // SQL query to get the group ID
	    String getGroupIdQuery = "SELECT ID FROM groupT WHERE name = ?";
	    int groupId = 0;

	    try (PreparedStatement getGroupIdStmt = this.connection.prepareStatement(getGroupIdQuery)) {
	        getGroupIdStmt.setString(1, groupName);
	        try (ResultSet groupIdResult = getGroupIdStmt.executeQuery()) {
	            if (groupIdResult.next()) {
	                groupId = groupIdResult.getInt("ID");
	            } else {
	                System.out.println("Group not found for name: " + groupName);
	                return null; // Early exit if group does not exist
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null; // Return null in case of failure
	    }

	    // SQL query to get feedback
	    String sqlQuery = """
	        SELECT f.Remarks AS feedback 
	        FROM Feedback f
	        INNER JOIN Submission s ON f.Submission_ID = s.ID
	        WHERE s.Del_ID = ? AND s.groupID = ?""";

	    try (PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery)) {
	        preparedStatement.setInt(1, deliverableId);
	        preparedStatement.setInt(2, groupId);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                feedback = resultSet.getString("feedback");
	            } else {
	                System.out.println("No feedback found for deliverable ID " + deliverableId + " and group ID " + groupId);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        this.closeConnection();
	    }

	    return feedback;
	}

	@Override
	public String getGrade(int deliverableId, String group_name) {
		this.establishConnection();
        String grade = null;

        // SQL query to get the group ID
        String getGroupIdQuery = "SELECT ID FROM groupT WHERE name = ?";
        int groupId = 0;

        try {
            PreparedStatement getGroupIdStmt = this.connection.prepareStatement(getGroupIdQuery);
            getGroupIdStmt.setString(1, group_name);
            ResultSet groupIdResult = getGroupIdStmt.executeQuery();
            if (groupIdResult.next()) {
                groupId = groupIdResult.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlQuery = """
                    SELECT grade
                    FROM Feedback
                    JOIN Submission ON Feedback.Submission_ID = Submission.ID
                    WHERE Submission.Del_ID = ? AND Submission.groupID = ?;
                """;

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, deliverableId);
            preparedStatement.setInt(2, groupId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                grade = resultSet.getString("grade");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.closeConnection();
        return grade;
    }
	
	
	@Override
	public String getGrader(int deliverableId, String groupName) {
	    this.establishConnection();
	    String grader = null;

	    // SQL query to get the group ID
	    String getGroupIdQuery = "SELECT ID FROM groupT WHERE name = ?";
	    int groupId = 0;

	    try (PreparedStatement getGroupIdStmt = this.connection.prepareStatement(getGroupIdQuery)) {
	        getGroupIdStmt.setString(1, groupName);
	        try (ResultSet groupIdResult = getGroupIdStmt.executeQuery()) {
	            if (groupIdResult.next()) {
	                groupId = groupIdResult.getInt("ID");
	            } else {
	                System.out.println("Group not found for name: " + groupName);
	                return null; // Early exit if group does not exist
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null; // Return null in case of failure
	    }

	    // SQL query to fetch the grader's username
	    String sqlQuery = """
	        SELECT u.username
	        FROM User u
	        JOIN Feedback f ON u.ID = f.Faculty_ID
	        JOIN Submission s ON f.Submission_ID = s.ID
	        WHERE s.Del_ID = ? AND s.groupID = ?""";

	    try (PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery)) {
	        preparedStatement.setInt(1, deliverableId);
	        preparedStatement.setInt(2, groupId);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                grader = resultSet.getString("username");
	            } else {
	                System.out.println("No grader found for deliverable ID " + deliverableId + " and group ID " + groupId);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        this.closeConnection();
	    }

	    return grader;
	}

	
	public Project getProjectByGroupId(int groupId) {
		if(this.establishConnection() == false)
			return null;
		Project project = null;
		
		try {
			String sqlQuery1 = "SELECT p.ID, p.title, p.description, p.doc_link, p.faculty_ID, u.name AS 'faculty_name', u.username AS 'faculty_username'\r\n"
					+ "FROM project AS p\r\n"
					+ "INNER JOIN groupT AS g\r\n"
					+ "ON p.ID = g.projectID\r\n"
					+ "INNER JOIN User AS u\r\n"
					+ "ON p.faculty_ID = u.ID \r\n"
					+ "WHERE g.ID = ?";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setInt(1, groupId);
			
			ResultSet result1 = statement1.executeQuery();
			if(result1.next()) {
				project = new Project(result1.getInt("ID"),
						result1.getString("title"),
						result1.getString("description"),
						result1.getString("doc_link"),
						result1.getInt("faculty_ID"),
						result1.getString("faculty_username"),
						result1.getString("faculty_name"));
			}
			
		} catch (SQLException e) {
			System.out.println("Exception thrown in the getProjectByGroupId(int) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return project;
	}
	
	public boolean hasGroupBeenAssignedProject(int groupId) {
		if(this.establishConnection() == false) 
			return false;
		
		boolean flag = false;
	
		try {
			String sqlQuery1 = "SELECT COUNT(1)\r\n"
					+ "FROM groupT\r\n"
					+ "WHERE groupT.ID = ?\r\n"
					+ "AND projectID IS NOT NULL;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setInt(1, groupId);
				
			ResultSet result1 = statement1.executeQuery();
			if(result1.next()) {
				if(result1.getInt(1) >= 1) 
					flag = true;
			}
		} catch (SQLException e) {
			System.out.println("Exception thrown in the hasGroupBeenAssignedProject(int) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return flag;
	}
	
	public ArrayList<Mentorship_Request> getMentorshipRequestAgaisnt(int id) {
		if(this.establishConnection() == false)
			return null;
		
		ArrayList<Mentorship_Request> mentorship_Request = null;

		try {
		
			String sqlQeury1 = "SELECT mR.ID, mR.GroupID, mR.Project_ID, mR.Status, p.title AS 'projectTitle', g.name AS 'groupName', g.leader AS 'leaderId',g.student1 AS 'member1Id', g.student2 AS 'member2Id'    \r\n"
					+ "FROM Ment_Req AS mR\r\n"
					+ "INNER JOIN project AS p\r\n"
					+ "ON mR.Project_ID = p.ID\r\n"
					+ "INNER JOIN groupT AS g\r\n"
					+ "ON mR.GroupID = g.ID\r\n"
					+ "INNER JOIN User AS u\r\n"
					+ "ON p.Faculty_ID = u.ID\r\n"
					+ "WHERE mr.Status = 'pending'\r\n"
					+ "AND u.ID = ?;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQeury1);
			statement1.setInt(1, id);
			
			ResultSet result1 = statement1.executeQuery();
			if(result1.next()) {
				mentorship_Request = new ArrayList<Mentorship_Request>();
				Mentorship_Request temp = new Mentorship_Request( result1.getInt("ID"),
						result1.getInt("GroupID"),
						result1.getInt("Project_ID"),
						result1.getString("Status"),
						result1.getString("projectTitle"),
						result1.getString("groupName"),
						result1.getInt("leaderId"),
						result1.getInt("member1Id"),
						result1.getInt("member2Id"));
				mentorship_Request.add(temp);
				
				while(result1.next()) {
					temp = new Mentorship_Request( result1.getInt("ID"),
							result1.getInt("GroupID"),
							result1.getInt("Project_ID"),
							result1.getString("Status"),
							result1.getString("projectTitle"),
							result1.getString("groupName"),
							result1.getInt("leaderId"),
							result1.getInt("member1Id"),
							result1.getInt("member2Id"));
					mentorship_Request.add(temp);					
				}
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown in the getMentorshipRequestAgaisnt(int) method of the DBHandler Class");
			e.printStackTrace();
		}

		this.closeConnection();
		return mentorship_Request;
	}

	public ArrayList<Mentorship_Request> getAllAcceptedMentorshipRequest(int id) {
		if(this.establishConnection() == false)
			return null;
		
		ArrayList<Mentorship_Request> mentorship_Request = null;

		try {
		
			String sqlQeury1 = "SELECT mR.ID, mR.GroupID, mR.Project_ID, mR.Status, p.title AS 'projectTitle', g.name AS 'groupName', g.leader AS 'leaderId',g.student1 AS 'member1Id', g.student2 AS 'member2Id'    \r\n"
					+ "FROM Ment_Req AS mR\r\n"
					+ "INNER JOIN project AS p\r\n"
					+ "ON mR.Project_ID = p.ID\r\n"
					+ "INNER JOIN groupT AS g\r\n"
					+ "ON mR.GroupID = g.ID\r\n"
					+ "INNER JOIN User AS u\r\n"
					+ "ON p.Faculty_ID = u.ID\r\n"
					+ "WHERE mr.Status = 'accepted'\r\n"
					+ "AND u.ID = ?;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQeury1);
			statement1.setInt(1, id);
			
			ResultSet result1 = statement1.executeQuery();
			if(result1.next()) {
				mentorship_Request = new ArrayList<Mentorship_Request>();
				Mentorship_Request temp = new Mentorship_Request( result1.getInt("ID"),
						result1.getInt("GroupID"),
						result1.getInt("Project_ID"),
						result1.getString("Status"),
						result1.getString("projectTitle"),
						result1.getString("groupName"),
						result1.getInt("leaderId"),
						result1.getInt("member1Id"),
						result1.getInt("member2Id"));
				mentorship_Request.add(temp);
				
				while(result1.next()) {
					temp = new Mentorship_Request( result1.getInt("ID"),
							result1.getInt("GroupID"),
							result1.getInt("Project_ID"),
							result1.getString("Status"),
							result1.getString("projectTitle"),
							result1.getString("groupName"),
							result1.getInt("leaderId"),
							result1.getInt("member1Id"),
							result1.getInt("member2Id"));
					mentorship_Request.add(temp);					
				}
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown in the getAllAcceptedMentorshipRequestAgaisnt(int) method of the DBHandler Class");
			e.printStackTrace();
		}

		this.closeConnection();
		return mentorship_Request;		
	}
	
	public ArrayList<Mentorship_Request> getAllDeclinedMentorshipRequest(int id) {
		if(this.establishConnection() == false)
			return null;
		
		ArrayList<Mentorship_Request> mentorship_Request = null;

		try {
		
			String sqlQeury1 = "SELECT mR.ID, mR.GroupID, mR.Project_ID, mR.Status, p.title AS 'projectTitle', g.name AS 'groupName', g.leader AS 'leaderId',g.student1 AS 'member1Id', g.student2 AS 'member2Id'    \r\n"
					+ "FROM Ment_Req AS mR\r\n"
					+ "INNER JOIN project AS p\r\n"
					+ "ON mR.Project_ID = p.ID\r\n"
					+ "INNER JOIN groupT AS g\r\n"
					+ "ON mR.GroupID = g.ID\r\n"
					+ "INNER JOIN User AS u\r\n"
					+ "ON p.Faculty_ID = u.ID\r\n"
					+ "WHERE mr.Status = 'declined'\r\n"
					+ "AND u.ID = ?;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQeury1);
			statement1.setInt(1, id);
			
			ResultSet result1 = statement1.executeQuery();
			if(result1.next()) {
				mentorship_Request = new ArrayList<Mentorship_Request>();
				Mentorship_Request temp = new Mentorship_Request( result1.getInt("ID"),
						result1.getInt("GroupID"),
						result1.getInt("Project_ID"),
						result1.getString("Status"),
						result1.getString("projectTitle"),
						result1.getString("groupName"),
						result1.getInt("leaderId"),
						result1.getInt("member1Id"),
						result1.getInt("member2Id"));
				mentorship_Request.add(temp);
				
				while(result1.next()) {
					temp = new Mentorship_Request( result1.getInt("ID"),
							result1.getInt("GroupID"),
							result1.getInt("Project_ID"),
							result1.getString("Status"),
							result1.getString("projectTitle"),
							result1.getString("groupName"),
							result1.getInt("leaderId"),
							result1.getInt("member1Id"),
							result1.getInt("member2Id"));
					mentorship_Request.add(temp);					
				}
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown in the getAllDeclinedMentorshipRequestAgaisnt(int) method of the DBHandler Class");
			e.printStackTrace();
		}

		this.closeConnection();
		return mentorship_Request;		
	}
	
	
	public boolean acceptMentorshipRequest(int mentorshipID) {
		if(this.establishConnection() == false)
			return false;
		
		boolean requestAccepted = false;

		try {
			
			String sqlQuery1 = "UPDATE Ment_Req\r\n"
					+ "SET Status = 'accepted'\r\n"
					+ "WHERE ID = ?;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setInt(1, mentorshipID);
			
			if(statement1.executeUpdate() > 0)
				requestAccepted = true;
		
		} catch (SQLException e) {
			System.out.println("Exception thrown in the acceptMentorshipRequest(int) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return requestAccepted;
	}
	
	public boolean declineMentorshipRequest(int mentorshipID) {
		if(this.establishConnection() == false)
			return false;
		
		boolean requestDeclined = false;

		try {
			
			String sqlQuery1 = "UPDATE Ment_Req\r\n"
					+ "SET Status = 'declined'\r\n"
					+ "WHERE ID = ?;";
			PreparedStatement statement1 = this.connection.prepareStatement(sqlQuery1);
			statement1.setInt(1, mentorshipID);
			
			if(statement1.executeUpdate() > 0)
				requestDeclined = true;
		
		} catch (SQLException e) {
			System.out.println("Exception thrown in the declineMentorshipRequest(int) method of the DBHandler Class");
			e.printStackTrace();
		}
		
		this.closeConnection();
		return requestDeclined;		
	}
	
}
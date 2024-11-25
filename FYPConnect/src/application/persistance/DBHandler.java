package application.persistance;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.datamodel.User;
import application.services.*;

// setup mysql with respect to eclipse then uncomment the following code
public class DBHandler {
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
	
<<<<<<< Updated upstream
=======
	// Get group members for a specific group
    public ResultSet getGroupMembers(String groupName) {
        this.establishConnection();
        String query = """
            SELECT u.name
            FROM GroupT g
            INNER JOIN User u ON u.ID = g.leader
            WHERE g.name = ?;
        """;
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, groupName);
            return statement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching group members for group: " + groupName);
            e.printStackTrace();
        }
        return null;
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
		        statement.setString(3, docLink.isEmpty() ? null : docLink); // Allow null for empty links
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
>>>>>>> Stashed changes
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

	
	
	
}

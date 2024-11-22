package application.datamodel;


public abstract class User {
	private int id;
	private String name;
	private String password;
	private String username;	// will be roll num incase of student and username incase of admin/faculty
	private String email;
	
 	public User() {
		this.id = -1;
		this.name = "\0";
		this.password = "\0";
		this.username = "\0";
		this.email = "\0";
	}
	public User(int id, String name, String password, String username, String email) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.username = username;
		this.email = email;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password + ", username=" + username + "email=" + email + "]";
	}
	
	// basic solution to display cgpa in table view 
	public double getCgpa() {
		return 0.0;
	}
}

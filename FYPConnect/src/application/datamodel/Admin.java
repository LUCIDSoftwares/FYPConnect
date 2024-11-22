package application.datamodel;

public class Admin extends User {

	public Admin() {
		super();
	}
	public Admin(int id, String name, String password, String username, String email) {
		super(id, name, password, username, email);
	}
	public Admin(String name, String username, String password, String email) {
		super(-1, name, password, username, email);
	}

	@Override
	public String toString() {
		return "Admin [getId()=" + getId() + ", getName()=" + getName() + ", getPassword()=" + getPassword()
				+ ", getUsername()=" + getUsername() + ", getEmail()=" + getEmail() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}

}

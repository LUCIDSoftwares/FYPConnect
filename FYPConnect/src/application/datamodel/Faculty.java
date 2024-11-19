package application.datamodel;

public class Faculty extends User {
	
	public Faculty() {
		super();
	}
	public Faculty(int id, String name, String password, String username, String email) {
		super(id, name, password, username, email);
	}

	@Override
	public String toString() {
		return "Faculty [getId()=" + getId() + ", getName()=" + getName() + ", getPassword()=" + getPassword()
				+ ", getUsername()=" + getUsername() + ", getEmail()=" + getEmail() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}

}



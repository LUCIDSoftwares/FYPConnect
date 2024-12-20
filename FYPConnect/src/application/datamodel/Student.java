package application.datamodel;

public class Student extends User {
	private double cgpa;
	
	public Student() {
		super();
		this.cgpa = -1;
	}
	
	public Student(double cgpa) {
		super();
		this.cgpa = cgpa;
	}
	public Student(int id, String name, String password, String username, String email, double cgpa) {
		super(id, name, password, username, email);
		this.cgpa = cgpa;
	}
	public Student(String name, String username, String password, String email, double cgpa) {
		super(-1, name, password, username, email);
		this.cgpa = cgpa;
	}
	
	@Override
	public double getCgpa() {
		return cgpa;
	}
	public void setCgpa(double cgpa) {
		this.cgpa = cgpa;
	}

	@Override
	public String toString() {
		return "Student [cgpa=" + cgpa + ", getId()=" + getId() + ", getName()=" + getName() + ", getPassword()="
				+ getPassword() + ", getUsername()=" + getUsername() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
	
}

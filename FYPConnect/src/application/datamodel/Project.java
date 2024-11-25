package application.datamodel;

public class Project {
	private int id;
	private String title;
	private String description;
	private String doc_link;
	private int faculty_id;
	private String faculty_username;
	private String faculty_name;
	
	public Project() {
		super();
	}
	public Project(int id, String title, String description, String doc_link, int faculty_id, String faculty_username,
			String faculty_name) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.doc_link = doc_link;
		this.faculty_id = faculty_id;
		this.faculty_username = faculty_username;
		this.faculty_name = faculty_name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDoc_link() {
		return doc_link;
	}
	public void setDoc_link(String doc_link) {
		this.doc_link = doc_link;
	}
	public int getFaculty_id() {
		return faculty_id;
	}
	public void setFaculty_id(int faculty_id) {
		this.faculty_id = faculty_id;
	}
	public String getFaculty_username() {
		return faculty_username;
	}
	public void setFaculty_username(String faculty_username) {
		this.faculty_username = faculty_username;
	}
	public String getFaculty_name() {
		return faculty_name;
	}
	public void setFaculty_name(String faculty_name) {
		this.faculty_name = faculty_name;
	}
	
	@Override
	public String toString() {
		return "Project [id=" + id + ", title=" + title + ", description=" + description + ", doc_link=" + doc_link
				+ ", faculty_id=" + faculty_id + ", faculty_username=" + faculty_username + ", faculty_name="
				+ faculty_name + "]";
	}

}

package application.datamodel;

public class Mentorship_Request {

	private int ID;
	private int GroupID;
	private int Project_ID;
	private String status;
	private String projectTitle;
	private String groupName;
	private int leaderId;
	private int member1Id;
	private int member2Id;
	
	
	public Mentorship_Request() {
		super();
	}
	public Mentorship_Request(int iD, int groupID, int project_ID, String status, String projectTitle, String groupName,
			int leaderName, int member1Username, int member2Username) {
		super();
		ID = iD;
		GroupID = groupID;
		Project_ID = project_ID;
		this.status = status;
		this.projectTitle = projectTitle;
		this.groupName = groupName;
		this.leaderId = leaderName;
		this.member1Id = member1Username;
		this.member2Id = member2Username;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getGroupID() {
		return GroupID;
	}
	public void setGroupID(int groupID) {
		GroupID = groupID;
	}
	public int getProject_ID() {
		return Project_ID;
	}
	public void setProject_ID(int project_ID) {
		Project_ID = project_ID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProjectTitle() {
		return projectTitle;
	}
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(int leaderName) {
		this.leaderId = leaderName;
	}
	public int getMember1Id() {
		return member1Id;
	}
	public void setMember1Id(int member1Username) {
		this.member1Id= member1Username;
	}
	public int getMember2Id() {
		return member2Id;
	}
	public void setMember2Id(int member2Username) {
		this.member2Id = member2Username;
	}
	
	@Override
	public String toString() {
		return "Mentorship_Request [ID=" + ID + ", GroupID=" + GroupID + ", Project_ID=" + Project_ID + ", status="
				+ status + ", projectTitle=" + projectTitle + ", groupName=" + groupName + ", leaderId=" + leaderId
				+ ", member1Id=" + member1Id + ", member2Id=" + member2Id + "]";
	}
}

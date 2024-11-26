package application.services;

import java.util.ArrayList;

import application.datamodel.Mentorship_Request;
import application.datamodel.Project;
import application.datamodel.User;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;
import application.session.UserSession;

public class ProjectHandler {
	private PersistanceHandler dbHandler;
	
	public ProjectHandler() {
		this.dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
	}
	
	
	public ArrayList<Project> getAllAvailableProjects() {
		
		return this.dbHandler.getAllAvailableProjects();
	}
	
	public ArrayList<Project> getAllAvailableProjects(String projectTitle) {
		User user = UserSession.getInstance().getCurrentUser();
		int groupId = this.dbHandler.getGroupId(user.getId());
		
		return this.dbHandler.getAllAvailableProjects(projectTitle, groupId);
	}	
	
	public void createMentorshipRequest(int projectId) {
		User user = UserSession.getInstance().getCurrentUser();
		int groupId = this.dbHandler.getGroupId(user.getId());
		
		this.dbHandler.createMentorshipRequest(projectId, groupId);
	}
	
	public ArrayList<Mentorship_Request> getAllPendingMentorshipRequest() {
		
		return this.dbHandler.getMentorshipRequestAgaisnt(UserSession.getInstance().getCurrentUser().getId());
	}
	
	public ArrayList<Mentorship_Request> getAllAcceptedMentorshipRequest() {
		
		return this.dbHandler.getAllAcceptedMentorshipRequest(UserSession.getInstance().getCurrentUser().getId());
	}
	
	public ArrayList<Mentorship_Request> getAllDeclinedMentorshipRequest() {
		
		return this.dbHandler.getAllDeclinedMentorshipRequest(UserSession.getInstance().getCurrentUser().getId());
	}
	
	public boolean acceptMentorshipRequest(int mentorshipId) {
		
		return this.dbHandler.acceptMentorshipRequest(mentorshipId);
	}
	
	public boolean declineMentorshipRequest(int mentorshipId) {
		
		return this.dbHandler.declineMentorshipRequest(mentorshipId);
	}
	
	public ArrayList<Project> getAllProjectsWithMentorshipRequest(String projectTitle) {
		User user = UserSession.getInstance().getCurrentUser();
		int groupId = this.dbHandler.getGroupId(user.getId());
		
		return this.dbHandler.getAllProjectsWithMentorshipRequest(projectTitle, groupId);		
	}
	
	public ArrayList<Project> getAllProjectsWithDeclinedRequests(String projectTitle) {
		User user = UserSession.getInstance().getCurrentUser();
		int groupId = this.dbHandler.getGroupId(user.getId());
		
		return this.dbHandler.getAllProjectsWithDeclinedRequests(projectTitle, groupId);		
	}
	
	public Project getProjectInformation() {
		User user = UserSession.getInstance().getCurrentUser();
		int groupId = this.dbHandler.getGroupId(user.getId());
		
		return this.dbHandler.getProjectByGroupId(groupId);
	}

}

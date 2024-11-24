package application.services;

import java.util.ArrayList;

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

}

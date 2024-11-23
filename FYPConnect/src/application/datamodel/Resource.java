package application.datamodel;

public class Resource {
	private int resourceId;
	private String title;
	private String description;
	private String uploaderUsername;
	private String filePath;
	
	public Resource() {
		super();
	}
	public Resource(int resourceId, String title, String description, String uploaderUsername, String filePath) {
		super();
		this.resourceId = resourceId;
		this.title = title;
		this.description = description;
		this.uploaderUsername = uploaderUsername;
		this.filePath = filePath;
	}
	
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
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
	public String getUploaderUsername() {
		return uploaderUsername;
	}
	public void setUploaderId(String uploaderUsername) {
		this.uploaderUsername = uploaderUsername;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public String toString() {
		return "Resource [resourceId=" + resourceId + ", title=" + title + ", description=" + description
				+ ", uploaderUsername=" + uploaderUsername + ", filePath=" + filePath + "]";
	}

}

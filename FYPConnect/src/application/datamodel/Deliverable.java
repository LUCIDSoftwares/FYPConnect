package application.datamodel;

import java.time.LocalDateTime;

public class Deliverable {
    private int deliverableId;
    private LocalDateTime deadline;
    private String description;
    private String docLink;
    private String facultyId;

    // Default constructor
    public Deliverable() {
        super();
    }

    // Parameterized constructor
    public Deliverable(int deliverableId, LocalDateTime deadline, String description, String docLink, String facultyId) {
        super();
        this.deliverableId = deliverableId;
        this.deadline = deadline;
        this.description = description;
        this.docLink = docLink;
        this.facultyId = facultyId;
    }

    // Getters and Setters
    public int getDeliverableId() {
        return deliverableId;
    }

    public void setDeliverableId(int deliverableId) {
        this.deliverableId = deliverableId;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocLink() {
        return docLink;
    }

    public void setDocLink(String docLink) {
        this.docLink = docLink;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    @Override
    public String toString() {
        return "Deliverable [deliverableId=" + deliverableId + ", deadline=" + deadline + ", description=" + description
                + ", docLink=" + docLink + ", facultyId=" + facultyId + "]";
    }
}

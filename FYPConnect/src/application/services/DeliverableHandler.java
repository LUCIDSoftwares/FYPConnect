package application.services;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import application.datamodel.Deliverable;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;

// Functionality related to managing deliverables
public class DeliverableHandler {
    private PersistanceHandler dbHandler;

    public DeliverableHandler() {
        this.dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");
    }

    // Returns all deliverables from the database
    public ArrayList<Deliverable> getAllDeliverables() {
        ArrayList<Deliverable> deliverableArrayList = this.dbHandler.getAllDeliverables();
        return deliverableArrayList;
    }
    
	public String downloadResource(String fileTitle, String filePath) {
		//String fileTitle = "My Thesis";
		//String filePath = "C:/Users/HP/Downloads/22I-0789_AdmitCard-1.pdf/";

		// extract the extension from the give file path
		String ext = filePath.substring(filePath.lastIndexOf("."), filePath.length());
		File originalFile = new File(filePath);
		
		//System.out.println("C:/Users/HP/Downloads/22I-0789_AdmitCard-1.pdf/");
		//File newFile = new File("C:/Users/HP/Downloads/Check2.pdf");
		
		// use date to always assign a unique name to a file that is to be downloaded
		// downloaded files will appear in the downloads folder with the 
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy-hhmmss.SSS");
		String formattedDate = sdf.format(date);
		// remove all space from the file title using regex
		fileTitle = fileTitle.replaceAll("\\s+","");
		
		String systemdownloads = System.getProperty("user.home");
		// replace the '\' character with '/' character
		systemdownloads = systemdownloads.replace("\\", "/");
			
		
		// assign the new path where the downloaded file is to appear with its name
		String newFilePath = systemdownloads + "/Downloads/" + fileTitle + formattedDate + ext;
		File newFile = new File(newFilePath);
		
		// now copying the file
		try {
			Files.copy(originalFile.toPath(), newFile.toPath());
			System.out.println("hogaya kaam");
			return newFilePath;
		}
		catch (Exception e) {
			System.out.println("Lo gi program tou warr gaya");
			System.out.println("Exception thrown in the downloadResource(String) method of the Resource Handler Class ");
			return null;
		}
	}
//
//    // Returns deliverables filtered by a specific faculty ID
//    public ArrayList<Deliverable> getDeliverablesByFacultyId(int facultyId) {
//        ArrayList<Deliverable> deliverableArrayList = this.dbHandler.getDeliverablesByFacultyId(facultyId);
//        return deliverableArrayList;
//    }

//    // Adds a new deliverable to the database
//    public boolean addDeliverable(Deliverable deliverable) {
//        return this.dbHandler.addDeliverable(deliverable);
//    }
//
//    // Deletes a deliverable by ID
//    public boolean deleteDeliverable(int deliverableId) {
//        return this.dbHandler.deleteDeliverable(deliverableId);
//    }
//
//    // Updates an existing deliverable
//    public boolean updateDeliverable(Deliverable deliverable) {
//        return this.dbHandler.updateDeliverable(deliverable);
//    }
//
//    // Provides details of a specific deliverable by ID
//    public Deliverable getDeliverableById(int deliverableId) {
//        return this.dbHandler.getDeliverableById(deliverableId);
//    }
}

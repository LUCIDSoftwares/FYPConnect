package application.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
//import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import application.datamodel.Resource;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;
import application.session.UserSession;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

// functionality related to uploading and downloading resources
public class ResourceHandler {
	PersistanceHandler dbHandler;
	
	public ResourceHandler() {
		this.dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");	
	}
	
	// return all the resources present in the system
	public ArrayList<Resource> getAllResources() {
		ArrayList<Resource> resourceArrayList = this.dbHandler.getAllResources();
		
		return resourceArrayList;
	}
	

	
	public ArrayList<Resource> getResourcesByTitle(String title) {
		ArrayList<Resource> resourceArrayList = this.dbHandler.getResourcesByTitle(title);
		
		return resourceArrayList;
	}
	
	public String downloadResource(String fileTitle, String filePath) {
		//String fileTitle = "My Thesis";

		// extract the extension from the give file path
		String ext = filePath.substring(filePath.lastIndexOf("."), filePath.length());
		File originalFile = new File(filePath);
		
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
	
	public boolean handleSubmit(String title, String description, File selectedFile) {
		
        // generate a unique filename using a time stamp
        String originalFileName = selectedFile.getName();
        // extract the extension
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // extract the file name excluding the extension
        originalFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        originalFileName = originalFileName.replaceAll("\\s","");
        // get time stamp in the given format
        String timestamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        String newFileName = originalFileName + timestamp + extension;

		// Define the path to copy the file to
        String resourcesPath = "/resources";
        File resourcesDir = new File(resourcesPath );
        if (!resourcesDir.exists()) {
            resourcesDir.mkdir(); 		// Create resources folder if it doesn't exist
        }
        File destinationFile = new File(resourcesDir, newFileName);
        
        String uploaderUsername = UserSession.getInstance().getCurrentUser().getUsername();
        
        try {
            // copy the file to the resources folder
            java.nio.file.Files.copy(selectedFile.toPath(), destinationFile.toPath());
            System.out.println("File copied successfully to: " + destinationFile.getAbsolutePath());

            // save the resource details to the database through the PersistenceHandler
            String filePath = resourcesPath + "/" + newFileName;
            //Date currentTimestamp = new Date();
            
            //boolean isSaved = dbHandler.saveSubmission(delId, this.groupId, currentTimestamp, fileLink);
            boolean isUploaded = dbHandler.saveResource(title, description, filePath, uploaderUsername);
            
            if (isUploaded) {
            	
            	return true;
            	
//                Alert alert = new Alert(AlertType.INFORMATION);
//                alert.setTitle("Submission Successful");
//                alert.setHeaderText(null);
//                alert.setContentText("File uploaded and resource recorded successfully.");
//                alert.showAndWait();
            } else {
            	
            	return false;
            	
//                Alert alert = new Alert(AlertType.ERROR);
//                alert.setTitle("Database Error");
//                alert.setHeaderText(null);
//                alert.setContentText("Failed to save resource details to the database.");
//                alert.showAndWait();
            }

        } catch (IOException e) {	
        	System.out.println("Exception in resource handler bara wala code");
        	return false;
//        	e.printStackTrace();
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setTitle("File Upload Error");
//            alert.setHeaderText(null);
//            alert.setContentText("Failed to copy the file. Please try again.");
//            alert.showAndWait();
            
        }
        //return false;
	}
	
}
//		String ext = filePath.substring(filePath.lastIndexOf("."), filePath.length());
//		File originalFile = new File(filePath);
//		String newFilePath = "Downloads//downloadedFile" + ext;
//		
//		File newFile = new File(newFilePath);
//
//		// now try to download it in the downloads folder
//		try {
//			Files.copy(originalFile.toPath(), newFile.toPath());
//			System.out.println("hogaya kaam");
//		}
//		catch (Exception e) {
//			newFilePath = null;
//			System.out.println("Exception thrown in the downloadResource(String) method of the ResourceHandler Class");
//			
//		}
		//return null;
//	}
	
	
//	String url;
//	
//	
//	public ResourceHandler(String url) {
//		this.url = url;
//	}
//	
//	public ResourceHandler() {
//		this.url = null;
//	}
//
//	@Override
//	protected Void call() throws Exception {
//		
//		File originalFile = new File("E://OneDrive - FAST National University//Semester 5//CS CS3004-Software Design and Analysis//Project//FYPConnect//Other Deliverable files//SD_SSD_DM_CD.pdf");
//		File newFile = new File("Check.pdf");
//		
//		try {
//			Files.copy(originalFile.toPath(), newFile.toPath());
//			System.out.println("hogaya kaam");
//		}
//		catch (Exception e) {
//			System.out.println("Lo gi program tou warr gaya");
//		}
//		
//		
////		String ext = url.substring(url.lastIndexOf("."), url.length());
////		
////		URI uri = Paths.get( "E://OneDrive - FAST National University//Semester 5//CS CS3004-Software Design and Analysis//Project//FYPConnect//Other Deliverable files//SD_SSD_DM_CD.pdf" ).toUri();
////		
////		try (InputStream is = uri.toURL().openStream()) {
////			Files.copy(is, Paths.get("downloadedFile.pdf"));
////		}
//		
//		return null;
//	}
//	
//	"C:\Users\HP\Downloads\summary_updated.docx"
//	
//	
//	@Override
//	public void failed() {
//		System.out.println("Download failed");
//	}
//	
//	@Override
//	public void succeeded() {
//		System.out.println("Download Completed");
//	}
//}

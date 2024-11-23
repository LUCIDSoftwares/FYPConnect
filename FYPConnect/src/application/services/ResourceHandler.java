package application.services;

import java.io.File;
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
import javafx.concurrent.Task;

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
	}
	
	
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
}

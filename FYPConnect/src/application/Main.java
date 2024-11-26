package application;
	
//import application.services.Resource;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
//import javafx.scene.layout.BorderPane;
//import java file
import java.io.File;

import application.datamodel.Admin;
import application.datamodel.Faculty;
import application.datamodel.Student;
import application.datamodel.User;
import application.persistance.ConcretePersistanceFactory;
import application.persistance.PersistanceHandler;
import application.session.UserSession;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//read username from /application/session/lastSession.log
		String username = "";
		try {
			File file = new File("src/application/session/lastSession.log");
			if (file.exists()) {
				java.util.Scanner myReader = new java.util.Scanner(file);
				while (myReader.hasNextLine()) {
					username = myReader.nextLine();
				}
				myReader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try {
			
			Parent root;
			if (username == "") {
				root = FXMLLoader.load(getClass().getResource("/resources/views/loginScreen.fxml"));
			} else {
				PersistanceHandler dbHandler = ConcretePersistanceFactory.getInstance().createPersistanceHandler("DBHandler");

				User user = dbHandler.retrieveUser(username);
				
				
				UserSession.getInstance().setCurrentUser(user);
				
				if (user instanceof Admin) {
                    root = FXMLLoader.load(getClass().getResource("/resources/views/adminScreens/AdminDashboardScreen.fxml"));
                } else if (user instanceof Student) {
				root = FXMLLoader.load(getClass().getResource("/resources/views/studentDashboardScreen.fxml"));
			} else if (user instanceof Faculty) {
				root = FXMLLoader.load(getClass().getResource("/resources/views/facultyHomeScreen.fxml"));
			}
              else{
                    root = FXMLLoader.load(getClass().getResource("/resources/views/loginScreen.fxml"));
                }
			}
			Scene scene = new Scene(root);
			primaryStage.setTitle("FYPConnect by Lucid Softwares");
			Image icon = new Image(getClass().getResourceAsStream("/fypconnectlogo.png"));
			primaryStage.getIcons().add(icon);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
				
//			Task<Void> task = new Resource();
//			Thread thread = new Thread(task);
//			thread.setDaemon(true);
//			thread.start();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

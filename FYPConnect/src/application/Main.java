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


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("/resources/views/loginScreen.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("FYPConnect by Lucid Softwares");
			Image icon = new Image(getClass().getResourceAsStream("/fypconnectlogo.png"));
			primaryStage.getIcons().add(icon);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
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

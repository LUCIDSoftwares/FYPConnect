module FYPConnect {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	opens application.controller to javafx.graphics, javafx.fxml;
	opens application.datamodel to javafx.base;
	opens application.controller.adminScreens to javafx.graphics, javafx.fxml;
	opens application.controller.studentViews to javafx.graphics, javafx.fxml;

	

}

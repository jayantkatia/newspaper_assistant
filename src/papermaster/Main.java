package papermaster;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	public void start(Stage primaryStage) {
		try {
			Parent root=FXMLLoader.load(getClass().getResource("view.fxml"));
			Scene scene=new Scene(root,600,400);
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("Paper Master");
			primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

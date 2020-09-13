package hawkerscontrolpanel;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root=FXMLLoader.load(getClass().getResource("view.fxml"));
			Scene scene=new Scene(root,800,720);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Hawkers Panel");	
			primaryStage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}

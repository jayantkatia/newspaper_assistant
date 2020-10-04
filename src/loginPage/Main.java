package loginPage;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	@Override
	public void start(Stage primaryStage) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("view.fxml"));
			Scene scene=new Scene(root,700,700);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Newspaper Distributor Assistant");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
}

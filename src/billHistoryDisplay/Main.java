package billHistoryDisplay;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application{
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root=FXMLLoader.load(getClass().getResource("view.fxml"));
			Scene scene=new Scene(root,810,900);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Bill History");
			primaryStage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}

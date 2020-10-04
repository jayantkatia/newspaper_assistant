package loginPage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class viewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView imgLogo;
    
    @FXML
    private Label labelTxt;
    
    @FXML
    void initialize() {
    	
    	labelTxt.setOpacity(0);
    	FadeTransition fadeIn = new FadeTransition(Duration.seconds(2),imgLogo);
    	fadeIn.setFromValue(0);
    	fadeIn.setToValue(1);
    	fadeIn.play();

    	fadeIn.setOnFinished((e)->{
    		
    		TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(1));
    		translateTransition.setNode(imgLogo);  		
    		translateTransition.setByX(-200);
    		translateTransition.play();

    		translateTransition.setOnFinished((j)->{
    			FadeTransition fadeInText = new FadeTransition(Duration.seconds(2),labelTxt);
    			fadeInText.setFromValue(0);
        		fadeInText.setToValue(1);
        		fadeInText.play();
        		
        		fadeIn.setOnFinished((k)->{
        			
        		});
    		});
    	});
    }
}

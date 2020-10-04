package billHistoryDisplay;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class viewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> comboMobile;

    @FXML
    private TableView<UserBean> table;

    @FXML
    void doMobile(ActionEvent event) {
    	selectedMobile=comboMobile.getSelectionModel().getSelectedItem();
    	doFill();
    	
    }
    void doFill() {
    	if(selectedMobile.equals("All")) ;
//    	{
//    		;
//    	}else if{
//    		;
//    	}
    };
    
    
    private DatabaseServices services;
    private String selectedMobile;
    private ObservableList<UserBean> data;
    @FXML
    void initialize() {
    	services=new DatabaseServices();
    	comboMobile.getItems().add("All");
    	comboMobile.getSelectionModel().select("All");
    	selectedMobile=comboMobile.getSelectionModel().getSelectedItem();
    	
    	doFill();
    }
}

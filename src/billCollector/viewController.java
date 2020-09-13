package billCollector;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class viewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text helpMobile;

    @FXML
    private ComboBox<String> comboMobile;

    @FXML
    private ListView<String> list;

    @FXML
    private ListView<Float> listPrices;

    @FXML
    private TextField txtBill;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnNew;

    private  DatabaseServices services;
    @FXML
    void initialize() {
      services=new DatabaseServices();
      comboMobile.getItems().addAll(services.fetchMobileNumbers());
      
      list.setFocusTraversable(false);
      listPrices.setFocusTraversable(false);
    }
    
    private String mobileNumber;
    @FXML
    void doMobile(ActionEvent event) {
    	list.getItems().clear();
		listPrices.getItems().clear();
		txtBill.setText("0.0");
		helpMobile.setText("*");
		
    	mobileNumber=comboMobile.getSelectionModel().getSelectedItem();
    	ArrayList<ModelBillCollector> billsList=services.fetch(mobileNumber);
    	if(billsList.size()!=0) 
    	{
    		Float bill=0.0f;
    		
    		for(int i=0;i<billsList.size();i++) {
    			list.getItems().add(billsList.get(i).dobilling + " @ "+ billsList.get(i).days);
    			listPrices.getItems().add(billsList.get(i).amount);
    			bill=bill+billsList.get(i).amount;
    		}
    		txtBill.setText(bill.toString());
    		btnSave.setDisable(false);
    	}else {
//    		
    		helpMobile.setText("No pending records with entered mobile number");
    		btnSave.setDisable(true);
    	}
    
    }

    @FXML
    void doNew(ActionEvent event) {
    	clearFields();
    }

    @FXML
    void doSave(ActionEvent event) {
    	if(isPrimaryKeyValidated()) {
    		services.save(mobileNumber);
    		comboMobile.getItems().remove(mobileNumber);
    		clearFields();
    	}
    }

    
   boolean isPrimaryKeyValidated() {
	   if(mobileNumber==null) {
		   helpMobile.setText("Please Enter Mobile Number");
		   return false;
	   }
	   return true;
   } 
   void clearFields() {
	   comboMobile.getSelectionModel().clearSelection();
	   comboMobile.getEditor().clear();
	   
	   txtBill.setText("0.0");
	   list.getItems().clear();
	   listPrices.getItems().clear();
	   
	   helpMobile.setText("*");
	   mobileNumber=null;
	   btnSave.setDisable(false);
   }
}

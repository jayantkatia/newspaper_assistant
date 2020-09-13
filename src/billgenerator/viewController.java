package billgenerator;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import customersmanager.ModelCustomers;
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
    private ListView<String> listPapers;

    @FXML
    private ListView<Float> listPrices;

    @FXML
    private TextField txtBill;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnNew;

    @FXML
    private Text txtArea;

    @FXML
    private Text txtName;

    @FXML
    private Text txtAddress;

    @FXML
    private Text txtFrom;

    @FXML
    private Text txtHawker;

    @FXML
    private Text txtTo;

    @FXML
    private Text txtDays;

    
    private DatabaseServices services;
    @FXML
    void initialize() {
    	services=new DatabaseServices();
    	services.fetchPapersAndPrices();
    	comboMobile.getItems().addAll(services.fetchMobileNumbersList());
    	
    	listPapers.setFocusTraversable(false);
    	listPrices.setFocusTraversable(false);
    	btnSave.setDisable(true);
    }
    void fillCombosAndList() {
    	
    }
    
    private ModelBill billObject;
    private String customerMobile;
    @FXML
    void doMobile(ActionEvent event) {
    	
    	clearRestFields();
    	
    	customerMobile=comboMobile.getSelectionModel().getSelectedItem();
    	ModelCustomers data = services.fetchData(customerMobile);
    	if(data != null) {
    		System.out.println("Data fetched .....Previous record exists");
    		txtAddress.setText(data.address);
    		txtName.setText(data.name);
    		txtArea.setText(data.area);
    		txtHawker.setText(data.hawkerName);
//////////////////////------------------
    		Date billStartMorning = Date.valueOf(data.dos.toLocalDate().atStartOfDay().toLocalDate());
        	Date todayEvening = Date.valueOf(LocalDate.now().plusDays(1).atStartOfDay().toLocalDate());
    		Long days= (todayEvening.getTime()-billStartMorning.getTime())/86400000;
    		System.out.println(days);
    		
    		
    		DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy");
    		txtFrom.setText(billStartMorning.toLocalDate().format(format));
    		txtTo.setText(LocalDate.now().format(format));
    		txtDays.setText(days.toString());
////////////////////---------------------	
    		listPapers.getItems().clear();
    		listPrices.getItems().clear();
    		
    		String[] usersSubscriptions=data.papers.split(",");
    		Float bill=0.0f;
    		for(int i=0;i<services.papersList.size();i++) {
    			for(int j=0;j<usersSubscriptions.length;j++) {
    				if(usersSubscriptions[j].equals(services.papersList.get(i))){
    					listPapers.getItems().add(usersSubscriptions[j] + " @ "+services.pricesList.get(i).toString());
    					listPrices.getItems().add(services.pricesList.get(i)*days);
    					bill=bill+(services.pricesList.get(i)*days);
    				}
    			}
    		}
     		txtBill.setText(bill.toString());
     		//////////////////------------
     			billObject=new ModelBill();
     	    	billObject.customerMobile=customerMobile;
     	    	billObject.days=days.intValue();
     	    	billObject.amount=bill;
     	    	billObject.dobilling=Date.valueOf(LocalDate.now());
     	    
    		btnSave.setDisable(false);
    	}else {
    		billObject=null;
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
    		services.saveBill(billObject);
    		clearFields();
    	}
    }

    

    
    boolean isPrimaryKeyValidated() {
    	if(customerMobile==null) {
    		helpMobile.setText("Please select/enter mobile number to generate bill");
    		return false;
    	}else if(billObject ==null) {
    		helpMobile.setText("This mobile number is not registered");
    		return false;
    	}
    	return true;
    }
    void clearRestFields()
    {
    	txtName.setText(" ");
    	txtAddress.setText(" ");
    	txtArea.setText(" ");
    	txtDays.setText(" ");
    	txtFrom.setText(" ");
    	txtHawker.setText(" ");
    	txtBill.setText("0.0");
    	txtTo.setText(" ");
    	listPapers.getItems().clear();
		listPrices.getItems().clear();
    }   
    void clearFields() {
     	//clearing fields
    	clearRestFields();
    	
    	comboMobile.getEditor().clear();
    	comboMobile.getSelectionModel().clearSelection();

    	//initial state of fields
    	customerMobile=null;
    	billObject=null;
		
    	//removing error messages
    	helpMobile.setText("*");
    	
    	btnSave.setDisable(true);

    }
}

package customersmanager;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class viewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtAddress;

    @FXML
    private ListView<String> listPapers;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Text helpName;

    @FXML
    private Text helpMobile;

    @FXML
    private Text helpAddress;

    @FXML
    private Text helpAreas;
   
    @FXML
    private Text helpHawkers;
    
    @FXML
    private Text helpPapers;

    @FXML
    private DatePicker txtDate;

    @FXML
    private ComboBox<String> comboMobile;

    @FXML
    private ComboBox<String> comboArea;

    @FXML
    private TextField txtName;

    @FXML
    private ComboBox<String> comboHawker;

   

    @FXML
    private ListView<Float> listPrices;

    @FXML
    private TextField txtBill;

    @FXML
    private ProgressIndicator progressIndicator;
////////////////////////////////////////////////////////
    private DatabaseServices services;
    @FXML
    void initialize() {
    		services=new DatabaseServices();
    		
    		listPrices.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    		listPapers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    		listPrices.setFocusTraversable(false);
    		
    		comboMobile.getItems().addAll(services.fetchMobileNumbersList());
    		comboArea.getItems().addAll(services.fetchAreas());
    		
    		services.fetchPapersAndPrices();
    		listPapers.getItems().addAll(services.papersList);
    		listPrices.getItems().addAll(services.pricesList);
    		txtDate.setValue(LocalDate.now());
    }
   
    @FXML
    void doArea(ActionEvent event) {
    	comboHawker.getItems().clear();
    	comboHawker.getItems().addAll(services.fetchHawkers(comboArea.getSelectionModel().getSelectedItem()));
    }

    @FXML
    void doPaper(MouseEvent event) {
    	listPrices.getSelectionModel().clearSelection();
    	ObservableList<Integer> arr= listPapers.getSelectionModel().getSelectedIndices();
    	Float sum=0.0f;
    	for(int i:arr) {
			listPrices.getSelectionModel().select(i);
			sum=sum+services.pricesList.get(i);
		}
    	txtBill.setText(sum.toString());
    }

   
    
    private String mobileNumber;
    @FXML
    void doMobile(ActionEvent event) {
    	mobileNumber=comboMobile.getSelectionModel().getSelectedItem();
    	ModelCustomers data=services.fetchData(mobileNumber);
    	if(data!=null) {
    		txtAddress.setText(data.address);
    		System.out.println(data.dos.toLocalDate());
    		txtDate.setValue(data.dos.toLocalDate());
    		txtName.setText(data.name);
    		comboArea.getSelectionModel().select(data.area);
    		comboHawker.getSelectionModel().select(data.hawkerName);
    		
    		
    		String[] papers=data.papers.split(",");
    		
    		listPapers.getSelectionModel().clearSelection();
    		listPrices.getSelectionModel().clearSelection();
    		Float sum=0.0f;
    		for(int i=0;i < services.papersList.size();i++) {
    			for(int j=0; j < papers.length;j++) {
    				if(papers[j].equals(services.papersList.get(i))){
    					
    					listPapers.getSelectionModel().select(i);
    					listPrices.getSelectionModel().select(i);
    	    			sum=sum+services.pricesList.get(i);
    				
    				}
    			}
    		}
        	txtBill.setText(sum.toString());
        	
        	
        	
    		
    		btnSave.setDisable(true);
    		btnUpdate.setDisable(false);
    		btnDelete.setDisable(false);
    	}else {
    		btnSave.setDisable(false);
    		btnUpdate.setDisable(true);
    		btnDelete.setDisable(true);
    	}
    	helpMobile.setText("*");
    }
//////////////////////////////////////////////////////
    @FXML
    void doNew(ActionEvent event) {
    	clearFields();
    }

    @FXML
    void doSave(ActionEvent event) {
    	if(isValidated()) {
    		services.save(getModelObject());
    		comboMobile.getItems().add(mobileNumber);
    		clearFields();
    	}
    }

    @FXML
    void doUpdate(ActionEvent event) {
    	if(isValidated()) {
    		services.update(getModelObject());
    		System.out.println("This is");
    		clearFields();
    	}
    }
    
    ModelCustomers getModelObject() {
    	ModelCustomers data=new ModelCustomers();
    	data.name=txtName.getText();
    	data.dos=Date.valueOf(txtDate.getValue());
    	data.address=txtAddress.getText();
    	data.hawkerName=comboHawker.getSelectionModel().getSelectedItem();
    	data.area=comboArea.getSelectionModel().getSelectedItem();
    	data.mobileNumber=mobileNumber;
    	
    	ObservableList<Integer> papersList=listPapers.getSelectionModel().getSelectedIndices();
    	String papers = "";
    	for(int i:papersList) {
    		papers= papers + services.papersList.get(i)+",";
    	}
    	papers.substring(0,papers.length()-1);
 
    	data.papers=papers;
    	return data;
    }
    
    @FXML
    void doDelete(ActionEvent event) {
    	if(isPrimaryKeyValidated()) {
    		services.delete(mobileNumber);
    		comboMobile.getItems().remove(mobileNumber);
    		clearFields();
    	}
    }
   ////////////////////////////////////////////////// 
    boolean isValidated() {
    	boolean flag=isPrimaryKeyValidated();
    	if(txtName.getText().isEmpty()) {
    		helpName.setText("Please enter name");
    		flag=false;
    	}
    	if(txtAddress.getText().isEmpty()) {
    		helpAddress.setText("Please enter address");
    		flag=false;
    	}
    	if(comboArea.getSelectionModel().getSelectedIndex() == -1) {
    		helpAreas.setText("Please select area");
    		flag=false;
    	}
    	if(comboHawker.getSelectionModel().getSelectedIndex() == -1) {
        	helpHawkers.setText("Please select hawker");
    		flag=false;
    	}
    	if(listPapers.getSelectionModel().getSelectedIndex() == -1) {
        	helpPapers.setText("Please select atleast one newspaper");
    		flag=false;
    	}

    	return flag;
    }
    boolean isPrimaryKeyValidated() {
    	if(mobileNumber==null) {
    		helpMobile.setText("Please enter mobile number");
    		return false;
    	}
    	return true;
    }
    void clearFields() {
    	//clearing fields
    	txtName.clear();
    	txtBill.setText("0.0");
    	txtDate.setValue(LocalDate.now());
    	txtAddress.clear();
    	comboArea.getSelectionModel().clearSelection();
    	comboHawker.getItems().clear();
    	
    	listPapers.getSelectionModel().clearSelection();
    	listPrices.getSelectionModel().clearSelection();
    	
    	comboMobile.getEditor().clear();
    	comboMobile.getSelectionModel().clearSelection();
    	
    
    	
    	//initial state of fields
		mobileNumber=null;
		
    
		
    	//removing error messages
    	helpAddress.setText("*");
    	helpAreas.setText("*");
    	helpPapers.setText("*");
    	helpName.setText("*");
    	helpMobile.setText("*");
    	helpHawkers.setText("*");
    
    	
    	
    	//enabling buttons
    	btnDelete.setDisable(false);
    	btnNew.setDisable(false);
    	btnSave.setDisable(false);
    	btnUpdate.setDisable(false);
    	
		System.out.println("Cleared Fields, state recovered, removed error messages, enabled buttons");
    }
}


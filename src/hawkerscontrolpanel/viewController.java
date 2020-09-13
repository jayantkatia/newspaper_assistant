package hawkerscontrolpanel;

//TODO::  along with_     paper_master  validations

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import commonlogic.RequiredImageLogic;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class viewController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private ImageView img;
    @FXML
    private URL location;

    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Rectangle imgContainer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtMobile;

    @FXML
    private ListView<String> listAreas;

    @FXML
    private TextField txtSalary;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private ComboBox<String> comboName;

    @FXML
    private Button btnAddNewAreas;
    
    @FXML
    private DatePicker txtDate;

    @FXML
    private Text helpDate;
   
    @FXML
    private Text helpName;

    @FXML
    private Text helpMobile;

    @FXML
    private Text helpAddress;

    @FXML
    private Text helpAreas;

    @FXML
    private Text helpImage;

    @FXML
    private Text helpSalary;

    
    private  RequiredImageLogic imageLogic;
    private  DatabaseServices services;
    private  String name;
    private String areas="";
    
    @FXML
    void initialize() {
    	imageLogic=new RequiredImageLogic("file:static/images/aadhar_card_icon.png","hawkers");
    	services=new DatabaseServices();
    	fillComboAndList();
    	listAreas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
    	
    	txtDate.setValue(LocalDate.now());	
    }
    void fillComboAndList() {
    	services.fetchCombosAndList();
    	comboName.getItems().addAll(services.namesList);
    	for(String i:services.namesList) {
    		System.out.println(i);
    	}
    	listAreas.getItems().addAll(services.areasList);
    }

    @FXML
    void doDelete(ActionEvent event) {
    	if(isPrimaryKeyValidated()) {
    		Task<Void> task=imageLogic.deleteImage(imageLogic.getFileName());
        	Thread th=new Thread(task);
        	
        	th.start();
        	services.delete(name);
        	
        	comboName.getItems().remove(name);
        	services.namesList.remove(name);
        	
        	clearFields();
    	}
    }

    @FXML
    void doImage(MouseEvent event) {
    	imgContainer.requestFocus();
    	
    	Image image=imageLogic.getImageAfterChoose();
    	if(image!=null) {
    		img.setImage(image);
    		helpImage.setText("*");
    	}else
    		helpImage.setText("Might be due to broken image, pls try more supported format png");
    }
    @FXML
    void doNew(ActionEvent event) {
    	clearFields();
    }

    @FXML
    void doSalary(ActionEvent event) {
    	//
    }
    @FXML
    void doMobile(ActionEvent event) {
    	//
    }

    
   boolean isPrimaryKeyValidated() {
	   
	   if(name==null){
   		helpName.setText("Please fill this field");
   		return false;
   		}
	   return true;
   }

   void clearFields() {
    	//clearing fields
    	txtAddress.clear();
    	txtMobile.clear();
    	txtSalary.clear();
    	
    	comboName.getEditor().clear();
    	comboName.getSelectionModel().clearSelection();
    	
    	listAreas.getSelectionModel().clearSelection();
    	
    	//initial state of fields
    	imageLogic.setIntialState();
		name=null;
		areas="";
		img.setImage(new Image(imageLogic.dummyImage));
    
		
    	//removing error messages
    	helpAddress.setText("*");
    	helpAreas.setText("*");
    	helpImage.setText("*");
    	helpName.setText("*");
    	helpMobile.setText("*");
    	helpSalary.setText("*");
    
    	
    	
    	//enabling buttons
    	btnDelete.setDisable(false);
    	btnNew.setDisable(false);
    	btnSave.setDisable(false);
    	btnUpdate.setDisable(false);
    	
		System.out.println("Cleared Fields, state recovered, removed error messages, enabled buttons");

    }
   

    @FXML
    void doName(ActionEvent event) {
    	name=comboName.getSelectionModel().getSelectedItem();
    	ModelHawkers data=services.fetch(name);
    	if(data != null) {

    		//update fields
    		txtAddress.setText(data.address);
    		txtMobile.setText(data.mobileNumber);
    		txtDate.setValue(data.doj.toLocalDate());
    		
    		Float salary=(float) data.salary;
    		txtSalary.setText(salary.toString());
    		
    		System.out.println(data.areas);
        	listAreas.getSelectionModel().clearSelection();  
    		String[] areasToSelect=data.areas.split(",");
    		for(String i:areasToSelect) {
    			System.out.println(i);
    			listAreas.getSelectionModel().select(i);
    		}
    		
    		img.setImage(new Image(data.aadharPic));
    		imageLogic.imagePath=data.aadharPic;
    		imageLogic.choosenImage=null;
    		
    		//if record exists then save disabled  ,   update,delete enable
			btnSave.setDisable(true);
			btnDelete.setDisable(false);
			btnUpdate.setDisable(false);
    
    	}else {
    		//if record doesn't exists then save enabled   update and delete disabled
    		btnUpdate.setDisable(true);
			btnDelete.setDisable(true);
			btnSave.setDisable(false);
    	}
    	
    	
    	
    	
    	helpName.setText("*");
    }
    @FXML
    void doAddNewAreas(ActionEvent event) {
    	System.out.println("Hello");
    	TextInputDialog dialog=new TextInputDialog();
    	dialog.setTitle("Add New Area");
    	
    	dialog.getDialogPane().setContentText("Enter Area :");
    	dialog.showAndWait();
    	TextField input=dialog.getEditor();
    	
    	
    	if(!input.getText().isEmpty()) {
    		System.out.println(input.getText());
    		if(! services.areasList.contains(input.getText())) {
    			listAreas.getItems().add(input.getText());
    			services.areasList.add(input.getText());
    		}
    		else
    			helpAreas.setText("ListBox Already contains "+ input.getText());
    			
    	}
    	
    }

    @FXML
    void doAddress(ActionEvent event) {
    	//
    }
    void getAreas() {
    	ObservableList<String> list= listAreas.getSelectionModel().getSelectedItems();
    	areas="";
    	for(String i:list) {
    		areas=areas+i+",";
    	}
    	areas=areas.substring(0,areas.length()-1);
    	System.out.println(areas);
    }
   

    @FXML
    void doSave(ActionEvent event) {
    	getAreas();
    	if(isValidated()) {
    		Task<Void> task=imageLogic.savingUpdatingAfterChoosing(name);
        	Thread th=new Thread(task);
        	th.start();
        	
        	services.save(getModelHawkers());
        	
        	comboName.getItems().add(name);
        	services.namesList.add(name);
        	
        	clearFields();

    	}
    }

    @FXML
    void doUpdate(ActionEvent event) {
    	getAreas();
    	if(isValidated()) {
    		Task<Void> task=imageLogic.savingUpdatingAfterChoosing(name);
        	Thread th=new Thread(task);
        	th.start();

        	services.update(getModelHawkers());
        	
        	clearFields();
    	}
    }
    
    void executeTasks(Task<Void> task) {
    	
    	progressIndicator.progressProperty().unbind();
    	progressIndicator.progressProperty().bind(task.progressProperty());
    	
    	Thread th=new Thread(task);
    	th.start();
    	
    }

    ModelHawkers getModelHawkers() {
    	ModelHawkers data=new ModelHawkers();
    	data.name=comboName.getSelectionModel().getSelectedItem();
    	data.address=txtAddress.getText();
    	data.areas=areas;
    	
    	
    	data.doj=Date.valueOf(txtDate.getValue());
    	
    		
    	
    	data.aadharPic=imageLogic.imagePath;
    	data.salary=(int)Float.parseFloat(txtSalary.getText());
    	data.mobileNumber=txtMobile.getText();
    	return data;
    }
    
    
    boolean isValidated() {
    	boolean flag=isPrimaryKeyValidated();
    	
    	if(txtAddress.getText().isEmpty()) {
    		helpAddress.setText("Please enter address");
    		flag=false;
    	}
    	if(txtMobile.getText().isEmpty()) {
    		helpMobile.setText("Please enter mobile number");
    		flag=false;
    	}
    	if(imageLogic.imagePath.equals(imageLogic.dummyImage) && imageLogic.choosenImage==null) {
    		helpImage.setText("Please upload an image");
    		flag=false;
    	}
    	if(txtSalary.getText().isEmpty()) {
    		helpSalary.setText("Please input salary");
    		flag=false;
    	}	
    	
    	
    	System.out.println(areas);
    	if(areas.isEmpty()) {
    		helpAreas.setText("Please select atleast one area");
    		flag=false;
    	}
    	return flag;
    }
    
}

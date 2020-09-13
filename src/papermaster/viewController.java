package papermaster;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import commonlogic.RequiredImageLogic;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

//TODO::  Support of jpg,jpeg
//TODO::  Delete when user fetches>change image with different format >saves   ....delete prev image with different format
//TODO::  Add validation for txtPrice to allow only numbers

public class viewController {
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private ProgressIndicator progressIndicator;
	@FXML
	private ComboBox < String > comboTitle;
	@FXML
	private TextField txtPrice;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnUpdate;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnNew;
	@FXML
	private ImageView img;
	@FXML
	private Text txtHelpTitle;
	@FXML
	private Text txtHelpPrice;
	@FXML
	private Text txtHelpImage;
	
	
	

	
	
	
	private DatabaseServices services;
	private RequiredImageLogic imageLogic;
	private String title;
	
	@FXML
	void initialize() {
		services = new DatabaseServices();
		imageLogic=new RequiredImageLogic("file:static/images/005-add-1.png","papers");
		
		fillComboBox();
		
		System.out.println("Program is executing in "+System.getProperty("user.dir")); //gives root directory where program executes...helps in relative paths
	}
	
	
	
	@FXML
	void doTitle(ActionEvent event) {
		title=comboTitle.getSelectionModel().getSelectedItem();
		ModelPaperMaster data = services.fetchData(title);
		//fetches data
		
		if (data != null) {
			System.out.println("Values Fetched...");
			txtPrice.setText(data.price.toString());
			img.setImage(new Image(data.imagePath));	
			imageLogic.imagePath=data.imagePath; //updating image
			
			imageLogic.choosenImage=null;   //choosen_image != null  && if path fetched then both true and choosenImage of previous will get used 
			// since in imageLogic.savingUpdatingAfterChoosing()  if(choosenImage==null)=>false
			
			
			//if record exists then save disabled  ,   update,delete enable
			btnSave.setDisable(true);
			btnDelete.setDisable(false);
			btnUpdate.setDisable(false);
		
		} else {
			//if record does not exists then save enable      update,delete disable
			btnUpdate.setDisable(true);
			btnDelete.setDisable(true);
			btnSave.setDisable(false);
		}
		txtHelpTitle.setText("*");
	
	}
	
	@FXML
	void doImage(MouseEvent event) {
		img.requestFocus();             //to make title value enter as focus still remains on title box
		
		
//		if (!isPrimaryKeyValidated())   //checks if title validated first cause' user might set image first
//			return;                 //choosen_image != null  && if path fetched then both true and choosenImage of previous will get used 
									// since in imageLogic.savingUpdatingAfterChoosing()  if(choosenImage==null)=>false
		Image image=imageLogic.getImageAfterChoose();
		if(image != null) {
			img.setImage(image); //sets chosen image
			txtHelpImage.setText("*");
		}
		else
			txtHelpImage.setText("Might be due to broken image, pls try more supported format png");
	
		
	}
	
	@FXML
	void doNew(ActionEvent event) {
		clearFields();
	}
	@FXML
	void doSave(ActionEvent event) {
		if (isValidated()) {
			
			Task<Void> task=imageLogic.savingUpdatingAfterChoosing(title); 
			executeLocalTasksWithProgressIndicator(task);
			
			services.saveForPaperMaster(getPaperMasterObject());
			
			fillComboBox();    //required since new entry
			clearFields();		//order must be maintained after filling only clear might be due to selectionIndex 
			    
		}
	}
	@FXML
	void doUpdate(ActionEvent event) {
		if (isValidated()) {
		
				Task<Void> task=imageLogic.savingUpdatingAfterChoosing(title);
				if(task!=null) {
					executeLocalTasksWithProgressIndicator(task);
				}       //task null in case user fetches and does not updates previous image 
			services.updateForPaperMaster(getPaperMasterObject());  
			clearFields();
		}		
	}
	@FXML
	void doDelete(ActionEvent event) {
		if(isPrimaryKeyValidated()) {
				Task<Void> task=imageLogic.deleteImage(imageLogic.getFileName());
				Thread th = new Thread(task);
				th.start();
				
				services.deleteForPaperMaster(title);
				
				fillComboBox();   //required since entry removed
				clearFields();   
		}
	}
	

	
	
	void fillComboBox() {
		comboTitle.getItems().removeAll(comboTitle.getItems());

		ArrayList < String > list = services.fetchTitles();
		comboTitle.getItems().addAll(list);
		System.out.println("Title Combo Box filled/re-filled");
		
		comboTitle.requestFocus();    //to simulate a false click as close button still not clears
	}
	ModelPaperMaster getPaperMasterObject() {
		return new ModelPaperMaster(title, imageLogic.imagePath, Float.parseFloat(txtPrice.getText()));
	}
	boolean isPrimaryKeyValidated() {
		if (title==null) {
			txtHelpTitle.setText("Please enter title");
			return false;
		}
		return true;
	}
	boolean isValidated() {
		boolean flag=isPrimaryKeyValidated();
		
		//if both dummyImage and null(chosen) then error.... any one or more failing  ensures image is there
		if (imageLogic.imagePath.equals(imageLogic.dummyImage) && imageLogic.choosenImage==null ) {
			txtHelpImage.setText("Please upload an image");
			flag= false;
		}
		
		if (txtPrice.getText().isEmpty()) {
			txtHelpPrice.setText("Please specify price");
			flag=false;
		}
		
		System.out.println("Fields are Valid :: "+flag);
		return flag;
	}
	
	void executeLocalTasksWithProgressIndicator(Task<Void> task) {
		progressIndicator.progressProperty().unbind();
		progressIndicator.progressProperty().bind(task.progressProperty());
		Thread th = new Thread(task);
		th.start();
	}
	
	void clearFields() {
		//clearing fields
		comboTitle.getSelectionModel().clearSelection();
		comboTitle.getEditor().clear();
		txtPrice.clear();
		img.setImage(new Image(imageLogic.dummyImage));
		
		//setting initial states
		imageLogic.setIntialState();
		title=null;

		
		//removing error messages
		txtHelpTitle.setText("*");
		txtHelpPrice.setText("*");
		txtHelpImage.setText("*");

		//enabling buttons
		btnUpdate.setDisable(false);
		btnSave.setDisable(false);
		btnDelete.setDisable(false);
		
		
		System.out.println("Cleared Fields, state recovered, removed error messages, enabled buttons");
	}
}
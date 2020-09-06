package papermaster;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


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
	
	
	@FXML
	void doImage(MouseEvent event) {
		img.requestFocus();
		if (!isPrimaryKeyValidated()) 
			return;
		
		Image image=imageAndDialogs.getImageAfterChoose();
		if(image != null)
			img.setImage(image);
//		  Dialog<String> dialog = new Dialog<String>();
//	      //Setting the title
//	      dialog.setTitle("Dialog");
//	      ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
//	      //Setting the content of the dialog
//	      dialog.setContentText("This is a sample dialog");
//	      //Adding buttons to the dialog pane
//	      dialog.getDialogPane().setContent(progressIndicator);
//	      dialog.getDialogPane().getButtonTypes().add(type);
//	      dialog.show();
	}

	
	
	
	private DatabaseServices services;
	private ImageAndDialogs imageAndDialogs;

	private final String dummyImage="file:static/images/005-add-1.png";
	
	
	
	@FXML
	void initialize() {
		services = new DatabaseServices();
		imageAndDialogs=new ImageAndDialogs(dummyImage);
		fillComboBox();
		System.out.println("Program is executing in "+System.getProperty("user.dir")); //gives root directory where program executes...helps in relative paths
	
	}
	
	
	
	@FXML
	void doTitle(ActionEvent event) {
		ModelPaperMaster data = services.fetchData(comboTitle.getSelectionModel().getSelectedItem());
		if (data != null) {
			txtPrice.setText(data.price.toString());
			img.setImage(new Image(data.imagePath));
	
			imageAndDialogs.imagePath=data.imagePath;
			
			//if record exists then save disabled  ,   update,delete enable
			btnSave.setDisable(true);
			btnDelete.setDisable(false);
			btnUpdate.setDisable(false);
		
		} else {
			//if record exists then save enable      update,delete disable
			btnUpdate.setDisable(true);
			btnDelete.setDisable(true);
			btnSave.setDisable(false);
		}
		txtHelpTitle.setText("*");
	}
	
	
	
	@FXML
	void doNew(ActionEvent event) {
		clearFields();
	}
	@FXML
	void doSave(ActionEvent event) {
		if (isValidated()) {
			Task<Void> task=imageAndDialogs.savingUpdatingAfterChoosing("images", comboTitle.getSelectionModel().getSelectedItem());
			executeLocalTasks(task);
			task.setOnSucceeded(new EventHandler < WorkerStateEvent > () {
				@Override
				public void handle(WorkerStateEvent event) {
					System.out.println("Done");
				}
			});
			
			services.saveForPaperMaster(getPaperMasterObject());
			fillComboBox();
			clearFields();
		}
	}
	@FXML
	void doUpdate(ActionEvent event) {
		if (isValidated()) {
			Task<Void> task=imageAndDialogs.savingUpdatingAfterChoosing("images", comboTitle.getSelectionModel().getSelectedItem());
				if(task!=null) {
					executeLocalTasks(task);
					task.setOnSucceeded(new EventHandler < WorkerStateEvent > () {
						@Override
						public void handle(WorkerStateEvent event) {
							System.out.println("Done");
							
						}
					});
				}
				services.updateForPaperMaster(getPaperMasterObject());
				clearFields();
			}
			
		
	}
	@FXML
	void doDelete(ActionEvent event) {
		if(isPrimaryKeyValidated()) {
			ModelPaperMaster data=services.fetchData(comboTitle.getSelectionModel().getSelectedItem());
			if (data!=null) {
				String[] fileNameArray=data.imagePath.split("/");
				Task<Void> task=imageAndDialogs.deleteImage(System.getProperty("user.dir")+"/database/images/"+fileNameArray[2]);
				Thread th = new Thread(task);
				th.start();
				
				task.setOnSucceeded(new EventHandler < WorkerStateEvent > () {
					@Override
					public void handle(WorkerStateEvent event) {
						System.out.println("Done");
						
					}
				});
				services.deleteForPaperMaster(comboTitle.getSelectionModel().getSelectedItem());
				clearFields();   //this must come first to delete selection
				fillComboBox();
			}else {
				//alert no such records
			}	
		}
	}
	

	
	
	void fillComboBox() {
		comboTitle.getSelectionModel().clearSelection();
		comboTitle.getEditor().clear();
		comboTitle.getItems().clear();
		ArrayList < String > list = services.fetchTitles();
		comboTitle.getItems().addAll(list);
		System.out.println("Title Combo Box filled/re-filled");
	}
	ModelPaperMaster getPaperMasterObject() {
		return new ModelPaperMaster(comboTitle.getSelectionModel().getSelectedItem(), imageAndDialogs.imagePath, Float.parseFloat(txtPrice.getText()));
	}
	boolean isPrimaryKeyValidated() {
		if (comboTitle.getSelectionModel().getSelectedItem()==null) {
			txtHelpTitle.setText("Please enter title");
			return false;
		}
		return true;
	}
	boolean isValidated() {
		boolean flag=isPrimaryKeyValidated();
		
		if (imageAndDialogs.imagePath.equals(dummyImage) && imageAndDialogs.choosenImage==null ) {
			txtHelpImage.setText("Please upload an image");
			flag= false;
		}
		
		if (txtPrice.getText().isEmpty()) {
			txtHelpPrice.setText("Please specify price");
			flag=false;
		}
		
		System.out.println(imageAndDialogs.imagePath);
		System.out.println(dummyImage);
	
		System.out.println(txtPrice.getText().isEmpty());
		System.out.println(imageAndDialogs.choosenImage ==null);
		System.out.println(imageAndDialogs.imagePath.equals(dummyImage));
		System.out.println();
		System.out.println("Fields are Valid :: "+flag);
		return flag;
	}
	
	void executeLocalTasks(Task<Void> task) {
		progressIndicator.setVisible(true);
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
		img.setImage(new Image(dummyImage));
		
		//setting initial states
		imageAndDialogs.imagePath=dummyImage;
		imageAndDialogs.choosenImage=null;
		
		
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
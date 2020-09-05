package papermaster;
//TODO::  Unlink image on update new
//TODO::  Price Field Validation
//TODO::  Info on update and save
//TODO::  prevent user from off before uploading image
//TODO::  update and save button disable/enable

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class viewController {

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    @FXML
    private ComboBox<String> comboTitle;

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
    void doDelete(ActionEvent event) {
    	services.deleteForPaperMaster(comboTitle.getSelectionModel().getSelectedItem());
    	clearFields();
    	fillComboBox();
    }
    
   
    
    @FXML
    void doImage(MouseEvent event) {
    	FileChooser filechooser=new FileChooser();
    	filechooser.setTitle("Choose Images(jpg,png,jpeg)");
    	filechooser.getExtensionFilters().add(new ExtensionFilter("Images", extensionFilterList));
    	File choosenImage=filechooser.showOpenDialog(null);
    	
    	if(choosenImage==null)
    		return;
    	
    	progressIndicator.setVisible(true);
    	
    	Task<Void> task= new Task<Void>() {
			@Override
			protected Void call() throws Exception{
				FileInputStream f1=new FileInputStream(choosenImage);
					
					String fileName=choosenImage.getName();
					System.out.println(fileName);
					FileOutputStream f2=new FileOutputStream("database/images/"+fileName);
					
					int maxlength=f1.available();
					int i=0;
					for(int c=f1.read(); c!=-1 ;c=f1.read()) {
						f2.write(c);
						updateProgress(i,maxlength);
						i++;
					}
					f1.close();
					f2.close();
				return null;
			}
    	};
    		
    	
    	
    		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				
				@Override
				public void handle(WorkerStateEvent event) {
					System.out.println("Done ");
			    	progressIndicator.setVisible(false);

				}
			});
    	    progressIndicator.progressProperty().unbind();
    	    progressIndicator.progressProperty().bind(task.progressProperty());  
    	    
    	    Thread th = new Thread(task);
    	    th.start();
    	    
    	    
    	Image pic=null;
		try {
		 pic=new Image(new FileInputStream(choosenImage));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(pic != null) {
			img.setImage(pic);
			imagePath="database/images/"+choosenImage.getName();
		}
    }

    @FXML
    void doNew(ActionEvent event) {
    	comboTitle.getSelectionModel().clearSelection();
    	txtPrice.clear();
    	img.setImage(new Image("file:static/images/005-add-1.png"));	
    }

    @FXML
    void doSave(ActionEvent event) {
    	if(checkForNull()) {
    		services.saveForPaperMaster(getPaperMasterObject());
    		fillComboBox();
    	}
//    	else {
//
//    	}
    }
    @FXML
    void doTitle(ActionEvent event) {

    	ModelPaperMaster data=services.fetchData(comboTitle.getSelectionModel().getSelectedItem());
    	if(data != null) {
    		txtPrice.setText(data.price.toString());
    		imagePath="file:"+data.imagePath;
    		img.setImage(new Image(imagePath));
//    		btnSave.setDisable(true);
    	}else {
//    		btnUpdate.setDisable(true);
//    		btnDelete.setDisable(false);
    	}
    }
    @FXML
    void doUpdate(ActionEvent event) {
    	if(checkForNull()) {
    		services.updateForPaperMaster(getPaperMasterObject());
    	}
//    	else {
//    		
//    	}
    }
    @FXML
    void initialize() {
       services= new DatabaseServices();
       fillComboBox();
       System.out.println(System.getProperty("user.dir"));
    }
    
    
    
	private DatabaseServices services;
	private String imagePath;
	private final ArrayList<String> extensionFilterList= new ArrayList<String>(Arrays.asList("*.jpg","*.jpeg","*.png"));
   
	ModelPaperMaster getPaperMasterObject() {
    	return new ModelPaperMaster(comboTitle.getSelectionModel().getSelectedItem(), imagePath, Float.parseFloat(txtPrice.getText()));
    }
    
    
    boolean checkForNull() {
    	if(imagePath!=null && !comboTitle.getSelectionModel().getSelectedItem().isEmpty() && !txtPrice.getText().isEmpty())
    		return true;
    	return false;
    }

    void fillComboBox() {
    	comboTitle.getItems().removeAll();
    	ArrayList<String> list=services.fetchTitles();
        comboTitle.getItems().addAll(list);
    }

    void clearFields() {
    	comboTitle.getSelectionModel().clearSelection();
    	txtPrice.clear();
    	imagePath=null;
    	img.setImage(new Image("file:static/images/005-add-1.png"));
    }
}



package papermaster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
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
	void doDelete(ActionEvent event) {
		if(comboTitle.getSelectionModel().getSelectedItem()!=null) {
			services.deleteForPaperMaster(comboTitle.getSelectionModel().getSelectedItem());
			clearFields();
			fillComboBox();
		}else {
			txtHelpTitle.setText("Please select title to delete");
		}

	}
	
	@FXML
	void doImage(MouseEvent event) {
		img.requestFocus();
		if (comboTitle.getSelectionModel().getSelectedItem()==null) {
			txtHelpTitle.setText("Please enter title first");
			return;
		}
		FileChooser filechooser = new FileChooser();
		filechooser.setTitle("Choose Images(jpg,png,jpeg)");
		filechooser.getExtensionFilters().add(new ExtensionFilter("Images", extensionFilterList));
		File choosenImage = filechooser.showOpenDialog(null);

		if (choosenImage == null) return;

		
		
		Dialog<String> dialog = new Dialog<String>();
	      //Setting the title
	      dialog.setTitle("Dialog");
	      ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
	      //Setting the content of the dialog
	      dialog.setContentText("This is a sample dialog");
	      //Adding buttons to the dialog pane
	      dialog.getDialogPane().setContent(progressIndicator);
	      dialog.getDialogPane().getButtonTypes().add(type);
	      dialog.show();
//		progressIndicator=new ProgressIndicator(0.0f);
		progressIndicator.setVisible(true);
		
		
		

		Task < Void > task = new Task < Void > () {@Override
			protected Void call() throws Exception {
				imageCopyFlag = false;

				FileInputStream f1 = new FileInputStream(choosenImage);

				String fileName = comboTitle.getSelectionModel().getSelectedItem() +choosenImage.getName();
				System.out.println(fileName);
				FileOutputStream f2 = new FileOutputStream("database/images/" +  fileName);

				int maxlength = f1.available();
				int i = 0;
				txtHelpImage.setText("Image is being copied on local Database");
				for (int c = f1.read(); c != -1; c = f1.read()) {
					f2.write(c);
					updateProgress(i, maxlength);
					i++;
				}
				f1.close();
				f2.close();
				return null;
			}
		};

		task.setOnSucceeded(new EventHandler < WorkerStateEvent > () {

			@Override
			public void handle(WorkerStateEvent event) {
				System.out.println("Done ");
				txtHelpImage.setText("*");
				progressIndicator.setVisible(false);
				imageCopyFlag = true;

			}
		});
		progressIndicator.progressProperty().unbind();
		progressIndicator.progressProperty().bind(task.progressProperty());

		Thread th = new Thread(task);
		th.start();

		Image pic = null;
		try {
			pic = new Image(new FileInputStream(choosenImage));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		if (pic != null) {
			img.setImage(pic);
			
			imagePath = "database/images/" + comboTitle.getSelectionModel().getSelectedItem() +choosenImage.getName();
		}
	}

	@FXML
	void doNew(ActionEvent event) {
		clearFields();
	}

	@FXML
	void doSave(ActionEvent event) {
		if (isValidated()) {
			services.saveForPaperMaster(getPaperMasterObject());
			fillComboBox();
			clearFields();
			System.out.println("mkns");
		}
	}@FXML
	void doTitle(ActionEvent event) {

		ModelPaperMaster data = services.fetchData(comboTitle.getSelectionModel().getSelectedItem());
		if (data != null) {
			txtPrice.setText(data.price.toString());
			imagePath = "file:" + data.imagePath;
			System.out.println(imagePath);
//			try {
				img.setImage(new Image(imagePath));
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}

			btnSave.setDisable(true);
			btnUpdate.setDisable(false);
		} else {
			btnUpdate.setDisable(true);
			btnDelete.setDisable(false);
			btnSave.setDisable(false);
		}

		txtHelpTitle.setText("*");
	}@FXML
	void doUpdate(ActionEvent event) {
		if (isValidated()) {
			services.updateForPaperMaster(getPaperMasterObject());
			clearFields();
		}
	}@FXML
	void initialize() {
		services = new DatabaseServices();
		fillComboBox();
		System.out.println(System.getProperty("user.dir"));
	}

	private DatabaseServices services;
	private String imagePath;
	boolean imageCopyFlag;
	private final ArrayList < String > extensionFilterList = new ArrayList < String > (Arrays.asList("*.jpg", "*.jpeg", "*.png"));

	ModelPaperMaster getPaperMasterObject() {
		return new ModelPaperMaster(comboTitle.getSelectionModel().getSelectedItem(), imagePath, Float.parseFloat(txtPrice.getText()));
	}

	boolean isValidated() {
		boolean flag=true;
		if (imagePath == null) {
			txtHelpImage.setText("Please upload an image");
			flag= false;
		}
		if (comboTitle.getSelectionModel().getSelectedItem()==null) {
			txtHelpTitle.setText("Please enter title");
			flag= false;
		}

		if (txtPrice.getText().isEmpty()) {
			txtHelpPrice.setText("Please specify price");
			flag=false;
		}
		if(!imageCopyFlag) {
			flag= false;
		}
		System.out.println(imageCopyFlag);
		System.out.println(flag);
		return flag;
	}

	void fillComboBox() {
		comboTitle.getItems().clear();
		ArrayList < String > list = services.fetchTitles();
		comboTitle.getItems().addAll(list);
	}

	void clearFields() {
		comboTitle.getSelectionModel().clearSelection();
		comboTitle.getEditor().clear();
		txtPrice.clear();
		
		
		imagePath = null;
		img.setImage(new Image("file:static/images/005-add-1.png"));
		
		  Dialog<String> dialog = new Dialog<String>();
	      //Setting the title
	      dialog.setTitle("Dialog");
	      ButtonType type = new ButtonType("Ok", ButtonData.OK_DONE);
	      //Setting the content of the dialog
	      dialog.setContentText("This is a sample dialog");
	      //Adding buttons to the dialog pane
	      dialog.getDialogPane().getButtonTypes().add(type);
	      dialog.show();
	      

		txtHelpTitle.setText("*");
		txtHelpPrice.setText("*");
		txtHelpImage.setText("*");

		btnUpdate.setDisable(false);
		btnSave.setDisable(false);
		btnDelete.setDisable(false);
	}
}
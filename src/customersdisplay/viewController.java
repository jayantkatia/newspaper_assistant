package customersdisplay;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class viewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> comboFilter;

    @FXML
    private ComboBox<String> comboValue;

    @FXML
    private Text helpfilter;

    @FXML
    private TableView<UserBean> table;
    
    @FXML
    void doFilter(ActionEvent event) {
    	ArrayList<String> data;
    	if(comboFilter.getSelectionModel().getSelectedItem().equals("Areas")) 
    		data=services.getAreas();
    	else
    		data=services.getPapers();
    	
    	comboValue.getItems().clear();
    	comboValue.getItems().addAll(data);
    }
    
    @FXML
    void doValue(ActionEvent event) {
    		ObservableList<UserBean> data=services.getRecords(comboFilter.getSelectionModel().getSelectedItem(),comboValue.getSelectionModel().getSelectedItem());
    		if(data.size()==0) {
    			System.out.println("Returning due to zero entries");
    			return;
    		}
    		table.getColumns().clear();
    		generateCommonColumns();
            table.getItems().clear();
            table.getItems().addAll(data);
    }
    
    @SuppressWarnings("unchecked")   // a warning to tell that compiler can't ensure type safety
	void generateCommonColumns() {
    	
    	TableColumn<UserBean,String> nameCol = new TableColumn<UserBean,String>("First Name");
        nameCol.setMinWidth(120);
        nameCol.setCellValueFactory(new PropertyValueFactory<UserBean, String>("name"));
		
        TableColumn<UserBean, String> mobileCol=new TableColumn<UserBean,String>("Mobile Number");
        mobileCol.setMinWidth(150);
        mobileCol.setCellValueFactory(new PropertyValueFactory<UserBean,String>("mobileNumber"));
        
        TableColumn<UserBean, String> hawkerCol=new TableColumn<UserBean, String>("Hawker Name");
        hawkerCol.setMinWidth(120);
        hawkerCol.setCellValueFactory(new PropertyValueFactory<UserBean,String>("hawkerName"));
        
        TableColumn<UserBean, String> addressCol=new TableColumn<UserBean, String>("Address");
        addressCol.setMinWidth(150);
        addressCol.setCellValueFactory(new PropertyValueFactory<UserBean,String>("address"));
        
        TableColumn<UserBean, String> dateCol=new TableColumn<UserBean, String>("Date of Subs");
        dateCol.setMinWidth(120);
        dateCol.setCellValueFactory(new PropertyValueFactory<UserBean,String>("dos"));
        
        
        TableColumn<UserBean,String> extraCol;
        if(comboFilter.getSelectionModel().getSelectedItem().equals("Papers")) {
        	extraCol= new TableColumn<UserBean,String>("Area");
            extraCol.setCellValueFactory(new PropertyValueFactory<UserBean, String>("area"));
        }else {
        	extraCol= new TableColumn<UserBean,String>("Papers");
            extraCol.setCellValueFactory(new PropertyValueFactory<UserBean, String>("papers"));
        }
        extraCol.setMinWidth(180);
 
        table.getColumns().addAll(nameCol,mobileCol,hawkerCol,addressCol,dateCol,extraCol);        
    }
    
    private DatabaseServices services;
    
    @FXML
    void initialize() {
       comboFilter.getItems().addAll("Areas","Papers");
       services=new DatabaseServices();
    }
}

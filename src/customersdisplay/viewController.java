package customersdisplay;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class viewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<?> comboFilter;

    @FXML
    private ComboBox<?> comboValue;

    @FXML
    private Text helpfilter;

    @FXML
    private Text helpvalue;

    @FXML
    private TableView<?> table;

    @FXML
    void doFilter(ActionEvent event) {

    }

    @FXML
    void doValue(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert comboFilter != null : "fx:id=\"comboFilter\" was not injected: check your FXML file 'view.fxml'.";
        assert comboValue != null : "fx:id=\"comboValue\" was not injected: check your FXML file 'view.fxml'.";
        assert helpfilter != null : "fx:id=\"helpfilter\" was not injected: check your FXML file 'view.fxml'.";
        assert helpvalue != null : "fx:id=\"helpvalue\" was not injected: check your FXML file 'view.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'view.fxml'.";

    }
}

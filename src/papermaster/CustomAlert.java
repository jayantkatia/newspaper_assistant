package papermaster;

import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;

public class CustomAlert {
	
	
	 static final String icon_delete="file:static/database/002-trash-1.png";
	 static final String icon_save="file:static/database/006-ok.png";
	 static final String icon_update="file:static/database/003-refresh.png";
	
	public static void showDialog(int count,String label) {

    	Alert alert = new Alert(Alert.AlertType.ERROR);

    	ImageView icon;
    	if(label.equals("delete")) {
    		alert.setHeaderText(count+" Records successfully deleted");
            alert.setTitle("Deleted Successfully");
             icon = new ImageView(icon_delete);
    	}else if(label.equals("update")) {
    		alert.setHeaderText(count+" Records successfully updated");
            alert.setTitle("Updated Successfully");
             icon = new ImageView(icon_update);
    	}else {
    		alert.setHeaderText(count+" Records successfully saved");
            alert.setTitle("Saved Successfully");
             icon = new ImageView(icon_save);
    	}

        // The standard Alert icon size is 48x48
        icon.setFitHeight(48);
        icon.setFitWidth(48);

        // Set our new ImageView as the alert's icon
        alert.getDialogPane().setGraphic(icon);
        alert.show();
	}
}

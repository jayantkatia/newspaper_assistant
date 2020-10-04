package billHistoryDisplay;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbcConnection.ConnectToJDBC;

public class DatabaseServices {
	private Connection connection;
	public DatabaseServices() {
		connection=ConnectToJDBC.getConnection();
	}
	ObservableList<UserBean> getMobileList(String mobileNumber,String status){
		ObservableList<UserBean> data=null;
		try {
			PreparedStatement ppstm=connection.prepareStatement("select billgenerator.*,customersmanager.name from billgenerator left join customersmanager on billgenerator.customermobile = customersmanager.mobilenumber where customermobile like ? && status like ?");
			ppstm.setString(1, mobileNumber);
			ppstm.setString(2, status);
			ResultSet table=ppstm.executeQuery();
			if(table.getFetchSize()!=0) {
				data=FXCollections.observableArrayList();
			}
			while(table.next()) {
				data.add(new UserBean(table.getString("name"), table.getString("customermobile"), table.getInt("days"), table.getDate("dobilling"), table.getFloat("amount")));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
}

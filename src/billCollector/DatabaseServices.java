package billCollector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import commonlogic.CustomAlert;
import jdbcConnection.ConnectToJDBC;

public class DatabaseServices {
	private Connection connection;
	
	DatabaseServices() {
		connection=ConnectToJDBC.getConnection();
	}
	
	ArrayList<ModelBillCollector> fetch(String mobileNumber){
		ArrayList<ModelBillCollector> data=null;
		try {
			data=new ArrayList<ModelBillCollector>();
			PreparedStatement ppstm=connection.prepareStatement("select days,dobilling,amount from billgenerator where status=0 AND customermobile=?");
			ppstm.setString(1, mobileNumber);
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				data.add(ModelBillCollector.getObject(table,mobileNumber));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return data;
	} 
	void save(String mobileNumber) {
		try {
			PreparedStatement ppstm=connection.prepareStatement("update billgenerator set status=1 where customermobile=?");
			ppstm.setString(1, mobileNumber);
			int rows_affected=ppstm.executeUpdate();
			System.out.println(rows_affected+" Updated Successfully");
			CustomAlert.showDialog(rows_affected,"paid");  //rows will be more since all previous are also updated
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	ArrayList<String> fetchMobileNumbers(){
		ArrayList<String> mobiArrayList=null;
		try {
			mobiArrayList=new ArrayList<String>();
			PreparedStatement ppstm=connection.prepareStatement("select distinct customermobile from billgenerator where status=0");
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				mobiArrayList.add(table.getString(1));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return mobiArrayList;
	} 
}

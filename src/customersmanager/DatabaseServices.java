package customersmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import commonlogic.CustomAlert;
import jdbcConnection.ConnectToJDBC;

public class DatabaseServices {
	private Connection connection;
	
	public ArrayList<String> papersList=new ArrayList<String>();
	public ArrayList<Float> pricesList=new ArrayList<Float>();
	
//	public ArrayList<String> hawkersList=new ArrayList<String>();
	
	DatabaseServices() {
		connection=ConnectToJDBC.getConnection();
	}
	
	void save(ModelCustomers data) {
		//label must be either "update" or "save"
		String query="insert into customersmanager values(?,?,?,?,?,?,?)";
		try {
			PreparedStatement ppstm=connection.prepareStatement(query);
			System.out.println(query);
			ppstm.setString(1, data.mobileNumber);
			ppstm.setString(2, data.name);
			ppstm.setString(3, data.address);
			ppstm.setString(4, data.area);
			ppstm.setString(5, data.hawkerName);
			ppstm.setDate(6, data.dos);
			ppstm.setString(7,data.papers);
			int rows_affected = ppstm.executeUpdate();
			System.out.println(rows_affected+" Saved Successfully");
			CustomAlert.showDialog(rows_affected, "save");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	void update(ModelCustomers data) {
		//label must be either "update" or "save"
		String query="update customersmanager set name=?,address=?,area=?,hawkername=?,dos=?,papers=? where mobilenumber=?";
		try {
			PreparedStatement ppstm=connection.prepareStatement(query);
			System.out.println(query);
			ppstm.setString(7, data.mobileNumber);
			ppstm.setString(1, data.name);
			ppstm.setString(2, data.address);
			ppstm.setString(3, data.area);
			ppstm.setString(4, data.hawkerName);
			ppstm.setDate(5, data.dos);
			ppstm.setString(6,data.papers);
			int rows_affected = ppstm.executeUpdate();
			System.out.println(rows_affected+" Updated Successfully");
			CustomAlert.showDialog(rows_affected, "update");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	void delete(String mobileNumber) {
		PreparedStatement ppstm;
		try {
			ppstm = connection.prepareStatement("delete from customersmanager where mobilenumber=?");
			ppstm.setString(1, mobileNumber);
			int rows_affected=ppstm.executeUpdate();
			System.out.println(rows_affected+ " Deleted Successfully");
			
			CustomAlert.showDialog(rows_affected,"delete");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	ModelCustomers fetchData(String mobileNumber) {
		ModelCustomers data=null;
		if(mobileNumber == null) {
			return null;
		}
		try {
			PreparedStatement ppstm=connection.prepareStatement("select * from customersmanager where mobilenumber=?");
			ppstm.setString(1,mobileNumber);
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				data=new ModelCustomers();
				data.getObjectFromResultSet(table);
			}
			System.out.println("Fetched Successfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	ArrayList<String> fetchMobileNumbersList() {
		ArrayList<String> mobileNumbersList=new ArrayList<String>();

		try {
			PreparedStatement ppstm=connection.prepareStatement("select mobilenumber from customersmanager");
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				mobileNumbersList.add(table.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mobileNumbersList;
	}
	void fetchPapersAndPrices() {
		try {
			PreparedStatement ppstm=connection.prepareStatement("select title,price from papermaster");
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				papersList.add(table.getString(1));
				pricesList.add(table.getFloat(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	ArrayList<String> fetchAreas() {
		ArrayList<String> areasList=new ArrayList<String>();

		try {
			PreparedStatement ppstm=connection.prepareStatement("select areas from hawkerscontrolpanel");
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				String[] areas=table.getString("areas").split(",");
				for(String i:areas){
				if(!areasList.contains(i))
					areasList.add(i);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return areasList;
	}
	
	ArrayList<String> fetchHawkers(String area) {
		ArrayList<String> hawkersList=new ArrayList<String>();
		PreparedStatement ppstm;
		try {
			ppstm = connection.prepareStatement("select name from hawkerscontrolpanel where areas like ?");
			ppstm.setString(1,"%"+area+"%");
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				hawkersList.add(table.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hawkersList;
	} 
	
}
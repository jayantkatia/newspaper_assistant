package hawkerscontrolpanel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import commonlogic.CustomAlert;
import jdbcConnection.ConnectToJDBC;

public class DatabaseServices {
	private Connection connection;
	ArrayList<String> namesList=new ArrayList<String>();
	ArrayList<String> areasList=new ArrayList<String>();
	
	DatabaseServices(){
			connection = ConnectToJDBC.getConnection();
	}
	
	void update(ModelHawkers data) {
		//label must be either update or save
		String query="update hawkerscontrolpanel set mobilenumber=?,address=?,areas=?,doj=?,salary=?,aadharpic=? where name=?";
	
		try {
			PreparedStatement ppstm=connection.prepareStatement(query);
			ppstm.setString(7, data.name);
			ppstm.setString(1, data.mobileNumber);
			ppstm.setString(2, data.address);
			ppstm.setString(3, data.areas);
			ppstm.setDate(4, data.doj);
			ppstm.setInt(5,data.salary);
			ppstm.setString(6,data.aadharPic);
			
			int rows_affected=ppstm.executeUpdate();
			CustomAlert.showDialog(rows_affected,"update");
			System.out.println(rows_affected+" Updated Successfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	void save(ModelHawkers data) {
		//label must be either update or save
		String query="insert into hawkerscontrolpanel values(?,?,?,?,?,?,?)";
		try {
			PreparedStatement ppstm=connection.prepareStatement(query);
			ppstm.setString(1, data.name);
			ppstm.setString(2, data.mobileNumber);
			ppstm.setString(3, data.address);
			ppstm.setString(4, data.areas);
			ppstm.setDate(5, data.doj);
			ppstm.setInt(6,data.salary);
			ppstm.setString(7,data.aadharPic);
			
			int rows_affected=ppstm.executeUpdate();
			CustomAlert.showDialog(rows_affected,"save");
			System.out.println(rows_affected+" Saved Successfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	void delete(String name) {
		try {
			PreparedStatement ppstm=connection.prepareStatement("delete from hawkerscontrolpanel where name=?");
			ppstm.setString(1, name);
			int rows_affected=ppstm.executeUpdate();
			CustomAlert.showDialog(rows_affected,"delete");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	ModelHawkers fetch(String name) {
		ModelHawkers data=null;
		if(name == null) {
			return data;
		}
		try {
			PreparedStatement ppstm=connection.prepareStatement("select * from hawkerscontrolpanel where name=?");
			ppstm.setString(1, name);
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				data = new ModelHawkers();
				data.setValuesFromResultSet(table);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		
		}
		
		return data;
	}
	void fetchCombosAndList(){		
		try {
			PreparedStatement ppstm=connection.prepareStatement("select name,areas from hawkerscontrolpanel");
			ResultSet table=ppstm.executeQuery();
			
			while(table.next()) {
				this.namesList.add(table.getString("name"));
				String[] areas = table.getString("areas").split(",");
				for(String i:areas) {
					if(! this.areasList.contains(i)) {
						areasList.add(i);
					}
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}

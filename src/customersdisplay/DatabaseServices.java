package customersdisplay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdbcConnection.ConnectToJDBC;

public class DatabaseServices {
	private Connection connection;
	private final DateTimeFormatter format=DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	
	
	public DatabaseServices() {
		connection=ConnectToJDBC.getConnection();
	}
	
	
	ObservableList<UserBean> getRecords(String filter,String value){
		PreparedStatement ppstm;
		ObservableList<UserBean> data=FXCollections.observableArrayList();
		try {
			if(filter.equals("Areas")) {
				ppstm=connection.prepareStatement("select * from customersmanager where area=?");
				ppstm.setString(1,value);
			}else {
				ppstm=connection.prepareStatement("select * from customersmanager where papers like ?");
				ppstm.setString(1,"%"+value+"%");
			}
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				data.add(new UserBean(table.getString("name"), table.getString("mobilenumber"), table.getString("address"), 
						table.getString("papers"), table.getString("area"), table.getString("hawkername"), 
						table.getDate("dos").toLocalDate().format(format)));
			}
			System.out.println("Data fetched successfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
	ArrayList<String> getAreas(){
		ArrayList<String> data=new ArrayList<String>();
		try {
			PreparedStatement ppstm=connection.prepareStatement("select distinct area from customersmanager");
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				data.add(table.getString("area"));
			}
			System.out.println("Data fetched successfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	ArrayList<String> getPapers(){
		ArrayList<String> data=new ArrayList<String>();
		try {
			PreparedStatement ppstm=connection.prepareStatement("select papers from customersmanager");
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				String[] papers= table.getString("papers").split(",");
				for(int i=0;i<papers.length;i++) {
					if(!data.contains(papers[i])) {
						data.add(papers[i]);
					}
				}
			}
			System.out.println("Data fetched successfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
}

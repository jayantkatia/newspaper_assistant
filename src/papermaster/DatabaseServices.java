package papermaster;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import commonlogic.CustomAlert;
import jdbcConnection.ConnectToJDBC;

public class DatabaseServices {
	private Connection connection;
	public DatabaseServices() {	
		try {
			connection=ConnectToJDBC.getConnection();
		}catch(Exception e) 
		{
			e.printStackTrace();
			System.out.println("Error");
		}
		
	}
	void saveForPaperMaster(ModelPaperMaster data) {
    	try {
			PreparedStatement ppstm=connection.prepareStatement("insert into papermaster values(?,?,?)");
			ppstm.setString(1,data.title);
			ppstm.setFloat(2,data.price);
			ppstm.setString(3,data.imagePath);
			int rows_affected=ppstm.executeUpdate();
    		CustomAlert.showDialog(rows_affected, "save");

			System.out.println(rows_affected +" are affected");
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    void updateForPaperMaster(ModelPaperMaster data) {
    	try {
			PreparedStatement ppstm=connection.prepareStatement("update  papermaster set price=?, pic=? where title=?");
			ppstm.setFloat(1,data.price);
			ppstm.setString(2,data.imagePath);
			ppstm.setString(3,data.title);
			int rows_affected=ppstm.executeUpdate();
    		CustomAlert.showDialog(rows_affected, "update");

			System.out.println(rows_affected +" are affected");
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    void deleteForPaperMaster(String title) {
    	try {
    		PreparedStatement ppstm=connection.prepareStatement("delete from papermaster where title=?");
    		ppstm.setString(1,title);
    		int rows_affected=ppstm.executeUpdate();
    		
    		CustomAlert.showDialog(rows_affected, "delete");
			System.out.println(rows_affected +" are affected"); 
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    }
    ModelPaperMaster fetchData(String title) {
    	ModelPaperMaster data = null;
    	if(title==null) {
    		return data;
    	}
    	try {
			PreparedStatement ppstm=connection.prepareStatement("select * from papermaster where title=?");
			ppstm.setString(1, title);
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				data=new ModelPaperMaster(table.getString("title"),table.getString("pic"),table.getFloat("price"));
			}
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return data;
    }
    
    ArrayList<String> fetchTitles(){
    	ArrayList<String> list=new ArrayList<String>();
    	try {
			PreparedStatement ppstm=connection.prepareStatement("select title from papermaster");
			ResultSet table=ppstm.executeQuery();
			while(table.next()) {
				list.add(table.getString("title"));
			}
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return list;
    }
}

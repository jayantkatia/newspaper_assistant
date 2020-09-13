package customersmanager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelCustomers {
	public String name;
	public String mobileNumber;
	public String address;
	public String papers="";
	public String area;
	public String hawkerName;
	public java.sql.Date dos; 
	
	
	public void getObjectFromResultSet(ResultSet table) throws SQLException{
		this.name=table.getString("name");
		this.address=table.getString("address");
		this.area=table.getString("area");
		this.papers=table.getString("papers");
		this.mobileNumber=table.getString("mobilenumber");
		this.hawkerName=table.getString("hawkername");
		this.dos=table.getDate("dos");
	};
}

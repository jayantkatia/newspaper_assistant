package hawkerscontrolpanel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelHawkers {
	
	public String name;
	public String address;
	public String mobileNumber;
	public int salary;
	public String aadharPic;
	public String areas;
	public java.sql.Date doj;
	
	void setCurrDate() {
//		Date date
	}
	void setInitialState() {
		
	}
	void setValuesFromResultSet(ResultSet table){
		try {
		
				this.name=table.getString("name");
				this.address=table.getString("address");
				this.areas=table.getString("areas");
				this.mobileNumber=table.getString("mobilenumber");
				this.aadharPic=table.getString("aadharpic");
				this.salary=table.getInt("salary");
				this.doj=table.getDate("doj");
		
		
	
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
}

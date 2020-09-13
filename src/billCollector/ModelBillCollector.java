package billCollector;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelBillCollector {
	public Date dobilling;
	public String mobileNumber;
	public float amount;
	public int days;
	
	static ModelBillCollector getObject(ResultSet tableRow,String mobString) {
		ModelBillCollector rowData=new ModelBillCollector();
		try {
			rowData.mobileNumber=mobString;
			rowData.days=tableRow.getInt("days");
			rowData.dobilling=tableRow.getDate("dobilling");
			rowData.amount=tableRow.getFloat("amount");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowData;
	}
}

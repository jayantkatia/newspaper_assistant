package billgenerator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import customersmanager.ModelCustomers;
import jdbcConnection.ConnectToJDBC;

public class DatabaseServices {
	private Connection connection;
	public ArrayList<String> papersList=new ArrayList<String>();
	public ArrayList<Float> pricesList=new ArrayList<Float>();
	
	DatabaseServices() {
		connection=ConnectToJDBC.getConnection();
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
				data.getObjectFromResultSet(table);  //had to make public to show here--means package mein default ke ander kam kr jaata hai
			}
			System.out.println("Fetched Successfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	void updateDOSCustomerPanel(String mobileNumber) {
		try {
			PreparedStatement ppstm=connection.prepareStatement("update customersmanager set dos=? where mobilenumber=?");
			
			LocalDate today=LocalDate.now();
			LocalDate tomorrow=today.plusDays(1);
			ppstm.setDate(1,Date.valueOf(tomorrow));
			ppstm.setString(2, mobileNumber);
			ppstm.execute();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	void saveBill(ModelBill data) {
		updateDOSCustomerPanel(data.customerMobile);
		try {
			PreparedStatement ppstm=connection.prepareStatement("insert into billgenerator(customermobile,days,dobilling,amount) values(?,?,?,?)");
			ppstm.setString(1, data.customerMobile);
			ppstm.setInt(2, data.days);
			ppstm.setDate(3, data.dobilling);
			ppstm.setFloat(4, data.amount);
			int rows_affected=ppstm.executeUpdate();
			
			System.out.println(rows_affected+" Saved Successfully");
		}catch(SQLException e) {
			e.printStackTrace();
		}
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
}

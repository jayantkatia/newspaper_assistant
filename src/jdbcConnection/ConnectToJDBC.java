package jdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToJDBC{
	
	Class.forName("com.mysql.cj.jdbc.Driver");
	
	public static Connection getConnection(){
		Connection connection=null;
		
//TODO: keep these in another file and read from there, similar to api.paths in flutter
		String database="newspaper_agency";
		String username="root";
		String password="";
		
		try {
			connection=DriverManager.getConnection("jdbc:mysql://localhost/"+database,username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}

package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	public static Connection getConnection() throws SQLException, ClassNotFoundException {

		String dbDriver = "com.mysql.cj.jdbc.Driver"; 
		String dbURL = "jdbc:mysql:// localhost:3306/"; 
		// Database name to access 
		String dbName = "cs3160_coursework"; 
		String dbUsername = "root"; 
		String dbPassword = "admin"; 

		Class.forName(dbDriver); 
		Connection con = DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword); 
		return con;
	}

}

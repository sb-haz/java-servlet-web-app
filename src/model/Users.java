package model;

import java.sql.PreparedStatement;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.Statement;

import util.DBUtil;


public class Users {

	private ResultSet rs = null;
	private PreparedStatement pstmt = null;

	public Users() {

	}

	public int isValid(String name, String pwd) {

		try {

			Connection connection = DBUtil.getConnection();

			if (connection != null) {

				// If the user does not exist, it returns -1.
				// If the username and password are correct, it returns the 'clientID' value from the database.

				// Prepare statement, Set parameters, Execute SQL query
				PreparedStatement stmt = connection.prepareCall("SELECT * FROM clients WHERE username=? AND password=?");
				stmt.setString(1, name);
				stmt.setString(2, pwd);
				rs = stmt.executeQuery();
				
				// If found in DB
				if (rs.next()) {
					
					// Return clientid
					int clientid = rs.getInt("clientid");
					return clientid;

				} else {
					
					// Else -1
					return -1;

				}

			} else {

				return -1;

			}

		} catch (Exception e) {

			System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
			return -1;

		}


	}

	public void addUser(String name, String pwd) {
		try {
			Connection connection = DBUtil.getConnection();
			if (connection != null) {
				
				// Add new user into DB with their username and password
				
				// Prepare statement, Set parameters, Execute SQL query
				PreparedStatement stmt = connection.prepareCall("INSERT INTO clients (username,password) VALUES (?,?)");
				stmt.setString(1, name);
				stmt.setString(2, pwd);
				stmt.executeUpdate();
				
                connection.close();
			}
		}
		catch(Exception e) {
			System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
		}
	}
}

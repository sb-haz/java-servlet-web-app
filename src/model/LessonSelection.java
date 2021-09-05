package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import util.DBUtil;

public class LessonSelection  {

	private HashMap<String, Lesson> chosenLessons;
	private int ownerID;

	private ResultSet rs = null;
	private Statement st = null;

	public LessonSelection(int owner) {

		chosenLessons = new HashMap<String, Lesson>();
		this.ownerID = owner;

		try {
			
			Connection connection = DBUtil.getConnection();

			try {

				if (connection != null) {
					
					// Get the details of any lessons currently selected by this user
					
					// Prepare statement
					PreparedStatement myStmt = connection.prepareCall("SELECT distinct lessons.description, lessons.level, lessons.startDateTime, lessons.endDateTime, lessons.lessonid FROM lessons JOIN lessons_booked ON lessons.lessonid = lessons_booked.lessonid WHERE lessons_booked.clientid=?");
					
					// Set parameters
					myStmt.setInt(1, owner);
					
					// Execute SQL query
					rs = myStmt.executeQuery();
					
					// Loop through result set to add lessons to selection
					while (rs.next()) {
						
						// Get DB data and set it to local variables
						String description = rs.getString("description");
						int level = rs.getInt("level");
						Timestamp startDateTime = rs.getTimestamp("startDateTime");
						Timestamp endDateTime = rs.getTimestamp("endDateTime");
						String lessonid = rs.getString("lessonid");
						
						// Create new Lesson object with data
						Lesson lesson = new Lesson(description, startDateTime, endDateTime, level, lessonid);
						
						// Add it to chosen lessons hashmap
						chosenLessons.put(lessonid, lesson);

					}
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
			}

		} catch (Exception e){
			System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
		}

	}

	/**
	 * @return the items
	 */
	public Set <Entry <String, Lesson>> getItems() {
		return chosenLessons.entrySet();
	}

	public void addLesson(Lesson l) {

		Lesson i = new Lesson(l);
		this.chosenLessons.put(l.getId(), i);

	}
	
	public void removeLesson(Lesson l) {

		Lesson i = new Lesson(l);
		this.chosenLessons.remove(l.getId());

	}

	public Lesson getLesson(String id){
		return this.chosenLessons.get(id);
	}

	public int getNumChosen(){
		return this.chosenLessons.size();
	}

	public int getOwner() {
		return this.ownerID;
	}

	public void updateBooking() {

		try {
			
			Connection connection = DBUtil.getConnection();
			
			try {
				
				if (connection != null) {
					
					// Remove all lessons booked by the user from the DB
					
					// Prepare statement, Set parameters, Execute SQL query
					PreparedStatement stmt = connection.prepareCall("DELETE FROM lessons_booked WHERE clientid=?");
					stmt.setInt(1, ownerID);
					stmt.executeUpdate();
					
					// Add the users selected lessons to DB
					
					// Get all lesson IDs that have been selected by user
					Object[] lessonKeys = chosenLessons.keySet().toArray();
					for (int i=0; i<lessonKeys.length; i++) {
						
						// Prepare statement, Set parameters, Execute SQL query
						PreparedStatement stmt2 = connection.prepareCall("INSERT INTO lessons_booked VALUES (?,?)");
						stmt2.setInt(1, ownerID);
						stmt2.setString(2, (String)lessonKeys[i]);
						stmt2.executeUpdate();
					}
					
					connection.close();
				}

			}catch(SQLException e) {
				System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
			}
		}catch(Exception e){
			System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
		}

	}
	
}

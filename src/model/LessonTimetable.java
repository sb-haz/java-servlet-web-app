package model;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import javax.sql.DataSource;

import util.DBUtil;

public class LessonTimetable {

  private ResultSet rs = null;
  private Statement st = null;
  
  private HashMap<String, Lesson> lessons;
  
  private DataSource ds = null;
    
    public LessonTimetable() {
        try {
            Connection connection = DBUtil.getConnection();
             try {
                if (connection != null) {
                    
                    lessons = new HashMap<String, Lesson>();
                    
                    // Prepare statement, Set parameters, Execute SQL query
                    PreparedStatement myStmt = connection.prepareStatement("SELECT * FROM lessons");
                    ResultSet rs = myStmt.executeQuery();
                    
                    // Loop through result set
                    while (rs.next()) {
                    	
                    	// Get data from DB and set it to local variables
                    	String description = rs.getString("description");
                    	int level = rs.getInt("level");
                    	Timestamp startDateTime = rs.getTimestamp("startDateTime");
                    	Timestamp endDateTime = rs.getTimestamp("endDateTime");
                    	String lessonid = rs.getString("lessonid");
                    	
                    	// Add new lesson objects to lesson timetable object with lessonid as key
                    	lessons.put(lessonid, new Lesson(description, startDateTime, endDateTime, level, lessonid));
                    	                    	
                    }
                    connection.close();
                }
            } catch(SQLException e) {
                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            }
        
        
          }catch(Exception e){
             System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
          }
        
    }
    
   
    /**
     * @return the items
     */
    public Lesson getLesson(String itemID) {
        
        return (Lesson)this.lessons.get(itemID);
    }

    public HashMap getLessons() {
        
        return lessons;
        
    }
    
}

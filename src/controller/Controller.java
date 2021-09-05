package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Lesson;
import model.LessonSelection;
import model.LessonTimetable;
import model.Users;
import util.DBUtil;

@WebServlet(value = "/do/*")
public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Users users;
	private LessonTimetable availableLessons;

	public void init() {
		users = new Users();
		availableLessons = new LessonTimetable();
		
		// Give it global scope
		this.getServletContext().setAttribute("availableLessons", availableLessons);
	}

	public void destroy() {

	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");

		String action = request.getPathInfo();
		//System.out.println("action is " + action);
		RequestDispatcher dispatcher = null;

		if (action.equals("/login")) {
			
			// Get username and password from request
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			// If valid check (isValid returns -1 if not valid)
			if (users.isValid(username, password) != -1) {

				// Create new session
				HttpSession session = request.getSession(true);

				// Add their username as attribute to session
				session.setAttribute("user", username);

				// Create new LessonSelection object for the user
				int ownerID = users.isValid(username, password);
				LessonSelection ls = new LessonSelection(ownerID);
				
				// Add that object to their session
				session.setAttribute("sessionLessons", ls);

				dispatcher = this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");

			} else {

				// Invalid login so select the login view
				dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");

			}

		} else {
			HttpSession session = request.getSession(false);
			
			// Check if user has logged in, all valid session should contain user
			if (session == null || (session.getAttribute("user") == null)) {
				
				dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");

			} else {

				if (action.equals("/viewTimeTable")) {
					
					// Show timetableview jsp
					dispatcher = this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");

				} else if (action.equals("/chooseLesson")) {

					// Get list of chosen lessons in session
					LessonSelection chosenLessons = (LessonSelection) session.getAttribute("sessionLessons");

					// Get ID of chosen lesson when user submits form
					String lessonID = request.getParameter("ID");

					// Get lesson from chosenLessons with id, to check if it exists
					Lesson tempLesson = chosenLessons.getLesson(lessonID);

					// Check if already in chosenLessons
					if (tempLesson == null) {

						// Create new lesson object
						Lesson lessonToAdd = availableLessons.getLesson(lessonID);

						// Add lesson to chosenLessons
						chosenLessons.addLesson(lessonToAdd);
					}

					dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");

				} else if (action.equals("/register")) {
					
					// Get parameters from request and call addUser method which adds them to DB
					String username = request.getParameter("username");
					String password = request.getParameter("password");
					users.addUser(username, password);
					
					dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
					
				} else if (action.equals("/removeLesson")) {

					// Get list of chosen lessons in session
					LessonSelection chosenLessons = (LessonSelection) session.getAttribute("sessionLessons");

					// Get ID of chosen lesson when user submits form
					String lessonID = request.getParameter("ID");

					// Temp lesson to remove
					Lesson lessonToRemove = availableLessons.getLesson(lessonID);

					// Get lesson from chosenLessons with id, to check if it exists
					chosenLessons.removeLesson(lessonToRemove);

					dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");

				} else if (action.equals("/viewSelection")) {

					//Get here by clicking on nav bar
					dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");

				} else if (action.equals("/finaliseBooking")) {

					// Pull sessions chosenLesson into a local variable to call updateBooking method on it
					// updateBooking deletes old selection of user and adds new ones
					LessonSelection chosenLessons = (LessonSelection) session.getAttribute("sessionLessons");
					chosenLessons.updateBooking();

					dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");

				} else if (action.equals("/logOut")) {
					
					// Delete session
					session.invalidate();
					dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");

				} else {
					
					// For an unrecognised command, select the login view
					dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
				}

			} 

		}

		dispatcher.forward(request, response);

	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}
}

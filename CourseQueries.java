package PlanningTool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
// This class manages database operations
public class CourseQueries {
	// DB connection details
	private static final String URL = "jdbc:mysql://eu-cdbr-azure-west-b.cloudapp.net:3306/lastname_firstname";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	private Connection connection = null;
	private static PreparedStatement selectAllCourses = null;
	private static PreparedStatement insertCourse = null;
	private static PreparedStatement editCourse = null;
	
	
	public CourseQueries()
	{
		try
		{
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // Connection to database
			selectAllCourses = connection.prepareStatement("SELECT * FROM courses"); // query that gets all the courses from database
			
			insertCourse = connection.prepareStatement("INSERT INTO courses VALUES (?,?,?,?,?)"); // inserting courses to database based on user input
			
			editCourse = connection.prepareStatement("UPDATE courses SET courseName = ?, courseId = ?, semester = ?, courseStatus = ?, year = ? WHERE courseName = ?"); // updating courses to database based on user input
			
		}// end try
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
			System.exit(1);
		}
	}
	
	// Query to get all courses from database
	public static ArrayList<Course> getAllCourses()
	{
		ArrayList<Course> results = null;
		ResultSet resultSet = null;
		
		try
		{
			resultSet = selectAllCourses.executeQuery(); // executing query
			results = new ArrayList<Course>();
		
			while(resultSet.next())
			{
				results.add(new Course(
					resultSet.getString("courseName"), // getting the courseName value
					resultSet.getString("courseId"), // getting the courseId value
					resultSet.getString("semester"), // getting the semester value
					resultSet.getString("courseStatus"), // getting the courseStatus value
					resultSet.getInt("year"))); // getting the year value
			}
		} // end try
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
			}
			catch (SQLException sqlException)
			{
				sqlException.printStackTrace();
			}
		} // end finally
		
		return results;
	} // end method getAllCourses
	
	
	// Method to insert new Courses in database
	public static void addCourse(String courseName, String courseId, Object semester, Object courseStatus, int year)
	{
		try
		{
			// Setting the values for the question marks '?' in the prepared statement
			insertCourse.setString(1, courseName);
			insertCourse.setString(2, courseId);
			insertCourse.setString(3, (String) semester);
			insertCourse.setString(4, (String) courseStatus);
			insertCourse.setInt(5, year);
			
			int result = insertCourse.executeUpdate();
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}	
	}
	
	// Method to update new Courses in database
	public static void editCourse(String courseName, String courseId, Object semester, Object courseStatus, int year, String thisRow)
	{
		try
		{
			// Setting the values for the question marks '?' in the prepared statement
			editCourse.setString(1, courseName);
			editCourse.setString(2, courseId);
			editCourse.setString(3, (String) semester);
			editCourse.setString(4, (String) courseStatus);
			editCourse.setInt(5, year);
			editCourse.setString(6, thisRow);
			
			int result = editCourse.executeUpdate();
		}
		catch (SQLException sqlException)
		{
			sqlException.printStackTrace();
		}	
	}
}

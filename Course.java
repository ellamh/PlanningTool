package PlanningTool;

public class Course {
	// Setting variables
	private String courseName;
	private String courseId;
	private String semester;
	private String courseStatus;
	private int year;
	
	Course (String aCourseName, String aCourseId, String aSemester, String aCourseStatus, int aYear)
	{
		// Needed for executing query
		this.courseName = aCourseName;
		this.courseId = aCourseId;
		this.semester = aSemester;
		this.courseStatus = aCourseStatus;
		this.year = aYear;
	}

	/* Making getters and setters needed for file Schedule.java
	*/
	
	public String getCourseName()
	{
		return this.courseName;
	}
	
	public String setCourseName()
	{
		return this.courseName;
	}
	
	public String getCourseId()
	{
		return this.courseId;
	}
	
	public String setCourseId()
	{
		return this.courseId;
	}
	
	public String getSemester()
	{
		return this.semester;
	}
	
	public String setSemester()
	{
		return this.semester;
	}
	
	public String getCourseStatus()
	{
		return this.courseStatus;
	}
	
	public String setCourseStatus()
	{
		return this.courseStatus;
	}
	
	public int getYear()
	{
		return this.year;
	}
	
	public int setYear()
	{
		return this.year;
	}
}
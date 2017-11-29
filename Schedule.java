// Made with Jesse Inkinen and August Hermas
package PlanningTool; 

import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.ArrayList;

// Creating table that does not allow cell edition
public class Schedule extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static int MAX_QTY = 30; // Setting max amount of input rows

	private ArrayList<Course> allCourses;
	private Course currentCourse;
	
	static JTable tableCourse;
	static JButton btnAddCourse;
	static JButton btnEditCourse;
	static JButton btnDeleteCourse;
	static DefaultTableModel myCourseTableModel;
	
	// Creating the GUI for application
	public Schedule(){
		super("Courses");// Naming the application

		new CourseQueries();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);// For closing the application
		getContentPane().setLayout(null); 
		setBounds(0,0,538,331);
		setLocationRelativeTo(null); 

		JLabel lblTheseAreMy = new JLabel("These are my Courses:");
		lblTheseAreMy.setBounds(197, 6, 142, 14);
		getContentPane().add(lblTheseAreMy);
		
		tableCourse = new JTable();
		tableCourse.setShowGrid(false);
		tableCourse.setShowVerticalLines(false);
		tableCourse.setShowHorizontalLines(false);
		tableCourse.setModel(createCourseTableModel()); // Creating a custom TableModel with Course data from the database
		tableCourse.setBounds(6, 36, 526, 229);
		getContentPane().add(tableCourse);
		
		btnAddCourse = new JButton("Add Course");//Button to add course
		btnAddCourse.setBounds(351, 277, 117, 23);
		getContentPane().add(btnAddCourse);
		
		btnDeleteCourse = new JButton("Delete Course");// Button to delete course
		btnDeleteCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = tableCourse.getSelectedRow();
                DefaultTableModel model= (DefaultTableModel)tableCourse.getModel();
 
                String selected = model.getValueAt(row, 0).toString();
                               
                            //Option dialog to confirm the delete action
                            if (row >= 0) { int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to delete this course?","Warning",JOptionPane.OK_CANCEL_OPTION);
                            if(dialogResult == JOptionPane.YES_OPTION){
 
                                model.removeRow(row);
                           
                                try {
                                      // Connecting database and deleting course
                                    Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://eu-cdbr-azure-west-b.cloudapp.net:3306/harmaala_ella", "b13c4327297c36", "6591036a");
                                    PreparedStatement ps = (PreparedStatement) conn.prepareStatement("delete from courses where courseName='"+selected+"' ");
                                    ps.executeUpdate();
                                }
                                catch (Exception w) {
                                    JOptionPane.showMessageDialog(null, "Connection Error!");
                                }  
                            }                                    
                    }  
            }
           
        } );
		btnDeleteCourse.setBounds(93, 274, 117, 29);
		getContentPane().add(btnDeleteCourse);
		
		
		btnEditCourse = new JButton("Edit Course");//Button to edit course
		btnEditCourse.setBounds(222, 274, 117, 29);
		getContentPane().add(btnEditCourse);

		MyEventHandler commandHandler = new MyEventHandler();//Adding ActionListener to addCourse button
		btnAddCourse.addActionListener(commandHandler);
	
		MyEventHandler2 commandHandler1 = new MyEventHandler2();//Adding ActionListener to editCourse button
		btnEditCourse.addActionListener(commandHandler1);
	}

	//Creating custom TableModel that retrieves data from database
	private DefaultTableModel createCourseTableModel()
	{
		allCourses = PlanningTool.CourseQueries.getAllCourses();

		Object[][] data = new Object[allCourses.size()][5];
		String[] columns = new String[] {"Course Name", "Course ID", "Semester", "Status", "Year"};
		
		//loop for finding data allCourses ArrayList
		for (int row=0; row<allCourses.size(); row++){
			
			currentCourse = allCourses.get(row); // get a Course from the ArrayList allCourses
			
			data[row][0] = currentCourse.getCourseName();
			data[row][1] = currentCourse.getCourseId();
			data[row][2] = currentCourse.getSemester();
			data[row][3] = currentCourse.getCourseStatus();
			data[row][4] = currentCourse.getYear();
		}

		myCourseTableModel = new DefaultTableModel(data, columns)//Course data from database
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column)  // Disabling cell edition
			{
				return false;
			}
		};

		return myCourseTableModel;
	}

	private class MyEventHandler implements ActionListener //EventHandler for addCourse button
	{
		public void actionPerformed (ActionEvent myEvent)
		{
			if (myEvent.getSource() == btnAddCourse){
				if (allCourses.size() < MAX_QTY){// If the amount of courses is smaller than max quantity, user can add a new course
					getNewCourseFromUser();
					tableCourse.setModel(createCourseTableModel()); // new TableModel containing updated data
				}
				else{
					JOptionPane.showMessageDialog(null, "You can not add more Courses in your collection", "Info", JOptionPane.INFORMATION_MESSAGE);
				}			
			}
		}
	}
    // Creating JPanel to update data in JTable and database
	private void getNewCourseFromUser(){
		// Defining field types and input quantities
		JTextField courseNameField = new JTextField(20);
	    JTextField courseIdField = new JTextField(6);
	    JComboBox<String> semesterField = new JComboBox<String>();
	    JComboBox<String> courseStatusField = new JComboBox<String>();
	    JTextField yearField = new JTextField(4);
 
	    JPanel myPanel = new JPanel();// new JPanel for user input
	    
	    myPanel.add(new JLabel("Course Name:"));
	    myPanel.add(courseNameField);
	    
	    myPanel.add(new JLabel("Course ID:"));
	    myPanel.add(courseIdField);

	    myPanel.add(new JLabel("Semester:"));
	    myPanel.add(semesterField);
		semesterField.addItem("Spring");//adding options
		semesterField.addItem("Autumn");//adding options
	    
	    myPanel.add(new JLabel("Status:"));
	    myPanel.add(courseStatusField);
	    courseStatusField.addItem("Scheduled");//adding options
	    courseStatusField.addItem("Ongoing");//adding options
	    courseStatusField.addItem("Completed");//adding options
	    courseStatusField.addItem("Failed");//adding options
	    
	    myPanel.add(new JLabel("Year:"));
	    myPanel.add(yearField);
	    
	    int result = JOptionPane.showConfirmDialog(null, myPanel, "Enter details of your new Course", JOptionPane.OK_CANCEL_OPTION); 
	    
	    if (result == JOptionPane.OK_OPTION) { // Calling CourseQueries to insert data into database    	
	    	PlanningTool.CourseQueries.addCourse(courseNameField.getText(), courseIdField.getText(), semesterField.getSelectedItem(), courseStatusField.getSelectedItem(), Integer.parseInt(yearField.getText()));
	    }
	}
	
	private class MyEventHandler2 implements ActionListener { // EventHandler for editCourse button
		public void actionPerformed(ActionEvent thisEvent) {
			{
				if (thisEvent.getSource() == btnEditCourse){
					if (allCourses.size() <= MAX_QTY){ // If the current amount of Courses in the database is smaller or equal to max quantity, cell editing is possible. Works in every instance
						getCourseFromUser();
						tableCourse.setModel(createCourseTableModel()); // new TableModel containing updated data
					}
					else{
						JOptionPane.showMessageDialog(null, "Something went wrong", "Info", JOptionPane.INFORMATION_MESSAGE);
					}			
				}
			}
		}
    // Creating JPanel to update data in JTable and database
		private void getCourseFromUser(){
			
		DefaultTableModel model = (DefaultTableModel)tableCourse.getModel();
   
    // get selected row index
        int selectedRowIndex = tableCourse.getSelectedRow();
        String thisRow = model.getValueAt(selectedRowIndex, 0).toString();
     // Defining field types and input quantities
    		JTextField courseName = new JTextField(20);
    	    JTextField courseId = new JTextField(6);
    	    JComboBox<String> semester = new JComboBox<String>();
    	    JComboBox<String> courseStatus = new JComboBox<String>();
    	    JTextField year = new JTextField(4);
    	    
         JPanel thisPanel = new JPanel();// new JPanel for user input
    	    
    	    thisPanel.add(new JLabel("Course Name:"));
    	    thisPanel.add(courseName);
    	    
    	    thisPanel.add(new JLabel("Course ID:"));
    	    thisPanel.add(courseId);

    	    thisPanel.add(new JLabel("Semester:"));
    	    thisPanel.add(semester);
    	    semester.addItem("Spring");//adding options
    	    semester.addItem("Autumn");//adding options
    	    
    	    thisPanel.add(new JLabel("Status:"));
    	    thisPanel.add(courseStatus);
    	    courseStatus.addItem("Scheduled");//adding options
    	    courseStatus.addItem("Ongoing");//adding options
    	    courseStatus.addItem("Completed");//adding options
    	    courseStatus.addItem("Failed");//adding options
    	    
    	    thisPanel.add(new JLabel("Year:"));
    	    thisPanel.add(year);
    	    	    
    	    int result = JOptionPane.showConfirmDialog(null, thisPanel, "Enter the changes", JOptionPane.OK_CANCEL_OPTION);
    	    
    	    if (result == JOptionPane.OK_OPTION) { // Calling CourseQueries to insert data into database
    	    	PlanningTool.CourseQueries.editCourse(courseName.getText(), courseId.getText(), semester.getSelectedItem(), courseStatus.getSelectedItem(), Integer.parseInt(year.getText()), thisRow);
    	    }                          
}       
		
};
	// Setting frame visible
	public static void main(String[] args) {
		Schedule frame = new Schedule();
		frame.setVisible(true);
	}
}
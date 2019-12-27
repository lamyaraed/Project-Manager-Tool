import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.Desktop;
import java.awt.event.ActionEvent;

public class UpdateWorkingHoursForm extends JFrame {
	private JTextField ProjectIDTxt;
	private JTable MajorTasksTable;
	private JTextField SelectedIDTxt;
	private JTextField WorkingHoursTxt;
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	private Date CurrentDate;

	int ProjectID;
	Connection con;
	public UpdateWorkingHoursForm(Connection connection) throws ParseException {
		CurrentDate = format.parse(timeStamp);
		
		getContentPane().setLayout(null);
		con = connection;
		JLabel lblWtiteProjectId = new JLabel("Wtite Project ID");
		lblWtiteProjectId.setBounds(36, 38, 107, 14);
		getContentPane().add(lblWtiteProjectId);
		
		ProjectIDTxt = new JTextField();
		ProjectIDTxt.setBounds(123, 35, 86, 20);
		getContentPane().add(ProjectIDTxt);
		ProjectIDTxt.setColumns(10);
		
		JButton btnSelectProject = new JButton("Select");
		
		btnSelectProject.setBounds(241, 34, 89, 23);
		getContentPane().add(btnSelectProject);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(36, 78, 567, 154);
		getContentPane().add(tabbedPane);
		
		JScrollPane MajorTasksPane = new JScrollPane();
		tabbedPane.addTab("Major Tasks", null, MajorTasksPane, null);
		
		MajorTasksTable = new JTable();
		MajorTasksTable.setEnabled(false);
		MajorTasksPane.setViewportView(MajorTasksTable);
		
		JLabel lblSelectTheId = new JLabel("Select The ID of the Completed Task");
		lblSelectTheId.setBounds(100, 262, 185, 14);
		getContentPane().add(lblSelectTheId);
		
		SelectedIDTxt = new JTextField();
		SelectedIDTxt.setEditable(false);
		SelectedIDTxt.setBounds(289, 259, 86, 20);
		getContentPane().add(SelectedIDTxt);
		SelectedIDTxt.setColumns(10);
		
		JLabel lblActualWorkingHours = new JLabel("Actual Working Hours");
		lblActualWorkingHours.setHorizontalAlignment(SwingConstants.RIGHT);
		lblActualWorkingHours.setBounds(150, 297, 123, 14);
		getContentPane().add(lblActualWorkingHours);
		
		WorkingHoursTxt = new JTextField();
		WorkingHoursTxt.setEditable(false);
		WorkingHoursTxt.setBounds(289, 294, 86, 20);
		getContentPane().add(WorkingHoursTxt);
		WorkingHoursTxt.setColumns(10);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setEnabled(false);
		btnUpdate.setBounds(441, 293, 89, 23);
		getContentPane().add(btnUpdate);
		
		JButton btnShowAnalysisReport = new JButton("Show analysis Report");
		//btnShowAnalysisReport.setEnabled(false);
		btnShowAnalysisReport.setBounds(100, 350, 155, 30);
		getContentPane().add(btnShowAnalysisReport);

		JButton btnShowChart = new JButton("Show project chart");
		btnShowChart.setEnabled(false);
		btnShowChart.setBounds(300, 350, 155, 30);
		getContentPane().add(btnShowChart);

		btnShowChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					getProjectChart ProjectChart = new getProjectChart();
					ProjectChart.getCategoryDataset(con, ProjectID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnSelectProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProjectID = Integer.parseInt(ProjectIDTxt.getText());
				try {
					if(ValidProjectID(ProjectID))
					{
						MajorTasksTable = GetMajorTaskTable(ProjectID);
						MajorTasksPane.setViewportView(MajorTasksTable);

						btnUpdate.setEnabled(true);
						WorkingHoursTxt.setEditable(true);
						SelectedIDTxt.setEditable(true);
						
					}
					else
					{
						JOptionPane.showMessageDialog(new JFrame(), "The Selected ID doesn't exist", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(new JFrame() ,e.getMessage() , "Error", JOptionPane.ERROR_MESSAGE);		
					e.printStackTrace();
				}
			}
		});
		
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int WorkingHours = Integer.parseInt(WorkingHoursTxt.getText());
					int TaskID = Integer.parseInt(SelectedIDTxt.getText());
				
				
					if(ValidTaskID(TaskID) && !WorkingHoursTxt.getText().equals("")
							&& !SelectedIDTxt.getText().equals(""))
					{
						UpdateMajorTask(TaskID , WorkingHours);
						
						MajorTasksTable = GetMajorTaskTable(ProjectID);
						MajorTasksPane.setViewportView(MajorTasksTable);
						btnShowAnalysisReport.setEnabled(true);
						btnShowChart.setEnabled(true);
					}
					else
					{
						JOptionPane.showMessageDialog(new JFrame(), "The Selected ID doesn't exist or you didn't write an actual working hours value", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					
				}
			}
		});
		
		btnShowAnalysisReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ArrayList<MajorTask> FinishedONTime = new ArrayList<MajorTask>();
					ArrayList<MajorTask> notFinishedONTime = new ArrayList<MajorTask>();
					
					WriteAnalysisFile(FinishedONTime , notFinishedONTime);
					
				} catch (SQLException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		setSize(660, 444);
		setVisible(true);
		
	}
	
	private void WriteAnalysisFile(ArrayList<MajorTask> finishedONTime, ArrayList<MajorTask> notFinishedONTime) throws SQLException, ParseException
	{
		String sql= "SELECT * FROM  [dbo].[MajorTasks] WHERE \r\n" + 
				"[ProjectID] = " + ProjectID+";";
		Calendar calendar = Calendar.getInstance();
			
		ResultSet resultSet = null;
		try (
		            PreparedStatement prepsInsertProduct = 
		            con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) 
		        {
		            prepsInsertProduct.execute();
		            resultSet = prepsInsertProduct.getResultSet();
		            
		            while(resultSet.next()){
		                 //Retrieve by column name
		                 int nIDs  = resultSet.getInt("MajorTaskID");
		                 String Name = resultSet.getString("Name");
		                 int worhHrs  =resultSet.getInt("ActualWorkingHours");
		                 Date plannedDate = resultSet.getDate("dueDate");
		                 Date startDate = resultSet.getDate("startTime");
		                 
		                worhHrs/=24;
		             	calendar.setTime(startDate);
		             	calendar.add(Calendar.DATE, worhHrs);
		             	Date AcualEndDate = calendar.getTime();
		             	
		             
	        			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						
						System.out.println("AcualEndDate = " + AcualEndDate);
		             	System.out.println("plannedDate = " + plannedDate);

		             	if(AcualEndDate.after(plannedDate))
		             	{
		             		notFinishedONTime.add(new MajorTask(Name, nIDs));
		             		
		             		System.out.println(Name + " Yes");
		             	}
		             	else
		             	{
		             		
		             		finishedONTime.add(new MajorTask(Name, nIDs));
		             		System.out.println(Name + " NO");
		             	}
		            }
		        }
		
		WriteReportOnFile("Analysis Report.txt" , notFinishedONTime , finishedONTime);
		
	}
	

	private void WriteReportOnFile(String FilePath, ArrayList<MajorTask> notFinishedONTime,
			ArrayList<MajorTask> finishedONTime) {
		// TODO Auto-generated method stub
		String Lines = "Tasks have been finished on time : \r\n\r\n";
		for(int i = 0 ; i < finishedONTime.size() ; i++)
		{
			MajorTask majorTask = finishedONTime.get(i);
			Lines+=majorTask.ID + " - " + majorTask.Name + "\r\n";
		}
		Lines+="---------------------------------------------------------\r\n";
		Lines += "\r\nTasks which have not been finished on Time: \r\n\r\n";
		
		for(int i = 0 ; i < notFinishedONTime.size() ; i++)
		{
			MajorTask majorTask = finishedONTime.get(i);
			Lines+=majorTask.ID + " - " + majorTask.Name + "\r\n";
		}
		
		PrintWriter out;		
    	try {
			//System.out.println(Parser.OperatorFileName);
			out = new PrintWriter(FilePath);
			out.println(Lines);
			out.close();
			
			OpenFile(FilePath);
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}

	protected void UpdateMajorTask(int taskID, int workingHours) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "UPDATE MajorTasks SET ActualWorkingHours = " + workingHours + " WHERE MajorTaskID = " + taskID + ";";
		try (
				PreparedStatement prepsInsertProduct = 
				con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) 
			{
				prepsInsertProduct.execute();
			}
	}

	protected boolean ValidTaskID(int taskID) throws SQLException {
		// TODO Auto-generated method stub
		String sql= "SELECT [MajorTaskID] FROM [dbo].[MajorTasks]\r\n" + 
				"WHERE [ProjectID] ="+ ProjectID +"AND [MajorTaskID] = " + taskID;
		
		ResultSet resultSet = null;
		  try (
		            PreparedStatement prepsInsertProduct = 
		            con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) 
		        {
		            prepsInsertProduct.execute();
		            resultSet = prepsInsertProduct.getResultSet();
		            
		            while(resultSet.next()){
		                 //Retrieve by column name
		                 int nIDs  = resultSet.getInt("MajorTaskID");
		                 
		                 if(nIDs == taskID)
		                 {
		                	 return true;
		                 }
		             }
		        }  
		return false;
	}

	protected JTable GetMajorTaskTable(int projectID) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM [dbo].[MajorTasks] WHERE [ProjectID]= "+ projectID;
		
		JTable table  = new JTable();
		ResultSet resultSet = null;
		try (
				PreparedStatement prepsInsertProduct = 
						con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) 
			{
				prepsInsertProduct.execute();
				resultSet = prepsInsertProduct.getResultSet();

			    // It creates and displays the table
			    table = new JTable(buildTableModel(resultSet));
			}
		
		//table.setShowGrid(false);
		return table;
	}

	protected boolean ValidProjectID(int projectID) throws SQLException {
		// TODO Auto-generated method stub
		String sql= "SELECT [ProjectID] FROM [dbo].[Projects] \r\n" + 
				"WHERE ProjectID ="+ projectID;
		
		ResultSet resultSet = null;
		  try (
		            PreparedStatement prepsInsertProduct = 
		            con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) 
		        {
		            prepsInsertProduct.execute();
		            resultSet = prepsInsertProduct.getResultSet();
		            
		            while(resultSet.next()){
		                 //Retrieve by column name
		                 int nIDs  = resultSet.getInt("ProjectID");
		                 
		                 if(nIDs == projectID)
		                 {
		                	 return true;
		                 }
		             }
		        }  
		return false;
	}
	
	public DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
	
	class MajorTask{
		String Name;
		int ID;
		public MajorTask(String name , int id) {
			// TODO Auto-generated constructor stub
			Name = name; ID = id;
		}
	}
	
	private boolean OpenFile(String Path) {
		// TODO Auto-generated method stub
		boolean isValid = false;
		Desktop desktop = Desktop.getDesktop();
		
		
		File file = new File(Path);
		if(file.exists()) {
			try {
				desktop.open(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			isValid = true;
		}		
		return isValid;
	}
}

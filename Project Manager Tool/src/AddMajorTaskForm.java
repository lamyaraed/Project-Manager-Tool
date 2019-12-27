import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

public class AddMajorTaskForm extends JFrame {
	
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	
	private static JTable SubTasksTable;
	private JTextField TaskStartDTxt;
	private JTextField TaskDueDTxt;
	private JTextField TaskDurationDTxt;
	private JTable DependentTable; // TODO
	private JTable TeamTable; // TODO
	static Connection conn;
	
	static JScrollPane subtask;
	static int majorTaskID = 0;
	private Date startTime;
	private Date dueDate;
	private Date CurrentDate;
	boolean submittedTable = false;
	
	public AddMajorTaskForm(Connection connection, int projectID, Date ProjectStartdate, Date ProjectDuedate) throws ParseException, SQLException {
		
		CurrentDate = format.parse(timeStamp);
		
		getContentPane().setLayout(null);
		conn = connection;
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(37, 107, 595, 243);
		getContentPane().add(tabbedPane);

		SubTasksTable = new JTable();
		SubTasksTable = GetSubTaskTable(-1);
		subtask = new JScrollPane();
		tabbedPane.addTab("SubTasks", null, subtask, null); 
		
		JScrollPane DependentsScrollPane = new JScrollPane();
		tabbedPane.addTab("Dependents", null, DependentsScrollPane, null);
		
		DependentTable = new JTable();
		DependentsScrollPane.setViewportView(DependentTable);
		
		JScrollPane TeamScrollPane = new JScrollPane();
		tabbedPane.addTab("Task Team", null, TeamScrollPane, null);
		
		TeamTable = new JTable();
		TeamScrollPane.setViewportView(TeamTable);
		
		TaskStartDTxt = new JTextField();
		TaskStartDTxt.setBounds(108, 22, 86, 20);
		getContentPane().add(TaskStartDTxt);
		TaskStartDTxt.setColumns(10);
		
		TaskDueDTxt = new JTextField();
		TaskDueDTxt.setBounds(108, 68, 86, 20);
		getContentPane().add(TaskDueDTxt);
		TaskDueDTxt.setColumns(10);
		
		TaskDurationDTxt = new JTextField();
		TaskDurationDTxt.setBounds(282, 22, 86, 20);
		getContentPane().add(TaskDurationDTxt);
		TaskDurationDTxt.setColumns(10);
		
		JLabel lblStartDate = new JLabel("Start Date :");
		lblStartDate.setBounds(37, 25, 61, 14);
		getContentPane().add(lblStartDate);
		
		JLabel lblDueDate = new JLabel("Due Date :");
		lblDueDate.setBounds(37, 71, 61, 14);
		getContentPane().add(lblDueDate);
		
		JLabel lblDuration = new JLabel("Duration");
		lblDuration.setBounds(226, 25, 46, 14);
		getContentPane().add(lblDuration);
		
		JRadioButton rdbtnIsMilestone = new JRadioButton("is MileStone");
		rdbtnIsMilestone.setBounds(226, 67, 109, 23);
		getContentPane().add(rdbtnIsMilestone);
		
		addSubTaskButton btnAddSubTasks = new addSubTaskButton(connection, majorTaskID, startTime, dueDate);
		getContentPane().add(btnAddSubTasks);
		
		addDependentTasksButton btnAddDependents = new addDependentTasksButton(connection , majorTaskID , projectID);
		getContentPane().add(btnAddDependents);
		
		addmemberTaskButton btnAddTeamMemeber  = new addmemberTaskButton(connection, majorTaskID);
		getContentPane().add(btnAddTeamMemeber);
		
		JButton btnSubmitMajorTasks = new JButton("Submit Major Task Info");
		btnSubmitMajorTasks.setBounds(417, 63, 167, 31);
		getContentPane().add(btnSubmitMajorTasks);
		
		btnSubmitMajorTasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String StartD = TaskStartDTxt.getText();
				String DueD   = TaskDueDTxt.getText();
				String Name = nameTextField.getText();
				int Duration = Integer.parseInt(TaskDurationDTxt.getText());
				boolean isMil = rdbtnIsMilestone.isSelected();
				int isMilstone = isMil?1:0;
				try {
					if(Validate(StartD,DueD,Duration,ProjectStartdate, ProjectDuedate).equals("valid"))
					{
						String insertSql = "INSERT INTO MajorTasks (startTime, dueDate, duration, isMilestone, \r\n" + 
								"  ActualWorkingHours , ProjectID,Name)" + 
								"VALUES "+ 
								"('"+StartD+"','" +DueD+"'," +Duration +"," +isMilstone+ "," + 0 + "," + projectID+",'"+Name+"');";
						
					    ResultSet resultSet = null;

						try (
							PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) {
								prepsInsertProduct.execute();
					            // Retrieve the generated key from the insert.
					            resultSet = prepsInsertProduct.getGeneratedKeys();

					            // Print the ID of the inserted row.
					            while (resultSet.next()) {
					            	majorTaskID = Integer.parseInt(resultSet.getString(1));
					            	System.out.println("Generated: " + majorTaskID);
					            }
					            AddProjectForm.RefreshTables();
					            
					            btnAddSubTasks.isEditable = true;
					            btnAddDependents.isEditable= true;
					            btnAddTeamMemeber.isEditable = true;
					            
					            btnAddSubTasks.MajorTaskEnd = dueDate;
					            btnAddSubTasks.MajorTaskStart = startTime;
					            btnAddSubTasks.MajorTaskID = majorTaskID;
					           
					            btnAddDependents.MajorTaskID = majorTaskID;
					            btnAddTeamMemeber.majorTaskID = majorTaskID;
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							submittedTable = false;
							JOptionPane.showMessageDialog(new JFrame() ,e1.getMessage() , "Error", JOptionPane.ERROR_MESSAGE);		
							e1.printStackTrace();
						}
					}
					else
					{
						submittedTable = false;
						JOptionPane.showMessageDialog(new JFrame(), MessageError, "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException | HeadlessException | ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		
		
		
		
		
		nameTextField = new JTextField();
		nameTextField.setBounds(482, 22, 86, 20);
		getContentPane().add(nameTextField);
		nameTextField.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(438, 25, 46, 14);
		getContentPane().add(lblName);
	
		setSize(800, 480);
		setVisible(true);	
	}


	String MessageError = "invalid";
	private JTextField nameTextField;
	private String Validate(String startD, String dueD, int duration, Date projectStartdate, Date projectDuedate) throws ParseException {
		// TODO Auto-generated method stub
		String isValid = "valid";
		startTime = format.parse(startD);
		
		System.out.println(startTime);
		System.out.println(CurrentDate);
		System.out.println(projectStartdate);
		System.out.println(projectDuedate);
		
		if(startTime.before(CurrentDate) || startTime.before(projectStartdate) || startTime.after(projectDuedate))
		{
			MessageError = "invalid Start Date, you need to write date after : " + timeStamp;
			return "invalid";
		}
		
		dueDate = format.parse(dueD);
		if(dueDate.before(CurrentDate) || dueDate.before(startTime) || startTime.before(projectStartdate) || startTime.after(projectDuedate))
		{
			MessageError = "invalid Date, You need to write date that it beween : " + timeStamp + " and " + startTime;
			return "invalid";
		}
		
		int validDuration = (int) getDateDiff(startTime, dueDate, TimeUnit.DAYS);
		if(((duration) >= validDuration*24) || duration < 0)
		{
			MessageError = duration + " is invalid duration" + "\nYou need to write duratoin that less than : " + validDuration + " days.";
			return "invalid";
		}
		return isValid;
	}
	
	private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	private static JTable GetSubTaskTable(int majorTaskID) throws SQLException {
		// TODO Auto-generated method stub
		JTable table  = new JTable();
		ResultSet resultSet = null;
		String sql = "select * from [dbo].[SubTasks] where [subTaskID] in\r\n" + 
				"(\r\n" + 
				"select [subTaskID] from [dbo].[SubTasks] , [dbo].[MajorTasks]\r\n" + 
				"where [dbo].[SubTasks].MajorTaskID =" + majorTaskID +" \r\n" + 
				")";
		
		try (
				PreparedStatement prepsInsertProduct = 
						conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) 
			{
				prepsInsertProduct.execute();
				resultSet = prepsInsertProduct.getResultSet();

			    // It creates and displays the table
			    table = new JTable(buildTableModel(resultSet));
			}
		//table.setShowGrid(false);
		
		return table;
	}
	
	
	public static DefaultTableModel buildTableModel(ResultSet rs)
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
	
	public static void RefreshTables() throws SQLException
	{
		SubTasksTable = GetSubTaskTable(majorTaskID);
		subtask.setViewportView(SubTasksTable);

	//SubTasksTable = GetDeliverablesTable(ProjectID);
		//deliverScroll.setViewportView(DeliverablesTable);
		
		
	}
}

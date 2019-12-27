import java.sql.Connection;
import java.sql.DriverManager;
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

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

public class AddProjectForm extends JFrame
{
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	static String connectionUrl = Main.connectionUrl;
	
	Date CurrentDate;//Done
	
	Date startdate;
	Date duedate;
	
	String startDate = "";
	String DueDate = "";
	static int ProjectID = 0;
	
	boolean submittedTable = false;
	private JTextField ProjectNameTxt;
	private JTextField CostTxt;
	private JTextField StartDateTxt;
	private JTextField DueDateTxt;
	private static JTable MajorTasksTable;	
	static private JTable DeliverablesTable;
	static JTabbedPane tabbedPane;
	static Connection con ;
	
	static JScrollPane major;
	static JScrollPane deliverScroll;
	static JScrollPane TeamScroll;
	
	public AddProjectForm(Connection connection) throws ParseException, SQLException 
	{
		con = connection;
		CurrentDate = format.parse(timeStamp);
		getContentPane().setLayout(null);
		
		ProjectNameTxt = new JTextField();
		ProjectNameTxt.setBounds(112, 24, 163, 27);
		getContentPane().add(ProjectNameTxt);
		ProjectNameTxt.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(56, 30, 46, 14);
		getContentPane().add(lblName);
		
		CostTxt = new JTextField();
		CostTxt.setBounds(112, 62, 163, 27);
		getContentPane().add(CostTxt);
		CostTxt.setColumns(10);
		
		JLabel lblTotalCose = new JLabel("Total Cost");
		lblTotalCose.setBounds(37, 65, 75, 21);
		getContentPane().add(lblTotalCose);
		
		StartDateTxt = new JTextField();
		StartDateTxt.setBounds(420, 24, 138, 27);
		getContentPane().add(StartDateTxt);
		StartDateTxt.setColumns(10);
		
		JLabel lblStartDate = new JLabel("Start Date");
		lblStartDate.setBounds(360, 27, 71, 21);
		getContentPane().add(lblStartDate);
		
		JLabel lblDueDate = new JLabel("Due Date");
		lblDueDate.setBounds(370, 68, 56, 14);
		getContentPane().add(lblDueDate);
		
		DueDateTxt = new JTextField();
		DueDateTxt.setBounds(420, 65, 138, 24);
		getContentPane().add(DueDateTxt);
		DueDateTxt.setColumns(10);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(37, 138, 551, 192);
		getContentPane().add(tabbedPane);
		
		MajorTasksTable = GetMajorTasksTable(-1);
		major = new JScrollPane();
		tabbedPane.addTab("Major Task", null, major, null);
		major.setViewportView(MajorTasksTable);
		
		
		//DeliverablesTable = new JTable();
		DeliverablesTable = GetDeliverablesTable(-1);
		deliverScroll = new JScrollPane();
		tabbedPane.addTab("Deliverables", null, deliverScroll, null);
		deliverScroll.setViewportView(DeliverablesTable);
		
		JButton ProjectInfoButton = new JButton("Submit Project Info");
		
		ProjectInfoButton.setBounds(250, 104, 138, 23);
		getContentPane().add(ProjectInfoButton);
		
		AddMajorTaskButton AddMajorTaskbtn = new AddMajorTaskButton(connection , ProjectID , startdate , duedate);
		AddMajorTaskbtn.setLocation(37, 341);
		getContentPane().add(AddMajorTaskbtn);

		AddDeliverablesButton addDeliverabltBTN = new AddDeliverablesButton(connection , ProjectID );
		addDeliverabltBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		addDeliverabltBTN.setLocation(185, 341);
		getContentPane().add(addDeliverabltBTN);

		JButton btnShowChart = new JButton("Show Estimated project chart");
		btnShowChart.setEnabled(true);
		btnShowChart.setBounds(335, 341, 200, 27);
		getContentPane().add(btnShowChart);
		btnShowChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					getProjectChart ProjectChart = new getProjectChart();
					ProjectChart.getEstimatedChart(con, ProjectID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		ProjectInfoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String PMName = ProjectNameTxt.getText();
				int Cost = Integer.parseInt(CostTxt.getText());
				String startD = StartDateTxt.getText();
				String endD = DueDateTxt.getText();
				
				try {
					if(ValidProjectInfo(Cost , startD , endD).equals("valid"))
					{
						String insertSql = "INSERT INTO Projects ( PMName, TotalCost, dueDate, StartDate, PlansPlanID) " + 
								"VALUES" + " ('" + PMName + "'," + Cost +",'" + startD + "','"+ endD+"'," +AddPlanButton.PlanID+ ");"; 
						
						ResultSet resultSet = null;

						try (
							PreparedStatement prepsInsertProduct = 
							connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) 
						{
								prepsInsertProduct.execute();
					            // Retrieve the generated key from the insert.
					            resultSet = prepsInsertProduct.getGeneratedKeys();

					            // Print the ID of the inserted row.
					            while (resultSet.next()) {
					            	ProjectID = Integer.parseInt(resultSet.getString(1));
					                System.out.println("Generated: " + ProjectID);
					            }
					            AddMajorTaskbtn.isEditable = true;
					            addDeliverabltBTN.isEditable = true;
					            
					            submittedTable = true;
					            MainFrame.refreshTables();
					            AddMajorTaskbtn.ProStart = startdate;
					            AddMajorTaskbtn.ProEnd = duedate;
					            AddMajorTaskbtn.ProjectID = ProjectID;
					           
					            addDeliverabltBTN.projectID = ProjectID;
					            					            
						} catch (SQLException e1) {
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
		
		setSize(800, 480);
		setVisible(true);	
	}
	
	String MessageError = "invalid";
	private String ValidProjectInfo(int cost, String startD, String endD) throws ParseException {
		// TODO Auto-generated method stub
		String isValid = "valid";
		if(cost<=0) {
			MessageError = "You can not have cost that is les than or equal 0";
			return MessageError;
		}
		startdate = format.parse(startD);
		if(startdate.before(CurrentDate))
		{
			MessageError = ("Error: invalid Start Date, you need to write date after : " + timeStamp);
			return MessageError;
		}
		duedate = format.parse(endD);
		if(duedate.before(CurrentDate) || duedate.before(startdate))
		{
			MessageError =("Error: invalid Date, You need to write date that it beween : " + timeStamp + " and " + startDate);
			return MessageError;
		}
		
		return isValid;
	}
	
	static JTable GetDeliverablesTable(int ProjectID) throws SQLException
	{
		JTable table  = new JTable();
		ResultSet resultSet = null;
		String sql = "select * from  [dbo].[Deliverables]\r\n" + 
				"where [DeliverableID] IN \r\n" + 
				"(\r\n" + 
				"Select [DeliverableID]  from [dbo].[Deliverables] , [dbo].[Projects]\r\n" + 
				"where [dbo].[Deliverables].[ProjectID] = " + ProjectID +" \r\n" + 
				")";
		
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


	static JTable GetMajorTasksTable(int ProjectID) throws SQLException
	{
		JTable table  = new JTable();
		ResultSet resultSet = null;
		String sql = "select MajorTaskID, startTime, dueDate, duration, isMilestone, ActualWorkingHours, ProjectID, Name from [dbo].MajorTasks " + 
				"where [MajorTaskID] IN \r\n" + 
				"(\r\n" + 
				"Select [MajorTaskID] from [dbo].[MajorTasks] , [dbo].[Projects]\r\n" + 
				"where [dbo].[MajorTasks].[ProjectID] = [dbo].[Projects].[ProjectID]\r\n" + 
				"		AND [dbo].[Projects].[ProjectID] = "+ ProjectID +"\r\n" + 
				")";
		
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
		MajorTasksTable = GetMajorTasksTable(ProjectID);
		major.setViewportView(MajorTasksTable);

		DeliverablesTable = GetDeliverablesTable(ProjectID);
		deliverScroll.setViewportView(DeliverablesTable);
        MainFrame.refreshTables();

		
	}
	
}

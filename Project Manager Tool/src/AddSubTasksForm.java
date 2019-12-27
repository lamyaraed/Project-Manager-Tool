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

public class AddSubTasksForm extends JFrame {
	
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	
	//private JTable SubTasksTable;
	private JTextField TaskStartDTxt;
	private JTextField TaskDueDTxt;
	private JTextField TaskDurationDTxt;
	Connection conn;
	
	private Date startTime;
	private Date dueDate;
	private Date CurrentDate;
	boolean submittedTable = false;
	
	public AddSubTasksForm(Connection connection, int majorTaskID, Date majorTaskStartdate, Date majorTaskDuedate) throws ParseException, SQLException {
		
		CurrentDate = format.parse(timeStamp);
		
		getContentPane().setLayout(null);
		conn = connection;
			
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
		
		JButton btnSubmitSubTasks = new JButton("Submit Sub Task Info");
		btnSubmitSubTasks.setBounds(417, 63, 167, 31);
		getContentPane().add(btnSubmitSubTasks);
		
		btnSubmitSubTasks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String StartD = TaskStartDTxt.getText();
				String DueD   = TaskDueDTxt.getText();
				String Name = nameTextField.getText();
				int Duration = Integer.parseInt(TaskDurationDTxt.getText());

				try {
					if(Validate(StartD,DueD,Duration,majorTaskStartdate, majorTaskDuedate).equals("valid"))
					{
						String insertSql = "INSERT INTO SubTasks (startTime, dueDate, ActualWorkingHours, duration, MajorTaskID, Name) " + 
								"VALUES "+ 
								"('"+StartD+"','" +DueD+"'," +0+","+Duration +"," + majorTaskID+",'"+Name+"');";
						
						
					    ResultSet resultSet = null;

						try (
							PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) {
								prepsInsertProduct.execute();
					            // Retrieve the generated key from the insert.
					            resultSet = prepsInsertProduct.getGeneratedKeys();

					            // Print the ID of the inserted row.
					            int TaskID = 0 ;
					            while (resultSet.next()) {
					                TaskID = Integer.parseInt(resultSet.getString(1));
					            	System.out.println("Generated: " + TaskID);
					            }
					            AddMajorTaskForm.RefreshTables();
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
	
		setSize(700, 200);
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
			MessageError = "invalid Start Date, you need to write date after : " + projectStartdate;
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

	
}

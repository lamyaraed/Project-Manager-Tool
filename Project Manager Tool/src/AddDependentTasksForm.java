import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JButton;

public class AddDependentTasksForm extends JFrame {
	
	static ArrayList<Integer> taskIDs = new ArrayList<Integer>();
	
	static int majorTaskID ; 
	static int projectID;
	private JTextField DependentID;
	static JTable majorTasksTable = new JTable();
	static JScrollPane majorTaskstab = new JScrollPane();
	static Connection connection;
	
	public AddDependentTasksForm(Connection connection, int majorTaskID, int projectID) throws SQLException {
		
		getContentPane().setLayout(null);
		System.out.println();
		this.connection =  connection;
		//this.majorTaskID = majorTaskID;
		this.projectID = projectID;
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 556, 168);
		getContentPane().add(tabbedPane);
		
		majorTaskstab = new JScrollPane();
		tabbedPane.addTab("tasks", null, majorTaskstab, null);
		majorTasksTable = GetTasksTable(majorTaskID ,projectID);
		majorTaskstab.setViewportView(majorTasksTable);
		
		JLabel lblNewLabel = new JLabel("Please enter the ID of the Tasks this major task Depends on :");
		lblNewLabel.setBounds(20, 190, 350, 14);
		getContentPane().add(lblNewLabel);
		
		DependentID = new JTextField();
		DependentID.setBounds(480, 190, 86, 20);
		getContentPane().add(DependentID);
		DependentID.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(10, 227, 89, 23);
		getContentPane().add(btnSubmit);
		
		JButton btnCancel = new JButton("cancel");
		btnCancel.setBounds(477, 221, 89, 23);
		getContentPane().add(btnCancel);
		
		
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		                  
		        int id;
		        boolean passed = false;			        	
			    id = Integer.parseInt(DependentID.getText());
			    
	        	for(int j = 0 ; j < taskIDs.size();j++) {//looping on array to make sure choosen id is present in DB
	        		Integer num = taskIDs.get(j);
	        		if(num.equals(id)) {
	        			passed = true;
	        			taskIDs.remove(num);
	        		}
	        	}
	        	if(!passed)
					JOptionPane.showMessageDialog(new JFrame(), "wrong input ,please enter ID from list", "Error", JOptionPane.ERROR_MESSAGE);
	        	else {	
			        String insertSQL  =  "INSERT INTO MajorTasks_dependentTasks(MajorTaskID,DependentTaskID) VALUES ("+ majorTaskID+","+id+");";
			       
			        try (PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);) {
			    		prepsInsertProduct.execute();
			    		ResultSet resultSet = prepsInsertProduct.getGeneratedKeys();

				            // Print the ID of the inserted row.
				            while (resultSet.next()) {
				            	int ID = Integer.parseInt(resultSet.getString(1));
				            	System.out.println("Generated: " + ID);
				            }
			        } catch (SQLException e1) {
						// TODO Auto-generated catch block
			        	JOptionPane.showMessageDialog(new JFrame() ,e1.getMessage() , "Error", JOptionPane.ERROR_MESSAGE);		
						e1.printStackTrace();
					}
	        	}
			
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});
		
		setSize(600, 300);
		setVisible(true);	
	}


	private static JTable GetTasksTable(int majorTaskID, int projectID) throws SQLException {
		// TODO Auto-generated method stub
		JTable table  = new JTable();
		taskIDs = new ArrayList<Integer>();
		ResultSet resultSet = null;
		System.out.println(majorTaskID + " "+projectID );
		String sql = "select MajorTaskID, startTime, dueDate, duration, isMilestone, ActualWorkingHours, ProjectID, Name from [dbo].MajorTasks " + 
				"where MajorTaskID != " + majorTaskID + " and ProjectID = " + projectID;
		
		try (
				PreparedStatement prepsInsertProduct = 
						connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) 
			{
				prepsInsertProduct.execute();
				resultSet = prepsInsertProduct.getResultSet();

			    // It creates and displays the table
			    table = new JTable(buildTableModel(resultSet));
			//	resultSet = prepsInsertProduct.getResultSet();

			}
		//table.setShowGrid(false);
		
		return table;
		}


	private static TableModel buildTableModel(ResultSet resultSet) throws SQLException {

	    ResultSetMetaData metaData = resultSet.getMetaData();
	    taskIDs = new ArrayList<Integer>();
	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (resultSet.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(resultSet.getObject(columnIndex));
	        }
	        data.add(vector);
	        int Major  = resultSet.getInt("MajorTaskID");
            taskIDs.add(Major);// a list for validation in next block of code
           System.out.println(Major);
	    }

	    return new DefaultTableModel(data, columnNames);
	}
	public static void RefreshTables() throws SQLException
	{
		majorTasksTable = GetTasksTable(majorTaskID, projectID);
		majorTaskstab.setViewportView(majorTasksTable);

		
	}
}

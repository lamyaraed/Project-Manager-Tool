import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class addMemberToTaskForm extends JFrame{
	static Connection connection;
	static int majorTaskID = 0;
	static JScrollPane memberTab;
	static JTable memberTable;
	private static ArrayList<Integer> memberIDs;
	JTextField memberID;
	
	public addMemberToTaskForm(Connection conn, int taskID) throws SQLException {
		// TODO Auto-generated constructor stub
		getContentPane().setLayout(null);
		System.out.println();
		this.connection =  conn;
		this.majorTaskID = taskID;
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 556, 168);
		getContentPane().add(tabbedPane);
		
		memberTab = new JScrollPane();
		tabbedPane.addTab("members", null, memberTab, null);
		memberTable = GetmembersTable();
		memberTab.setViewportView(memberTable);
		
		JLabel lblNewLabel = new JLabel("Please enter the ID of the memebers who will work on this Task :");
		lblNewLabel.setBounds(20, 190, 350, 14);
		getContentPane().add(lblNewLabel);
		
		memberID = new JTextField();
		memberID.setBounds(480, 190, 86, 20);
		getContentPane().add(memberID);
		memberID.setColumns(10);
		
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
			    id = Integer.parseInt(memberID.getText());
			    
	        	for(int j = 0 ; j < memberIDs.size();j++) {//looping on array to make sure choosen id is present in DB
	        		Integer num = memberIDs.get(j);
	        		if(num.equals(id)) {
	        			passed = true;
	        			memberIDs.remove(num);
	        		}
	        	}
	        	if(!passed)
					JOptionPane.showMessageDialog(new JFrame(), "wrong input ,please enter ID from list", "Error", JOptionPane.ERROR_MESSAGE);
	        	else {	
	        		String insertSql = "INSERT INTO Task_TeamMember(TaskID, [Team MemberID])" + 
		        			"VALUES ("+taskID+","+id+");";
			       
			        try (PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) {
			    		prepsInsertProduct.execute();

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


	private static JTable GetmembersTable() throws SQLException {
		// TODO Auto-generated method stub
		JTable table  = new JTable();
		ResultSet resultSet = null;
		System.out.println(majorTaskID + " in class member to task");
		String sql =  "SELECT MemberID, Name , Title" + 
                " FROM TeamMembers;";
		
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
	    memberIDs = new ArrayList<Integer>();
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
	        int memberID  = resultSet.getInt("MemberID");
	        memberIDs.add(memberID);// a list for validation in next block of code
           System.out.println(memberID);
	    }

	    return new DefaultTableModel(data, columnNames);
	}
	public static void RefreshTables() throws SQLException
	{
		memberTable= GetmembersTable();
		memberTab.setViewportView(memberTable);
		
	}

	/*
	 		

	        int id;
	        boolean passed = false;
	        for(int i = 0 ; i < n ;i++) {
		        do {
		        	
		        	scanner = new Scanner(System.in);
		        	id = scanner.nextInt();
		        	
		        	for(int j = 0 ; j < memberIDs.size();j++) {
		        		Integer num = memberIDs.get(j);
		        		if(num.equals(id)) {
		        			passed = true;
		        			memberIDs.remove(num);
		        		}
		        	
		        	}
		        	
		        	if(!passed)
		        		System.out.println("wrong input , you either have already added this person or this person ID doesnt exist,\nplease enter ID from list");
		        	
		        }while(!passed);
		        String insertSql = "INSERT INTO Task_TeamMember(TaskID, [Team MemberID])" + 
	        			"VALUES ("+taskID+","+id+");";
		       
		        try (PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) {
		    		prepsInsertProduct.execute();
		        }
		        passed = false;
	        }
	 */
}

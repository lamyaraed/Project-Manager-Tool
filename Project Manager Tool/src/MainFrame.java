import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class MainFrame extends JFrame 
{
	static JTable Projects = new JTable();
	static JTable Members = new JTable();
	static JTable Plans = new JTable();
	static JScrollPane ProjectTab = new JScrollPane();
	static JScrollPane TeamMembersTab = new JScrollPane();
	static JScrollPane PlansTab = new JScrollPane();
	static String sql;
	static Connection con;
//	String connectionUrl = ProjectManagerTool.connectionUrl;
	
	public MainFrame(Connection connection) throws SQLException {
		setTitle("Project Manager Tool");
		getContentPane().setLayout(null);
		con = connection;
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 35, 623, 273);
		getContentPane().add(tabbedPane);
		
		//******************************************
		PlansTab = new JScrollPane();
		tabbedPane.addTab("Plans", null, PlansTab, null);
		
		Plans = GetPlansTable();
		PlansTab.setViewportView(Plans);
		//*********************************************
		ProjectTab = new JScrollPane();
		tabbedPane.addTab("Projects", null, ProjectTab, null);
		
		Projects = GetProjectsTable();
		ProjectTab.setViewportView(Projects);
		
		//********************************************
		TeamMembersTab = new JScrollPane();
		tabbedPane.addTab("Memebers", null, TeamMembersTab, null);
		
		Members = GetMembersTable();
		TeamMembersTab.setViewportView(Members);
		///*********************************************
		
		AddProjectButton AddProjectBtn =  new AddProjectButton(connection);
		AddProjectBtn.setBounds(247, 336, 111, 30);
		getContentPane().add(AddProjectBtn);
		
		AddPlanButton btnAddPlan = new AddPlanButton(connection);
		btnAddPlan.setLocation(120, 336);
	
		getContentPane().add(btnAddPlan);
		
		AddMembersButton btnAddMembers = new AddMembersButton(connection);
		btnAddMembers.setLocation(391, 336);
		getContentPane().add(btnAddMembers);
		
		JLabel lblNewLabel = new JLabel("Record the actual woking hours for completed major tasks for a specific task :");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(50, 388, 470, 27);
		getContentPane().add(lblNewLabel);
		
		recordActWorkHrsButton btnRecord = new recordActWorkHrsButton(connection);
		btnRecord.setText("Update");
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnRecord.setBounds(500, 388, 111, 26);
		getContentPane().add(btnRecord);
		
		System.out.println("Connected");
			
		setSize(800, 480);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	}

	
	
	private static JTable GetMembersTable() throws SQLException {
		// TODO Auto-generated method stub
		JTable table  = new JTable();
		ResultSet resultSet = null;
		sql = "SELECT * \r\n" + 
				"  FROM TeamMembers;";
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
	
	private static JTable GetProjectsTable() throws SQLException {
		// TODO Auto-generated method stub
		JTable table  = new JTable();
		ResultSet resultSet = null;
		sql = "SELECT * \r\n" + 
				"  FROM Projects;";
		
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
	
	private static JTable GetPlansTable() throws SQLException
	{
		JTable table  = new JTable();
		ResultSet resultSet = null;
		sql = "SELECT * \r\n" + 
				"  FROM Plans;";
	
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
	
	public static void refreshTables() throws SQLException
	{
		Projects = GetProjectsTable();
		Members = GetMembersTable();
		Plans = GetPlansTable();
		
		ProjectTab.setViewportView(Projects);
		TeamMembersTab.setViewportView(Members);
		PlansTab.setViewportView(Plans);
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
}

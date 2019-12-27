import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProjectManagerTool 
{

	//ArrayList<Projects> projects = new ArrayList<Projects>();
	//ArrayList<Plans> plans = new ArrayList<Plans>();
	Scanner scanner= new Scanner(System.in);
	
	
	/*public void addNewProject(Connection connection) throws ParseException, SQLException
	{
		Projects project = new Projects();	
		int projectID = project.createProject(connection);
	}*/

	
	public void showAllMajorTasks(Connection connection) throws SQLException
	{
		String showSql = "SELECT MajorTaskID, startTime, dueDate, duration, isMilestone, ActualWorkingHours, ProjectID, PlanID \r\n" + 
				"  FROM MajorTasks;\r\n" + 
				"";
		ResultSet resultSet = null;

		try (
			PreparedStatement prepsInsertProduct = 
			connection.prepareStatement(showSql, Statement.RETURN_GENERATED_KEYS);) 
		{
			prepsInsertProduct.execute();
			resultSet = prepsInsertProduct.getResultSet();
			System.out.println("ID" + " " + "ActualWorkingHours");
			 while(resultSet.next()){
		         //Retrieve by column name
		         int MajorTaskID  = resultSet.getInt("MajorTaskID");
		         String ActualWorkingHours = resultSet.getString("ActualWorkingHours");
		         System.out.println(MajorTaskID + "        " + ActualWorkingHours);
			 }
		}
	}
	
	//public static final String  connectionUrl ="jdbc:sqlserver://DESKTOP-R2QBSGS:1433;databaseName=ProjectManagerTool;integratedSecurity=true;";
	public static final String  connectionUrl ="jdbc:sqlserver://LAPTOP-LCQG7EAK:1433;databaseName=ProjectManagementTool;integratedSecurity=true;";

	public void updateMajorTaks(Connection connection) throws SQLException
	{
		showAllMajorTasks(connection);
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("write number of completed Major tasks : ");
		int n = scanner.nextInt();
		
		for(int i = 0 ; i < n ; i++)
		{
			System.out.println( (i+1)+ "\nWrite the id : ");
			int id = scanner.nextInt();
			
			int workhrs = 0;
			
			do {
			System.out.println("Actual Warking Hours : ");
			workhrs = scanner.nextInt();
			}while(workhrs < 0);
			
			String updateSql = "UPDATE MajorTasks SET ActualWorkingHours = " + workhrs + " WHERE MajorTaskID = " + id + ";";

			try (
				PreparedStatement prepsInsertProduct = 
				connection.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS);) 
			{
				prepsInsertProduct.execute();
				System.out.println("Updated");
			}
			
			showAllMajorTasks(connection);
			
		}	
	}
	
}

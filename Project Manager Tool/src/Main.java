import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;

public class Main {

	public static final String  connectionUrl ="jdbc:sqlserver://LAPTOP-5H7DPM6M:1433;databaseName=ProjectManagementTool;integratedSecurity=true;";
//	public static final String  connectionUrl ="jdbc:sqlserver://DESKTOP-R2QBSGS:1433;databaseName=ManagementTool;integratedSecurity=true;";
//	public static final String  connectionUrl ="jdbc:sqlserver://LAPTOP-LCQG7EAK:1433;databaseName=ProjectManagementTool;integratedSecurity=true;";

	public static void main(String[] args) throws ParseException, SQLException {
		
		try {
		// TODO dependent tasks and subtasks
			Connection connection;
			connection = DriverManager.getConnection(connectionUrl);
			new MainFrame(connection);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

}

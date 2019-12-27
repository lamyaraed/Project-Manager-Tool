import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JButton;

public class AddProjectButton extends JButton 
{
	Connection connection;
	public AddProjectButton(Connection connection) {
		// TODO Auto-generated constructor stub
		this.connection = connection;
		setText("Add Project");
		setBounds(160, 336, 111, 30);
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					new AddProjectForm(connection);
					
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
}

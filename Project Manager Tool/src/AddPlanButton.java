import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JButton;

public class AddPlanButton extends JButton {

	public static int PlanID;
	
	Connection connection;
	public AddPlanButton(Connection connection) 
	{
		// TODO Auto-generated constructor stub
		this.connection = connection;
		setText("Add Plan");
		setBounds(33, 336, 102, 30);
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddPlanForm(connection);
			}
		});
	}
}

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JButton;

public class recordActWorkHrsButton extends JButton {

	Connection connection;
	
	public recordActWorkHrsButton(Connection connection) {
		// TODO Auto-generated constructor stub
		this.connection = connection;
		setText("Actual Working Hours : ");
		setBounds(160, 336, 111, 30);
		
		addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new UpdateWorkingHoursForm(connection);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

}

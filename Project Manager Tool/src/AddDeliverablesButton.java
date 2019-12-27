import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AddDeliverablesButton extends JButton {
	
	public boolean isEditable = false;
	int projectID; 
	
	public AddDeliverablesButton(Connection connection, int ProjectID ) {
		// TODO Auto-generated constructor stub
		setBounds(37, 343, 138, 27);
		setText("Add Deliverable");
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					if(isEditable)
						new AddDeliverablesForm(connection , projectID);
					else
					{
						JOptionPane.showMessageDialog(new JFrame() , "There is no Project to add Major tasks to" , "Error", JOptionPane.ERROR_MESSAGE);		
						
					}
				} catch (NumberFormatException | HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
	}


}

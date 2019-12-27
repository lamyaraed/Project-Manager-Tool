import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AddMajorTaskButton extends JButton {
	
	public boolean isEditable = false;
	Date ProStart;
	Date ProEnd;
	int ProjectID;
	
	public AddMajorTaskButton(Connection connection, int projectID, Date startdate, Date duedate ) {
		// TODO Auto-generated constructor stub
		setBounds(37, 343, 138, 27);
		setText("Add Major Task");
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					if(isEditable)
						new AddMajorTaskForm(connection , ProjectID , ProStart , ProEnd);
					else
					{
						JOptionPane.showMessageDialog(new JFrame() , "There is no Project to add Major tasks to" , "Error", JOptionPane.ERROR_MESSAGE);		
						
					}
				} catch (ParseException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}


}

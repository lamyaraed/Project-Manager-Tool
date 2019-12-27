import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class addSubTaskButton extends JButton {
	
	public boolean isEditable = false;
	Date MajorTaskStart;
	Date MajorTaskEnd;
	int MajorTaskID;
	
	public addSubTaskButton(Connection connection, int projectID, Date startdate, Date duedate ) {
		// TODO Auto-generated constructor stub
		setBounds(37, 378, 145, 32);
		setText("Add Sub-Task");
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					if(isEditable)
						new AddSubTasksForm(connection , MajorTaskID , MajorTaskStart , MajorTaskEnd);
					else
					{
						JOptionPane.showMessageDialog(new JFrame() , "There is no Major Task to add Sub tasks to" , "Error", JOptionPane.ERROR_MESSAGE);		
						
					}
				} catch (ParseException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}


}

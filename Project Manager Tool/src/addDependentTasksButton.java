import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class addDependentTasksButton extends JButton {
	
	public boolean isEditable = false;
	private Connection connection;
	protected int MajorTaskID;
	
	public addDependentTasksButton(Connection connection, int majorTaskID, int projectID ) {
		// TODO Auto-generated constructor stub
	
		this.connection = connection;
		setText("add Dependent Tasks");
		setBounds(192, 378, 120, 30);
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(isEditable)
						new AddDependentTasksForm(connection, MajorTaskID, projectID );
					else
					{
						JOptionPane.showMessageDialog(new JFrame() , "There is no Major Tasks to Depend on" , "Error", JOptionPane.ERROR_MESSAGE);		
						
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
}

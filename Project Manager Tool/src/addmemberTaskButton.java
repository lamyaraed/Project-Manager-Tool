import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class addmemberTaskButton extends JButton{
	boolean isEditable =false;
	protected int majorTaskID;
	
	public addmemberTaskButton(Connection conn , int TaskID){
		setText("Add Team Memeber");
		setBounds(322, 378, 145, 30);
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					if(isEditable)
						new addMemberToTaskForm(conn, majorTaskID );
					else
					{
						JOptionPane.showMessageDialog(new JFrame() , "There is no Major Tasks to add members to" , "Error", JOptionPane.ERROR_MESSAGE);		
						
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
}

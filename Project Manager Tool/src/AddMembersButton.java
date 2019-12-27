import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;

public class AddMembersButton extends JButton {

	public static int MemberID;
	private Connection connection;

	public AddMembersButton(Connection connection) {
		// TODO Auto-generated constructor stub
	
		this.connection = connection;
		setText("Add Member");
		setBounds(304, 336, 117, 27);
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddMemberForm(connection);
			}
		});
	}

}

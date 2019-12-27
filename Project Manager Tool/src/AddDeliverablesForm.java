import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class AddDeliverablesForm extends JFrame{
	
	public AddDeliverablesForm(Connection connection, int projectID ) {
		getContentPane().setLayout(null);
		
		JLabel lblDeliverableDescription = new JLabel("Deliverable Description :");
		lblDeliverableDescription.setBounds(42, 55, 154, 14);
		getContentPane().add(lblDeliverableDescription);
		
		JButton btnAddDeliverable = new JButton("Add Deliverable");
		btnAddDeliverable.setBounds(289, 209, 107, 23);
		getContentPane().add(btnAddDeliverable);
		
		JTextPane textPane = new JTextPane();
		textPane.setBounds(52, 80, 344, 102);
		getContentPane().add(textPane);
		
		btnAddDeliverable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String description = textPane.getText();
				
				try {
					String insertSql = "INSERT INTO Deliverables(description,  ProjectID) " + 
							"VALUES ('" + description + "' ," + projectID +");";
						
					    ResultSet resultSet = null;

						try (
							PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) {
								prepsInsertProduct.execute();
					            // Retrieve the generated key from the insert.
					            resultSet = prepsInsertProduct.getGeneratedKeys();

					            // Print the ID of the inserted row.
					            int DeliverableID = 0 ;
					            while (resultSet.next()) {
					            	DeliverableID = Integer.parseInt(resultSet.getString(1));
					            	System.out.println("Generated: " + DeliverableID);
					            }
					            AddProjectForm.RefreshTables();
						} 
						catch (SQLException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(new JFrame() ,e1.getMessage() , "Error", JOptionPane.ERROR_MESSAGE);		
							e1.printStackTrace();
						}
				}
				catch (NumberFormatException | HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
		});
		setSize(450, 300);
		setVisible(true);

	}
}

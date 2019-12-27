import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddMemberForm extends JFrame 
{
	private JTextField NameTxt;
	private JTextField TitleTxt;
	private JTextField SalaryTxt;
	private JTextField workHrsTxt;
	protected int memberID;
	
	public AddMemberForm(Connection connection) {
		setTitle("Add Member");
		getContentPane().setLayout(null);
		
		NameTxt = new JTextField();
		NameTxt.setBounds(81, 31, 103, 20);
		getContentPane().add(NameTxt);
		NameTxt.setColumns(10);
		
		TitleTxt = new JTextField();
		TitleTxt.setBounds(81, 69, 103, 20);
		getContentPane().add(TitleTxt);
		TitleTxt.setColumns(10);
		
		SalaryTxt = new JTextField();
		SalaryTxt.setBounds(299, 31, 103, 20);
		getContentPane().add(SalaryTxt);
		SalaryTxt.setColumns(10);
		
		workHrsTxt = new JTextField();
		workHrsTxt.setBounds(299, 69, 103, 20);
		getContentPane().add(workHrsTxt);
		workHrsTxt.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(25, 34, 46, 14);
		getContentPane().add(lblName);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTitle.setBounds(25, 72, 46, 14);
		getContentPane().add(lblTitle);
		
		JLabel lblSalary = new JLabel("Salary");
		lblSalary.setBounds(243, 34, 46, 14);
		getContentPane().add(lblSalary);
		
		JLabel lblWorkingHours = new JLabel("Working hours");
		lblWorkingHours.setBounds(209, 72, 80, 14);
		getContentPane().add(lblWorkingHours);
		
		JButton btnSubmit = new JButton("submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Name = NameTxt.getText();
				String Title = TitleTxt.getText();
				int Wrkhrs = Integer.parseInt(workHrsTxt.getText());
				int salary = Integer.parseInt(SalaryTxt.getText());
				
				if(salary>0 && Wrkhrs >0)
				{
					String insertSql = "INSERT INTO TeamMembers(Title,WorkingHours,Cost,Name) " + 
							"VALUES ('"+Title +"',"+ Wrkhrs +"," +salary + ",'"+Name +"');";
					
					ResultSet resultSet = null;
					try (
						PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) {
							prepsInsertProduct.execute();
				            // Retrieve the generated key from the insert.
				            resultSet = prepsInsertProduct.getGeneratedKeys();

				            // Print the ID of the inserted row.
				            memberID = 0; 
				            while (resultSet.next()) {
				            	memberID = Integer.parseInt(resultSet.getString(1));
				            	System.out.println("Generated: " + memberID);
				            }
				            AddMembersButton.MemberID = memberID;
				            MainFrame.refreshTables();
			        }catch (SQLException e1) {
						// TODO Auto-generated catch block
			        	JOptionPane.showMessageDialog(new JFrame(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(new JFrame(), "Invalid Member Info", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		
		btnSubmit.setBounds(170, 146, 89, 23);
		getContentPane().add(btnSubmit);
		
		setSize(450, 300);
		setVisible(true);
	}

}

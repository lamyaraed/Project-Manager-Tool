import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddPlanForm extends JFrame 
{
	
	private JTextField WorkinghrsTxt;
	int PlanID;
	public AddPlanForm(Connection connection){
	
		setTitle("Add Plan");
		getContentPane().setLayout(null);
		
		WorkinghrsTxt = new JTextField();
		WorkinghrsTxt.setBounds(152, 33, 113, 26);
		getContentPane().add(WorkinghrsTxt);
		WorkinghrsTxt.setColumns(10);
		
		JLabel lblNumberOfWorking = new JLabel("Number of working hours");
		lblNumberOfWorking.setBounds(20, 39, 122, 14);
		getContentPane().add(lblNumberOfWorking);
		
		JLabel lblStartDay = new JLabel("Start Day :");
		lblStartDay.setBounds(298, 39, 65, 14);
		getContentPane().add(lblStartDay);
		
		JRadioButton rdbtnSunday = new JRadioButton("Sunday");
		rdbtnSunday.setBounds(358, 30, 109, 23);
		getContentPane().add(rdbtnSunday);
		
		JRadioButton rdbtnSaterday = new JRadioButton("Saturday");
		rdbtnSaterday.setBounds(358, 60, 109, 23);
		getContentPane().add(rdbtnSaterday);
		
		JButton btnSubmitPlanInfo = new JButton("Submit Plan Info");
		btnSubmitPlanInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				int nWarkingHours =Integer.parseInt(WorkinghrsTxt.getText());
				String startDay = rdbtnSunday.isSelected()?"Sunday":"Saturday";
				if(nWarkingHours > 0 && nWarkingHours <24 && !(rdbtnSaterday.isSelected() && rdbtnSunday.isSelected())
						&& !(!rdbtnSaterday.isSelected() && !rdbtnSunday.isSelected()))
				{
					String insertSql = "INSERT INTO Plans(nWarkingHours,  StartDay)" + 
							"VALUES (" + nWarkingHours +",'" + startDay  + "' );";
				  
					ResultSet resultSet = null;
					try (
						PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) {
							prepsInsertProduct.execute();
				            // Retrieve the generated key from the insert.
				            resultSet = prepsInsertProduct.getGeneratedKeys();

				            // Print the ID of the inserted row.
				            PlanID = 0; 
				            while (resultSet.next()) {
				            	PlanID = Integer.parseInt(resultSet.getString(1));
				            	System.out.println("Generated: " + PlanID);
				            }
				            AddPlanButton.PlanID = PlanID;
				            MainFrame.refreshTables();
				           // setVisible(false);	
			        } catch (SQLException e1) {
						// TODO Auto-generated catch block
			        	JOptionPane.showMessageDialog(new JFrame(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(new JFrame(), "Invalid Plan Info", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSubmitPlanInfo.setBounds(212, 100, 128, 26);
		getContentPane().add(btnSubmitPlanInfo);
	
		setSize(550, 215);
		setVisible(true);	
	}
}

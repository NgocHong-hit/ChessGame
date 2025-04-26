package Client_GUI;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;

public class Update extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtUsername;
	private JTextField txtLastName;
	private JPasswordField passwordField;
	private JTextField txtFirstName;

	/**
	 * Create the panel.
	 */
	public Update(Frame frame) {
		setBackground(new Color(255, 255, 255));
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Update.class.getResource("/Client_GUI/Icon/DoHoa.png")));
		lblNewLabel.setBounds(166, 21, 550, 265);
		add(lblNewLabel);
		
		JLabel lblUpdateInformation = new JLabel("UPDATE INFORMATION ");
		lblUpdateInformation.setForeground(new Color(196, 157, 68));
		lblUpdateInformation.setFont(new Font("Tahoma", Font.BOLD, 36));
		lblUpdateInformation.setBounds(204, 296, 480, 38);
		add(lblUpdateInformation);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setForeground(new Color(232, 186, 81));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_1.setBounds(166, 360, 108, 28);
		add(lblNewLabel_1);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtUsername.setColumns(10);
		txtUsername.setBounds(96, 398, 225, 38);
		add(txtUsername);
		
		JLabel lblNewLabel_1_2 = new JLabel("Last Name");
		lblNewLabel_1_2.setForeground(new Color(232, 186, 81));
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_1_2.setBounds(517, 360, 95, 28);
		add(lblNewLabel_1_2);
		
		txtLastName = new JTextField();
		txtLastName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtLastName.setColumns(10);
		txtLastName.setBounds(460, 398, 225, 38);
		add(txtLastName);
		
		JLabel lblNewLabel_1_1 = new JLabel("Password");
		lblNewLabel_1_1.setForeground(new Color(232, 186, 81));
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_1_1.setBounds(166, 446, 108, 28);
		add(lblNewLabel_1_1);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordField.setColumns(10);
		passwordField.setBounds(96, 484, 225, 38);
		add(passwordField);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Gender");
		lblNewLabel_1_1_1.setForeground(new Color(232, 186, 81));
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_1_1_1.setBounds(166, 532, 108, 28);
		add(lblNewLabel_1_1_1);
		
		JComboBox<Object> comboBoxGender = new JComboBox<Object>();
		comboBoxGender = new JComboBox<Object>();
		comboBoxGender.setFont(new Font("Tahoma", Font.PLAIN, 14));
		comboBoxGender.setBackground(new Color(255, 255, 255));
		comboBoxGender.setModel(new DefaultComboBoxModel(new String[] {"--Chọn giới tính--", "Nam", "Nữ", "Khác"}));
		comboBoxGender.setBounds(96, 570, 225, 38);
		comboBoxGender.setSelectedIndex(0);
        add(comboBoxGender);
        
		
		JLabel lblNewLabel_1_1_2 = new JLabel("First Name");
		lblNewLabel_1_1_2.setForeground(new Color(232, 186, 81));
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_1_1_2.setBounds(517, 451, 95, 28);
		add(lblNewLabel_1_1_2);
		
		txtFirstName = new JTextField();
		txtFirstName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtFirstName.setColumns(10);
		txtFirstName.setBounds(459, 484, 225, 38);
		add(txtFirstName);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Date of Birth");
		lblNewLabel_1_1_1_1.setForeground(new Color(232, 186, 81));
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel_1_1_1_1.setBounds(517, 543, 119, 28);
		add(lblNewLabel_1_1_1_1);
		
		JDateChooser dateDateofBrith = new JDateChooser();
		dateDateofBrith.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dateDateofBrith.setBounds(459, 570, 225, 38);
		add(dateDateofBrith);
		
		JButton btnUpdate = new JButton("UPDATE");
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnUpdate.setBackground(new Color(183, 145, 72));
		btnUpdate.setBounds(290, 627, 187, 38);
		add(btnUpdate);
		
		JButton btnExit = new JButton("X");
		btnExit.setForeground(new Color(225, 0, 0));
		btnExit.setFont(new Font("Tahoma", Font.BOLD, 24));
		btnExit.setFocusPainted(false);
		btnExit.setBorderPainted(false);
		btnExit.setBackground(Color.WHITE);
		btnExit.setBounds(750, 0, 57, 45);
		add(btnExit);
      

	}
}

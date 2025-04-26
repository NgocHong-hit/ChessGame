package Client_GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

import javax.swing.*;
import Client_Socket.Client;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
 


public class SignUp extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<Object> comboBox;
    private JTextField txtLastName;
    private JTextField txtFirstName;
    private JDateChooser dateDateofBrith;
    private Client client;

    public SignUp(JFrame frame) {
        setBackground(new Color(255, 255, 255));
        setLayout(null);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(SignUp.class.getResource("/Client_GUI/Icon/DoHoa.png")));
        lblNewLabel.setBounds(258, 0, 550, 265);
        add(lblNewLabel);
        
        JLabel lblSignUp = new JLabel("SIGN UP");
        lblSignUp.setForeground(new Color(196, 157, 68));
        lblSignUp.setFont(new Font("Tahoma", Font.BOLD, 41));
        lblSignUp.setBounds(380, 263, 200, 38);
        add(lblSignUp);
        
        JLabel lblNewLabel_1 = new JLabel("Username");
        lblNewLabel_1.setForeground(new Color(232, 186, 81));
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_1.setBounds(240, 311, 108, 28);
        add(lblNewLabel_1);
        
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtUsername.setColumns(10);
        txtUsername.setBounds(181, 349, 225, 38);
        add(txtUsername);
        
        JLabel lblNewLabel_1_1 = new JLabel("Password");
        lblNewLabel_1_1.setForeground(new Color(232, 186, 81));
        lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_1_1.setBounds(240, 397, 108, 28);
        add(lblNewLabel_1_1);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtPassword.setColumns(10);
        txtPassword.setBounds(181, 435, 225, 38);
        add(txtPassword);
        
        JLabel lblNewLabel_1_2 = new JLabel("Last Name");
        lblNewLabel_1_2.setForeground(new Color(232, 186, 81));
        lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_1_2.setBounds(610, 311, 95, 28);
        add(lblNewLabel_1_2);
        
        txtLastName = new JTextField();
        txtLastName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtLastName.setColumns(10);
        txtLastName.setBounds(559, 349, 225, 38);
        add(txtLastName);
        
        JLabel lblNewLabel_1_1_1 = new JLabel("Gender");
        lblNewLabel_1_1_1.setForeground(new Color(232, 186, 81));
        lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_1_1_1.setBounds(240, 483, 108, 28);
        add(lblNewLabel_1_1_1);
        
        comboBox = new JComboBox<Object>();
        comboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
        comboBox.setBackground(new Color(255, 255, 255));
        comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] {"--Chọn giới tính--", "Nam", "Nữ", "Khác"}));
        comboBox.setBounds(181, 521, 225, 38);
        comboBox.setSelectedIndex(0);
        add(comboBox);
        
        JLabel lblNewLabel_1_1_2 = new JLabel("First Name");
        lblNewLabel_1_1_2.setForeground(new Color(232, 186, 81));
        lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_1_1_2.setBounds(610, 397, 95, 28);
        add(lblNewLabel_1_1_2);
        
        txtFirstName = new JTextField();
        txtFirstName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtFirstName.setColumns(10);
        txtFirstName.setBounds(559, 435, 225, 38);
        add(txtFirstName);
        
        JLabel lblNewLabel_1_1_1_1 = new JLabel("Date of Birth");
        lblNewLabel_1_1_1_1.setForeground(new Color(232, 186, 81));
        lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_1_1_1_1.setBounds(610, 483, 119, 28);
        add(lblNewLabel_1_1_1_1);
        
        dateDateofBrith = new JDateChooser ();
        dateDateofBrith.setFont(new Font("Tahoma", Font.PLAIN, 14));
        dateDateofBrith.setBounds(559, 521, 225, 38);
        add(dateDateofBrith);
        
//        Kết nối đến server khi khởi động
        client = new Client("localhost", 12345);
        client.connectToServer();
        
        
        JButton btnSignUp = new JButton("SIGN UP");
        btnSignUp.setFont(new Font("Tahoma", Font.PLAIN, 20));
        btnSignUp.setBackground(new Color(183, 145, 72));
        btnSignUp.setBounds(380, 625, 187, 38);
        add(btnSignUp);
        btnSignUp.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            String gender = (String) comboBox.getSelectedItem();
            String lastname = txtLastName.getText().trim();
            String firstName = txtFirstName.getText().trim();
            
            if (dateDateofBrith.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sinh.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date_of_birth = sdf.format(dateDateofBrith.getDate());

            // Kiểm tra các trường nhập
            if (username.isEmpty() || password.isEmpty() || lastname.isEmpty() ||
                firstName.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tất cả các trường thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!username.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (gender.equals("--Chọn giới tính--")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn giới tính", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Gửi yêu cầu đăng ký và xử lý phản hồi
            try {
                String response = client.register(username, password, lastname, firstName, gender, date_of_birth);
                if (response.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
//                  // Chuyển sang giao diện đăng nhập sau khi đăng ký thành công
                    frame.setContentPane(new Login(frame));
                    frame.revalidate();
                    frame.repaint();
                } else if (response.startsWith("ERROR")) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + response.split("\\|")[1]);
                } else {
                    JOptionPane.showMessageDialog(this, "Phản hồi không hợp lệ từ server.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi gửi yêu cầu đến server.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        
        
 
        
        JLabel lblNewLabel_2 = new JLabel("Already have an account?");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_2.setBounds(316, 689, 187, 28);
        add(lblNewLabel_2);
        
        JButton btnSignin = new JButton("Sign in");
        btnSignin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnSignin.setOpaque(false);
        btnSignin.setForeground(new Color(183, 145, 72));
        btnSignin.setFont(new Font("Tahoma", Font.PLAIN, 17));
        btnSignin.setFocusPainted(false);
        btnSignin.setContentAreaFilled(false);
        btnSignin.setBorderPainted(false);
        btnSignin.setBackground(Color.WHITE);
        btnSignin.setBounds(487, 692, 93, 21);
        btnSignin.setBorderPainted(false);
        btnSignin.setFocusPainted(false);
        btnSignin.setContentAreaFilled(false);
        btnSignin.setOpaque(false);

        // Hover để thêm gạch chân
        btnSignin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
            	btnSignin.setText("<html><u>Sign In</u></html>");
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	btnSignin.setText("Sign In");
            }
//            Hàm khi nhấn chuột vào thì chuyển qua trang Sign in
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setContentPane(new Login(frame));
                frame.revalidate();
                frame.repaint();
            }
        
        });
        add(btnSignin);
    }
    


	
   
   
}
package Client_GUI;

import javax.swing.*;

import Client_Socket.Client;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private Client client;


    /**
     * Create the panel.
     */
    public Login(JFrame frame) {

        setBackground(new Color(255, 255, 255));
        setLayout(null);
        
        JLabel lblNewLabel_3 = new JLabel("");
        lblNewLabel_3.setIcon(new ImageIcon(Login.class.getResource("/Client_GUI/Icon/DoHoa.png")));
        lblNewLabel_3.setBounds(245, 10, 557, 273);
        add(lblNewLabel_3);
        
        JLabel lblNewLabel = new JLabel("SIGN IN");
        lblNewLabel.setForeground(new Color(196, 157, 68));
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 42));
        lblNewLabel.setBounds(362, 293, 204, 51);
        add(lblNewLabel);
        
        JLabel lblNewLabel_1 = new JLabel("Username");
        lblNewLabel_1.setForeground(new Color(232, 186, 81));
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_1.setBounds(402, 372, 108, 28);
        add(lblNewLabel_1);
        
        txtUser = new JTextField();
        txtUser.setBounds(322, 410, 244, 50);
        add(txtUser);
        txtUser.setColumns(10);
        
        JLabel lblNewLabel_1_1 = new JLabel("Password");
        lblNewLabel_1_1.setForeground(new Color(232, 186, 81));
        lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_1_1.setBounds(402, 470, 119, 28);
        add(lblNewLabel_1_1);
        
        txtPass = new JPasswordField();
        txtPass.setColumns(10);
        txtPass.setBounds(322, 516, 248, 45);
        add(txtPass);
        
        
        JButton bntSingIn = new JButton("SIGN IN");
        bntSingIn.setBackground(new Color(183, 145, 72));
        bntSingIn.setFont(new Font("Tahoma", Font.PLAIN, 20));
        bntSingIn.setBounds(354, 600, 187, 38);
        add(bntSingIn);
        
        JButton btnSignup = new JButton("Sign up");
        btnSignup.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnSignup.setFont(new Font("Tahoma", Font.PLAIN, 17));
        btnSignup.setBackground(new Color(255, 255, 255));
        btnSignup.setForeground(new Color(183, 145, 72));
        btnSignup.setBounds(448, 673, 93, 21);
        bntSingIn.addActionListener(e -> {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.", "Thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String response = client.login(
                    txtUser.getText(),
                    new String(txtPass.getPassword())
                );

                if (response.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");

                    // Tạo đối tượng Play (JFrame) và hiển thị nó
                    Play play = new Play(client, txtUser.getText());
                    play.setVisible(true);  // Hiển thị cửa sổ Play

                    // Nếu muốn đóng cửa sổ đăng nhập hiện tại:
                    frame.dispose();

                } else {
                    JOptionPane.showMessageDialog(this, response.split("\\|")[1]);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }


        });

  


        // Bỏ viền, nền để giống hyperlink
        btnSignup.setBorderPainted(false);
        btnSignup.setFocusPainted(false);
        btnSignup.setContentAreaFilled(false);
        btnSignup.setOpaque(false);

        // Hover để thêm gạch chân
        btnSignup.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSignup.setText("<html><u>Sign Up</u></html>");
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSignup.setText("Sign Up");
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setContentPane(new SignUp(frame));
                frame.revalidate();
                frame.repaint();
            }
        });

        add(btnSignup);
        
        JLabel lblNewLabel_2 = new JLabel("Don't have an account?");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_2.setBounds(300, 670, 160, 28);
        add(lblNewLabel_2);

        
//      Kết nối đến server khi khởi động
      client = new Client("localhost", 12345);
      client.connectToServer();
    }
    

}

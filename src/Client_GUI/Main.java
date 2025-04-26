package Client_GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
        setLocationRelativeTo(null); // căn giữa màn hình
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Main.class.getResource("/Client_GUI/Icon/DoHoa1.png")));
		lblNewLabel.setBounds(265, -15, 542, 387);
		contentPane.add(lblNewLabel);
		
		 JButton bntSignIn = new JButton("SIGN IN");
	     bntSignIn.setForeground(new Color(255, 255, 255));
	     bntSignIn.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
	     bntSignIn.setBackground(new Color(225, 198, 102));
	     bntSignIn.setFocusPainted(false); // Tắt viền khi nhấn
	     bntSignIn.setBounds(390, 478, 233, 59); // Vị trí và kích thước
	     contentPane.add(bntSignIn);
	     bntSignIn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
            	bntSignIn.setBackground(new Color(210, 180, 80));
            }

            public void mouseExited(MouseEvent e) {
            	bntSignIn.setBackground(new Color(225, 198, 102));
            }
        });
	     bntSignIn.addActionListener(e -> {
		    setContentPane(new Login(this));
		    revalidate();
		    repaint();
		});
//	
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(Main.class.getResource("/Client_GUI/Icon/DoHoaVua.png")));
		lblNewLabel_1.setBackground(new Color(255, 255, 255));
		lblNewLabel_1.setBounds(35, 115, 149, 151);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("");
		lblNewLabel_1_1.setIcon(new ImageIcon(Main.class.getResource("/Client_GUI/Icon/DoHoaTot.png")));
		lblNewLabel_1_1.setBackground(Color.WHITE);
		lblNewLabel_1_1.setBounds(817, 94, 120, 183);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("");
		lblNewLabel_1_2.setIcon(new ImageIcon(Main.class.getResource("/Client_GUI/Icon/DoHoaTot1.png")));
		lblNewLabel_1_2.setBackground(Color.WHITE);
		lblNewLabel_1_2.setBounds(759, 275, 120, 183);
		contentPane.add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("");
		lblNewLabel_1_3.setIcon(new ImageIcon(Main.class.getResource("/Client_GUI/Icon/DoHoaNgua.png")));
		lblNewLabel_1_3.setBackground(Color.WHITE);
		lblNewLabel_1_3.setBounds(207, 460, 120, 183);
		contentPane.add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("");
		lblNewLabel_1_4.setIcon(new ImageIcon(Main.class.getResource("/Client_GUI/Icon/DoHoaTuong.png")));
		lblNewLabel_1_4.setBackground(Color.WHITE);
		lblNewLabel_1_4.setBounds(848, 506, 112, 151);
		contentPane.add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_4_1 = new JLabel("");
		lblNewLabel_1_4_1.setIcon(new ImageIcon(Main.class.getResource("/Client_GUI/Icon/DoHoaVua1.png")));
		lblNewLabel_1_4_1.setBackground(Color.WHITE);
		lblNewLabel_1_4_1.setBounds(68, 327, 108, 128);
		contentPane.add(lblNewLabel_1_4_1);
		
		JButton btnSignup = new JButton("SIGN UP");
        btnSignup.setForeground(new Color(255, 255, 255));
        btnSignup.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        btnSignup.setBackground(new Color(225, 198, 102));
        btnSignup.setFocusPainted(false); // Tắt viền khi nhấn
        btnSignup.setBounds(390, 598, 233, 59);
        contentPane.add(btnSignup);
        
        JLabel lblNewLabel_2 = new JLabel("");
        lblNewLabel_2.setBounds(275, 354, 443, 101);
        contentPane.add(lblNewLabel_2);
        
        JLabel lblNewLabel_3 = new JLabel("");
        lblNewLabel_3.setIcon(new ImageIcon(Main.class.getResource("/Client_GUI/Icon/CHESS GAME.png")));
        lblNewLabel_3.setBounds(290, 358, 398, 77);
        contentPane.add(lblNewLabel_3);
        
        JLabel lblNewLabel_1_3_1 = new JLabel("");
        lblNewLabel_1_3_1.setIcon(new ImageIcon(Main.class.getResource("/Client_GUI/Icon/DoaHoaNgua2.png")));
        lblNewLabel_1_3_1.setBackground(Color.WHITE);
        lblNewLabel_1_3_1.setBounds(32, 598, 120, 183);
        contentPane.add(lblNewLabel_1_3_1);
        
        JLabel lblNewLabel_1_3_2 = new JLabel("");
        lblNewLabel_1_3_2.setIcon(new ImageIcon(Main.class.getResource("/Client_GUI/Icon/DoHoaTot2.png")));
        lblNewLabel_1_3_2.setBackground(Color.WHITE);
        lblNewLabel_1_3_2.setBounds(679, 598, 120, 183);
        contentPane.add(lblNewLabel_1_3_2);
		btnSignup.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
            	btnSignup.setBackground(new Color(210, 180, 80));
            }

            public void mouseExited(MouseEvent e) {
            	btnSignup.setBackground(new Color(225, 198, 102));
            }
        });
		btnSignup.addActionListener(e -> {
		    setContentPane(new SignUp(this));
		    revalidate();
		    repaint();
		});
	}

}

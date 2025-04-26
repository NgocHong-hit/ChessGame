package Client_GUI;

import Client_Socket.Client;

import javax.swing.*;
import java.awt.*;


public class Play extends JFrame {
    private Client client;
    private String username;
    private int score;

    private JLabel nameLabel;
    private JLabel scoreLabel;
    private JButton btnMatch;
    private JButton btnViewRooms;
    private JTextArea activeGamesArea;

    public Play(Client client, String username) {
        this.client = client;
        this.username = username;

        setTitle("Sảnh chờ - Người chơi: " + username);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
        initListener();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        nameLabel = new JLabel("Tên: " + username);
        scoreLabel = new JLabel("Điểm: " + score);
        topPanel.add(nameLabel);
        topPanel.add(scoreLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        btnMatch = new JButton("Ghép đôi");
        btnViewRooms = new JButton("Xem các phiên đang chơi");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnMatch);
        buttonPanel.add(btnViewRooms);

        centerPanel.add(buttonPanel, BorderLayout.NORTH);

        activeGamesArea = new JTextArea();
        activeGamesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(activeGamesArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void initListener() {
        btnMatch.addActionListener(e -> {
            try {
                client.sendMatchRequest();
                JOptionPane.showMessageDialog(Play.this, "Đã gửi yêu cầu ghép đôi!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(Play.this, "Lỗi khi gửi yêu cầu!");
            }
        });

        btnViewRooms.addActionListener(e -> {
            try {
                client.requestActiveGames();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Lắng nghe phản hồi từ server
        client.listenForServerMessage(new Client.MessageHandler() {
            public void handle(String message) {
                System.out.println("Server: " + message);

                if (message.startsWith("MATCH_REQUEST:")) {
                    String rival = message.split(":")[1];
                    int result = JOptionPane.showConfirmDialog(Play.this,
                            "Bạn có đồng ý ghép đôi với " + rival + " không?",
                            "Xác nhận ghép đôi", JOptionPane.YES_NO_OPTION);
                    try {
                        client.sendMatchResponse(result == JOptionPane.YES_OPTION);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (message.startsWith("MATCH_SUCCESS:")) {
                    // Server gửi: MATCH_SUCCESS:WHITE hoặc MATCH_SUCCESS:BLACK
                    String color = message.split(":")[1];
                    handleMatchSuccess(color);
                }else if (message.startsWith("GAME_START|")) {
                    String color = message.split("\\|")[1];
                    handleMatchSuccess(color);
                }
                else if (message.startsWith("MATCH_DECLINED")) {
                    JOptionPane.showMessageDialog(Play.this, "Ghép đôi thất bại. Bạn đã trở lại hàng chờ.");
                } else if (message.startsWith("ACTIVE_GAMES:")) {
                    String list = message.substring("ACTIVE_GAMES:".length());
                    SwingUtilities.invokeLater(() -> activeGamesArea.setText(list));
                }
            }
        });
    }

    public void handleMatchSuccess(String color) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "Ghép đôi thành công, bạn là quân " + color + "!");

            JFrame chessFrame = new JFrame("Trò chơi Cờ Vua");
            chessFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            chessFrame.setSize(1000, 800);
            chessFrame.setLocationRelativeTo(null);

            // Gửi Client và màu quân vào giao diện Chess
            Chess chessPanel = new Chess(client, color);
            chessFrame.add(chessPanel);
            chessFrame.setVisible(true);

            dispose(); // Đóng cửa sổ Play
        });
    }
}

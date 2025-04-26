package Client_GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

import Client_Game.ChessGame;
import Client_Game.ChessMove;
import Client_Game.ChessPiece;
import Client_Game.Position;
import Client_Socket.Client;

public class Chess extends JPanel {
    private static final int BOARD_SIZE = 8;
    private static final long serialVersionUID = 1L;
    private JPanel boardPanel;
    private JButton[][] squares;
    private ImageIcon[][] pieceIcons;
    private JLabel timerLabel;
    private Timer timer;
    private int timeLeft = 600; // 600 giây = 10 phút
    private JLabel moveTimePiece;
    private Timer moveTime;
    private int moveTineLeft = 30;
    private ChessGame game;
    private Position selectedPosition;
    private static Client client;
    private static String color;
    

    public Chess(Client client, String color) {
    	 setName("Chess Game - " + color);
         setSize(800, 600);
        setBackground(new Color(165, 99, 10));
        setLayout(new BorderLayout());

        // ==== TẠO BOARD PANEL ====
        boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardPanel.setPreferredSize(new Dimension(800, 800));
        squares = new JButton[BOARD_SIZE][BOARD_SIZE];
        pieceIcons = new ImageIcon[BOARD_SIZE][BOARD_SIZE];

        // ==== TẠO RIGHT PANEL ====
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(300, 800));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        JLabel playerInfoLabel = new JLabel("Chức năng:");
        playerInfoLabel.setBounds(3, 10, 120, 19);
        playerInfoLabel.setForeground(new Color(242, 197, 38));
        playerInfoLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        rightPanel.add(playerInfoLabel);

        JButton btnNewButton = new JButton("New button");
        btnNewButton.setBounds(0, 46, 85, 21);
        rightPanel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("New button");
        btnNewButton_1.setBounds(115, 46, 85, 21);
        rightPanel.add(btnNewButton_1);

        JLabel lblNgiChi = new JLabel("Người chơi:");
        lblNgiChi.setBounds(3, 90, 120, 19);
        lblNgiChi.setForeground(new Color(242, 197, 38));
        lblNgiChi.setFont(new Font("Tahoma", Font.BOLD, 15));
        rightPanel.add(lblNgiChi);

        JLabel lblNewLabel = new JLabel("Tên người chơi 1");
        lblNewLabel.setBounds(25, 115, 120, 13);
        rightPanel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Tên người chơi 2");
        lblNewLabel_1.setBounds(133, 115, 120, 13);
        rightPanel.add(lblNewLabel_1);

        JLabel lblThiGian = new JLabel("Thời gian:");
        lblThiGian.setBounds(3, 150, 120, 19);
        lblThiGian.setForeground(new Color(242, 197, 38));
        lblThiGian.setFont(new Font("Tahoma", Font.BOLD, 15));
        rightPanel.add(lblThiGian);

        JLabel lblNewLabel_2 = new JLabel("Nước đi");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_2.setForeground(new Color(242, 197, 38));
        lblNewLabel_2.setBounds(10, 180, 99, 21);
        rightPanel.add(lblNewLabel_2);

        moveTimePiece = new JLabel("00:30");
        moveTimePiece.setBounds(120, 180, 81, 21);
        moveTimePiece.setForeground(Color.BLACK);
        moveTimePiece.setFont(new Font("Tahoma", Font.BOLD, 17));
        moveTimePiece.setAlignmentX(CENTER_ALIGNMENT);
        rightPanel.add(moveTimePiece);

        JLabel lblNewLabel_3 = new JLabel("Trận đấu");
        lblNewLabel_3.setForeground(new Color(242, 197, 38));
        lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 17));
        lblNewLabel_3.setBounds(10, 210, 99, 21);
        rightPanel.add(lblNewLabel_3);

        timerLabel = new JLabel("10:00");
        timerLabel.setBounds(120, 210, 81, 21);
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        timerLabel.setAlignmentX(CENTER_ALIGNMENT);
        rightPanel.add(timerLabel);

        JLabel lblChat = new JLabel("Chat");
        lblChat.setForeground(new Color(242, 197, 38));
        lblChat.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblChat.setBounds(3, 250, 120, 19);
        rightPanel.add(lblChat);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 280, 260, 191);
        rightPanel.add(scrollPane);

        JTextArea textArea = new JTextArea();
        scrollPane.setViewportView(textArea);

        JTextField textField = new JTextField();
        textField.setBounds(6, 490, 190, 32);
        rightPanel.add(textField);
        textField.setColumns(10);

        JButton btnNewButton_2 = new JButton("Gửi");
        btnNewButton_2.setBounds(205, 490, 60, 32);
        rightPanel.add(btnNewButton_2);

        // ==== KHỞI TẠO GAME ====
        game = new ChessGame();
        selectedPosition = null;
        initializeBoard();
        updateBoardUI();
        startTimer();

        // ==== THÊM VÀO GIAO DIỆN ====
        add(boardPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }


    private void startTimer() {
        timer = new Timer(1000, e -> {
            if (timeLeft > 0) {
                timeLeft--;
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;
                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
            } else {
                timer.stop();
                JOptionPane.showMessageDialog(null, "Hết thời gian! Trận đấu kết thúc.");
            }
        });
        timer.start();
    }

    private void startMoveTimer() {
    	moveTineLeft = 30;
        if (moveTime != null && moveTime.isRunning()) {
            moveTime.stop();
        }

        moveTime = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	moveTineLeft--;
                updateMoveTimerLabel(); // cập nhật giao diện, ví dụ: lblMoveTime.setText("Thời gian: " + moveTimeRemaining + "s");

                if (moveTineLeft <= 0) {
                    moveTime.stop();
                    JOptionPane.showMessageDialog(null, "Hết thời gian cho nước đi!");
                    selectedPosition = null;
                }
            }
        });
        moveTime.start();
    }
    private void resetMoveTime() {
        moveTineLeft = 30;
        updateMoveTimerLabel();
    }
    private void updateMoveTimerLabel() {
        int minutes = moveTineLeft / 60;
        int seconds = moveTineLeft % 60;
        moveTimePiece.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void stopMoveTimer() {
        if (moveTime != null && moveTime .isRunning()) {
            moveTime.stop();
        }
    }



    private ImageIcon loadIcon(String fileName) {
        try {
            String path = "C:/Users/DELL/eclipse-workspace/ChoiCoVua/src/piece/" + fileName;
            java.net.URL imgURL = new File(path).toURI().toURL();
            ImageIcon icon = new ImageIcon(imgURL);
            return icon;
        } catch (Exception e) {
            System.err.println("Không tìm thấy ảnh: " + fileName + " - " + e.getMessage());
            return null;
        }
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Color brownColor = new Color(156, 95, 45);
                Color lightYellowColor = new Color(232, 179, 108);
                squares[row][col] = new JButton();
                squares[row][col].setPreferredSize(new Dimension(75, 75));
                squares[row][col].setBackground((row + col) % 2 == 0 ? brownColor : lightYellowColor);

                final int r = row;
                final int c = col;
                squares[row][col].addActionListener(e -> handleSquareClick(r, c));
                boardPanel.add(squares[row][col]);
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        ChessPiece[][] board = game.getBoard();
        Position clickedPos = new Position(row, col);

        if (selectedPosition == null) {
            ChessPiece piece = board[row][col];

            if (piece != null && piece.getColor() == game.getCurrentTurn()) {
                selectedPosition = clickedPos;
                highlightSquare(row, col, true);
                System.out.println("Selected: " + row + ", " + col);

                // ✅ Bắt đầu thời gian nước đi khi chọn quân đúng
                startMoveTimer();
            } else {
                JOptionPane.showMessageDialog(null, "Chưa đến lượt của bạn", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }

        } else {
            ChessPiece piece = board[selectedPosition.getRow()][selectedPosition.getColumn()];
            if (piece == null) {
                System.out.println("Error: No piece at selected position");

                // ✅ Dừng timer nếu có lỗi
                stopMoveTimer();

                selectedPosition = null;
                return;
            }

            ChessMove move = new ChessMove(piece, clickedPos);
            System.out.println("move from " + selectedPosition + " to " + clickedPos);

            if (game.makeMove(move)) {
                updateBoardUI();
                System.out.println("Move successful");

                // ✅ Reset và dừng đồng hồ sau nước đi thành công
                resetMoveTime(); // reset UI về 30 giây
                stopMoveTimer(); // dừng lại cho tới lượt kế

                if (game.isGameOver()) {
                    JOptionPane.showMessageDialog(this, "Trò chơi kết thúc!");
                    if (timer != null) timer.stop();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nước đi không hợp lệ", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }

            // ✅ Hủy highlight & reset vị trí đã chọn
            highlightSquare(selectedPosition.getRow(), selectedPosition.getColumn(), false);
            selectedPosition = null;
        }
    }


    private void highlightSquare(int row, int col, boolean highlight) {
        Color brownColor = new Color(156, 95, 45);
        Color lightYellowColor = new Color(232, 179, 108);
        Color highlightColor = new Color(255, 255, 200);
        squares[row][col].setBackground(highlight ? highlightColor : ((row + col) % 2 == 0 ? brownColor : lightYellowColor));
    }

    private void updateBoardUI() {
        ChessPiece[][] board = game.getBoard();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                ChessPiece piece = board[row][col];
                if (piece == null) {
                    squares[row][col].setIcon(null);
                } else {
                    String iconName = getIconName(piece);
                    ImageIcon icon = loadIcon(iconName);
                    squares[row][col].setIcon(icon);
                }
            }
        }
    }

    private String getIconName(ChessPiece piece) {
        String color = piece.getColor() == ChessPiece.PieceColor.white ? "white" : "black";
        String type = piece.getType().toString();
        return type + "_" + color + ".png";
    }

    public static void main(String[] args) {
    	JFrame frame = new JFrame("Cờ vua");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setContentPane(new Chess(client, "white"));
    	frame.pack();
    	frame.setLocationRelativeTo(null);
    	frame.setVisible(true);

    }
}

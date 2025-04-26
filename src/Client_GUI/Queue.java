package Client_GUI;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class Queue extends JFrame {
    private JLabel statusLabel;
    private JLabel timerLabel;
    private JButton viewMatchesButton;
    private JButton cancelButton;
    private Timer timer;
    private int seconds = 0;

    public Queue() {
        setTitle("Hàng Đợi");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Nhãn tiêu đề
        JLabel titleLabel = new JLabel("Hàng Đợi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        // Nhãn trạng thái
        statusLabel = new JLabel("Đang tìm đối thủ...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        // Spinner
        JProgressBar spinner = new JProgressBar();
        spinner.setIndeterminate(true);

        // Nhãn thời gian chờ
        timerLabel = new JLabel("Thời gian chờ: 0 phút 0 giây", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // Nút "Xem trận đấu đang diễn ra"
        viewMatchesButton = new JButton("Xem trận đấu đang diễn ra");
        viewMatchesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Hiển thị danh sách trận đấu đang diễn ra.");
            }
        });

        // Nút "Hủy"
        cancelButton = new JButton("Hủy");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn rời hàng đợi?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Đã hủy hàng đợi.");
                }
            }
        });

        // Bố cục
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);
        gbc.gridy = 1;
        mainPanel.add(statusLabel, gbc);
        gbc.gridy = 2;
        mainPanel.add(spinner, gbc);
        gbc.gridy = 3;
        mainPanel.add(timerLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(viewMatchesButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Bắt đầu đếm thời gian
        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds++;
                int minutes = seconds / 60;
                int remainingSeconds = seconds % 60;
                timerLabel.setText(String.format("Thời gian chờ: %d phút %d giây", minutes, remainingSeconds));
            }
        }, 1000, 1000);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Queue queueGUI = new Queue();
            queueGUI.setVisible(true);
        });
    }
}
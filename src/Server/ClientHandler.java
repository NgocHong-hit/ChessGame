package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.mindrot.jbcrypt.BCrypt;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private KeyPair RSAkey;
    private static final String URL = "jdbc:mysql://localhost:3306/chess_game?useSSL=false&serverTimezone=UTC";
    private static final String User = "root";
    private static final String Password = "";
    private String username;
    private boolean confirmedMatch = false;
    private ClientHandler pendingMatchWith;
    private Server_socket server;
    private SecretKeySpec aesKey;
   public String getUsername() {
	   return username;
   }
   
   public void setUsername (String username) {
	   this.username = username;
   }
   
   
   
    public ClientHandler(Socket clientSocket, KeyPair RSAkey, Server_socket server) {
        this.clientSocket = clientSocket;
        this.RSAkey = RSAkey;
       this.server = server;
    }

    public void run() {
        try {
            // Tạo luồng I/O để giao tiếp với Client
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Gửi public key RSA cho client
            String publicKey = Base64.getEncoder().encodeToString(RSAkey.getPublic().getEncoded());
            output.println(publicKey);

            // Nhận khóa AES đã mã hóa từ client và giải mã bằng private key RSA
            String encryptedAESKey = input.readLine();
            String aesKeyStr = decryptRSA(encryptedAESKey, RSAkey.getPrivate());
            this.aesKey = new SecretKeySpec(Base64.getDecoder().decode(aesKeyStr), "AES");

            System.out.println("Server nhận AES key từ client: " + aesKeyStr);

            while (true) {
                String encryptedRequest = input.readLine();
                if (encryptedRequest == null) {
                    System.out.println("Client ngắt kết nối.");
                    break;
                }

                String request;
                try {
                    request = decryptAES(encryptedRequest, aesKey);
                } catch (Exception e) {
                    output.println(encryptAES("ERROR|Không thể giải mã yêu cầu", aesKey));
                    continue;
                }
                System.out.println("Từ client (dữ liệu mã hóa): " + encryptedRequest);
                System.out.println("Yêu cầu từ client: " + request);

                if (request.startsWith("REGISTER")) {
                    String[] parts = request.split("\\|");
                    if (parts.length == 7) {
                        String username = parts[1];
                        String password = parts[2];
                        String lastName = parts[3];
                        String firstName = parts[4];
                        String gender = parts[5];
                        String dateOfBirth = parts[6];
                        handleRegister(username, password, lastName, firstName, gender, dateOfBirth, output, aesKey);
                    } else {
                        output.println(encryptAES("ERROR|Sai định dạng dữ liệu gửi từ client", aesKey));
                    }
                } else if (request.startsWith("SIGNIN")) {
                    String[] parts = request.split("\\|");
                    if (parts.length == 3) {
                        String username = parts[1];
                        String password = parts[2];
                        System.out.println("Đang xử lý đăng nhập cho: " + username);
                        handleSignIn(username, password, aesKey, output);
                    } else {
                        output.println(encryptAES("ERROR|Sai định dạng dữ liệu đăng nhập", aesKey));
                    }
                }else if (request.equals("MATCH_REQUEST")) {
                	server.addToQueue(this);
                	server.attemptMatchPlayer();
                	
                	if (this.pendingMatchWith != null) {
                		this.sendMessage("MATCH_REQUEST:" + this.pendingMatchWith.getUsername());
                    } else {
                        this.sendMessage("MATCH_REQUEST:WAITING"); // hoặc báo là đang chờ người chơi khác
                    }
                }
                else if (request.startsWith("MATCH_RESPONSE")) {
                    String[] parts = request.split("\\|");
                    if (parts.length == 2) {
                        boolean accepted = parts[1].equalsIgnoreCase("ACCEPT");
                        this.handleMatchConfirm(accepted);
                    } else {
                        sendMessage("ERROR|Sai định dạng phản hồi ghép đôi");
                    }
                }

                else if (request.equals("MATCH_CONFIRM")) {
                    handleMatchConfirm(true);
                }else if (request.equals("MATCH_DECLINE")) {
                	handleMatchConfirm(false);
                }else {
                    output.println(encryptAES("ERROR|Lệnh không hợp lệ", aesKey));
                }
                System.out.println("Đã giải mã: " + request);
            } 
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }       

    // Giải mã AES bằng khóa private Server Key
    private String decryptRSA(String encryptedAESKey, PrivateKey privatekey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privatekey);
        byte[] decoded = Base64.getDecoder().decode(encryptedAESKey);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    // Mã hóa dữ liệu bằng khóa AES gửi tới client
    private String encryptAES(String data, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    } 

    // Giải mã dữ liệu đã mã hóa bằng khóa AES
    private String decryptAES(String encryptedData, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

 // Xử lí hàm đăng ký
    private void handleRegister(String username, String password, String lastName, String firstName, String gender, String dateOfBirth, PrintWriter writer, SecretKeySpec aesKey) throws Exception {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // Mã hóa mật khẩu bằng BCrypt
        try (Connection conn = DriverManager.getConnection(URL, User, Password);
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO users (username, password, LastName, FirstName, Gender, DateOfBirth) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, lastName);
            stmt.setString(4, firstName);
            stmt.setString(5, gender);
            stmt.setString(6, dateOfBirth);  // Định dạng yyyy-MM-dd
            stmt.executeUpdate();
            String response = "SUCCESS";
            writer.println(encryptAES(response, aesKey));
        } catch (SQLIntegrityConstraintViolationException e) {
            String response = "ERROR|Username already exists";
            writer.println(encryptAES(response, aesKey));
        }
    }
    
//    Xử lí hàm đăng nhập
    private void handleSignIn (String username, String password, SecretKeySpec aesKey, PrintWriter writer) throws Exception{
    	try (Connection conn = DriverManager.getConnection(URL, User, Password);
    			PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")){
    		stmt.setString(1, username);
    		ResultSet rs = stmt.executeQuery();
    		
    		if (rs.next()) {
    		    String hashedPassword = rs.getString("password");
    		    if (BCrypt.checkpw(password, hashedPassword)) {
    		        writer.println(encryptAES("SUCCESS", aesKey));
    		        this.setUsername(username);
    		        server.addToQueue(this);
    		        server.attemptMatchPlayer();

    		    } else {
    		    	writer.println(encryptAES("ERROR|Tên đăng nhập hoặc mật khẩu không đúng", aesKey));
    		    }
    		} else {
    			writer.println(encryptAES("ERROR|Tài khoản không tồn tại", aesKey));
    		}

    	}catch (SQLException e) {
            e.printStackTrace();
            String response = "ERROR|Lỗi hệ thống";
            writer.println(encryptAES(response, aesKey));
        }
    }
    
// Set đối thủ đang chờ xác nhận
    public void setPendingMatchWith(ClientHandler partner) {
    	this.pendingMatchWith = partner;
    	if (partner != null) {
    		String name = partner.getUsername();
    	    System.out.println("Ghép đôi với: " +name);
    	} else {
    	    System.out.println("Chưa có đối thủ ghép đôi.");
    	}

    }

//    Hàm xử lí xác nhận từ chối 
    public void handleMatchConfirm(boolean accepted) {
        if (!accepted) {
            this.sendMessage("MATCH_DECLINED");
            if (pendingMatchWith != null) {
                pendingMatchWith.sendMessage("MATCH_DECLINED");
                server.addToQueue(pendingMatchWith);
            }
            server.addToQueue(this);
            return;
        }

        // Nếu đã đồng ý ghép đôi, tiếp tục
        this.confirmedMatch = true;

        if (pendingMatchWith != null && pendingMatchWith.confirmedMatch) {
            // Tạo phiên chơi nếu cả hai người đều đồng ý
            GameSession game = new GameSession(this, pendingMatchWith);
            server.addGameSession(game);  // Đưa phiên chơi vào danh sách các game đang chơi

            // Gửi thông báo bắt đầu trận đấu cho cả hai người chơi
            this.sendMessage("MATCH_START");
            pendingMatchWith.sendMessage("MATCH_START");
        }
    
    	this.confirmedMatch = true;
    	if (pendingMatchWith != null && pendingMatchWith.confirmedMatch) {
    		GameSession game = new GameSession (this,pendingMatchWith);
    		server.addGameSession(game);
    		this.sendMessage("MATCH_START");
    		pendingMatchWith.sendMessage("MATCH_START");
    	}
    }
    
    public void sendMessage(String message) {
        try {
            if (aesKey == null) {
                System.err.println("AES key chưa được khởi tạo!");
                return;
            }
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            output.println(encryptAES(message, aesKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

//    Cập nhật điểm trong database
//    public void decreaseScore(int amount) {
//        // Ví dụ: cập nhật điểm trong database
//        try (Connection conn = DriverManager.getConnection(URL, User, Password);
//             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET score = score - ? WHERE username = ?")) {
//            stmt.setInt(1, amount);
//            stmt.setString(2, this.username);
//            stmt.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

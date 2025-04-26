package Client_Socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import Client_GUI.Play;


public class Client {
    private SecretKey AESkey;  // Khóa AES riêng cho mỗi client
    private PublicKey serverPublic;
    private PrintWriter output;
    private BufferedReader input;
    private Socket socket;
    private Play play;

    public Client(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectToServer() {
        try {
            // Nhận public key từ server
            String publicKeyStr = input.readLine();
            byte[] publicKeyByte = Base64.getDecoder().decode(publicKeyStr);
            KeyFactory keyfactory = KeyFactory.getInstance("RSA");
            serverPublic = keyfactory.generatePublic(new java.security.spec.X509EncodedKeySpec(publicKeyByte));

            // Sinh khóa AES duy nhất cho mỗi client
            AESkey = generateAESKey();
            String aesKeyStr = Base64.getEncoder().encodeToString(AESkey.getEncoded());
            String encryptedAESKey = encryptRSA(aesKeyStr, serverPublic);
            output.println(encryptedAESKey);  // Gửi khóa AES mã hóa tới server
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sinh khóa AES riêng biệt cho mỗi client
    private static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // 128-bit AES key
        return keyGen.generateKey();
    }

    // Mã hóa AES bằng public key RSA
    private static String encryptRSA(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Mã hóa dữ liệu bằng khóa AES gửi tới sever
    private static String encryptAES(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Giải mã dữ liệu bằng khóa AES
    private static String decryptAES(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }

    // Gửi và nhận thông điệp với AES mã hóa
    public String sendRequest(String message) {
        try {
            String encryptedMessage = encryptAES(message, AESkey); // Mã hóa thông điệp với AES
            output.println(encryptedMessage);
            String encryptedResponse = input.readLine();
            return decryptAES(encryptedResponse, AESkey);  // Giải mã phản hồi từ server
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi kết nối hoặc mã hóa.";
        }
    }

    public String register(String username, String password, String lastName, String firstName, String gender, String date_of_brith) throws Exception {
        String request = "REGISTER|" + username + "|" + password + "|" + lastName + "|" + firstName + "|" + gender + "|" + date_of_brith;
        String encryptedRequest = encryptAES(request, AESkey);
        output.println(encryptedRequest);
        String encryptedResponse = input.readLine();
        return decryptAES(encryptedResponse, AESkey); // Trả về chuỗi đã giải mã
    }
    
    public String login (String username, String password) throws Exception {
    	  String request = "SIGNIN|" + username + "|" + password;
    	  String encrypted = encryptAES(request, AESkey);
    	  output.println(encrypted);
    	  String response = input.readLine();
    	  return decryptAES(response, AESkey);
    }
    
    public String receiveMessage() throws Exception {
        String encryptedResponse = input.readLine();
        if (encryptedResponse == null) return null;
        return decryptAES(encryptedResponse, AESkey);
    }
    
    public void sendMatchRequest() throws Exception {
        String request = "MATCH_REQUEST";
        String encrypted = encryptAES(request, AESkey);
        output.println(encrypted);
    }

    public void sendMatchResponse(boolean accept) throws Exception {
        String response = "MATCH_RESPONSE|" + (accept ? "ACCEPT" : "DECLINE");
        String encrypted = encryptAES(response, AESkey);
        output.println(encrypted);
    }
    
    public void requestActiveGames() throws Exception {
        String request = "REQUEST_ACTIVE_GAMES";
        String encrypted = encryptAES(request, AESkey);
        output.println(encrypted);
    }
    
    public void sendMessage() throws Exception {
    	String request = "JOIN_QUEUE";
    	 String encrypted = encryptAES(request, AESkey);
         output.println(encrypted);    }
    
    public void setPlayInstance(Play play) {
        this.play = play;
    }

    public void listenForServerMessage(MessageHandler handler) {
        new Thread(() -> {
            try {
                String line;
                while ((line = input.readLine()) != null) {
                    String msg = decryptAES(line, AESkey);

                    if (msg.startsWith("GAME_START")) {
                        String[] parts = msg.split("\\|");
                        String color = parts[1];
                        if (play != null) {
                            play.handleMatchSuccess(color);
                        }
                    } else {
                        handler.handle(msg); // chuyển lại cho GUI xử lý
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public interface MessageHandler {
        void handle(String message);
    }

    public void close() throws Exception {
        if (socket != null) socket.close();
        if (output != null) output.close();
        if (input != null) input.close();
    }
}

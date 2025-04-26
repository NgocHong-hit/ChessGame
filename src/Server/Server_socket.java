package Server;


import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server_socket {
	private static final int port = 12345;
    private static KeyPair RSAkey;
    private ServerSocket serverSocket;
    private static ExecutorService executor; // Thread pool để xử lý nhiều client
    private Queue<ClientHandler> waitingQueue = new LinkedList<>();
    private List<GameSession> gameSession = new ArrayList<>();
    private List<ClientHandler> clients = new ArrayList<>();
    
    //	Sinh cặp khóa RSA
	private Server_socket(int port) throws Exception{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // Độ dài khóa 2048 bit
        RSAkey = keyGen.generateKeyPair();
//        Khởi tạo SeverSocket trên cổng mặc định
        serverSocket = new ServerSocket(port);
        System.out.println("Server đang chay trên cổng "+ port + "...");
//      	Tạo thread pool với tối đa 15 client đồng thời
        executor = Executors.newFixedThreadPool(15);
	}
	
	public void start() {
		try {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client đã kết nối: "+ clientSocket.getInetAddress().getHostAddress());
//				Tạo ClientHandler và chạy trong thread riêng
				ClientHandler handler = new ClientHandler(clientSocket, RSAkey,this);
				executor.execute(handler);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    public synchronized void addToQueue(ClientHandler player) {
    	waitingQueue.offer(player);
    	sendRoomListToAllClients();
    }
    
//    Hàm ghép đôi nếu có hai người
    public synchronized void attemptMatchPlayer() {
        while (waitingQueue.size() >= 2) {
            ClientHandler p1 = waitingQueue.poll();
            ClientHandler p2 = waitingQueue.poll();

            // Nếu cùng username (người chơi giống nhau) thì bỏ qua, trả p2 lại hàng chờ
            if (p1.getUsername().equals(p2.getUsername())) {
                waitingQueue.add(p2); // Giữ p2 lại, bỏ qua vòng này
                continue;
            }

            // Gửi yêu cầu xác nhận ghép đôi
            p1.sendMessage("MATCH_REQUEST:" + p2.getUsername());
            p2.sendMessage("MATCH_REQUEST:" + p1.getUsername());

            // Tạm thời lưu thông tin lại
            p1.setPendingMatchWith(p2);
            p2.setPendingMatchWith(p1);
        }
    }

    public void addGameSession (GameSession game) {
    	gameSession.add(game);
    }
    
	public static void main (String [] args) throws Exception {
		Server_socket server = new Server_socket(port);
		server.start();
	}
	
	public synchronized void sendRoomListToAllClients() {
		 StringBuilder roomList = new StringBuilder("ROOM_LIST");

		    for (GameSession game : gameSession) {
		        String player1 = game.getPlayer1().getUsername();
		        String player2 = game.getPlayer2().getUsername();
		        roomList.append("|").append(player1).append("-vs-").append(player2);
		    }

		    // Gửi cho tất cả client
		    for (ClientHandler client : clients) {
		        client.sendMessage(roomList.toString());
		    }
	}
	public synchronized void acceptMatch(ClientHandler p1, ClientHandler p2) {
	    // Xử lý việc người chơi chấp nhận ghép đôi và tạo GameSession
	    GameSession game = new GameSession(p1, p2);
	    addGameSession(game);
	    
	    // Gửi thông báo cho cả hai người chơi về việc bắt đầu trận đấu
	    p1.sendMessage("MATCH_ACCEPTED:" + p2.getUsername());
	    p2.sendMessage("MATCH_ACCEPTED:" + p1.getUsername());
	    
	    // Cập nhật danh sách phòng chơi
	    sendRoomListToAllClients();
	}
}

package Server;

public class GameSession {
	private ClientHandler player1;
	private ClientHandler player2;
	private boolean isPlayer1Turn;
	
	public GameSession (ClientHandler p1, ClientHandler p2) {
		this.player1 = p1;
		this.player2 = p2;
		this.isPlayer1Turn = true;
		
//		Thông báo cho cả hai client là game đã bắt đầu
		player1.sendMessage("GAME_START|WHITE");
        player2.sendMessage("GAME_START|BLACK");
	}
	
	public ClientHandler getPlayer1() {
		return player1;
	}
	
	public ClientHandler getPlayer2() {
		return player2;
	}
	
	public void handMove (ClientHandler player, String movedata) {
//		Kiểm tra lượt đi
		if ((player == player1 && isPlayer1Turn) || (player == player2 && isPlayer1Turn)) {
//			Gửi nưới đi cho đối phương
			if (player == player1) {
				player2.sendMessage("OPPONENT_MOVE|" + movedata);
            } else {
                player1.sendMessage("OPPONENT_MOVE|" + movedata);
            }
			isPlayer1Turn = !isPlayer1Turn;
		}else {
            player.sendMessage("ERROR|Không phải lượt của bạn");

		}
	}
	
	public void endGame(String result) {
        player1.sendMessage("GAME_END|" + result);
        player2.sendMessage("GAME_END|" + result);
    }
	
}

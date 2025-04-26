package Client_Game;

import java.util.ArrayList;
import java.util.List;

public class ChessPiece {
	public enum PieceType {
		pawn,
		knight,
		bishop,
		rook,
		queen,
		king
	}
	
	public enum PieceColor{
		white,
		black
	}
	private PieceType type;
	private PieceColor color;
	private Position position;
	
	public ChessPiece(PieceType type, PieceColor color , Position position) {
		this.type = type;
		this.color = color;
		this.position = position;
	}
	
	public PieceType getType() {
		return type;
	}
	public PieceColor getColor() {
		return color;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	
//	hàm kiểm tra quân cờ có nằm trong bàn cờ không
	private boolean isValidPosition (Position pos) {
		int row = pos.getRow();
		int col = pos.getColumn();
		return row >= 0 && row < 8 && col >= 0 && col < 8;
		}
//	kiểm tra quân cờ ở vj trí pos có phải quân địch không
	private boolean isEnemyPiece (ChessPiece [][] board, Position pos) {
		ChessPiece piece = board[pos.getRow()][pos.getColumn()];
        return piece != null && piece.getColor() != this.color;
	}
//	Di chuyển con tốt
	private List<Position> getPawnMove (ChessPiece [][] board){
		List<Position> moves = new ArrayList<>();
		int direction = (color == PieceColor.white) ? -1:1; //trắng đi lên, đen đi xuống
		int start = (color == PieceColor.white) ? 6:1; // vị trị bắt đầu của trằng là hàng 6
		
		// di chuyển tiến 1 ô
		Position forward1 = new Position (position.getRow() + direction , position.getColumn());
		if (isValidPosition(forward1) && board[forward1.getRow()][forward1.getColumn()] ==null){
			moves.add(forward1);
		}
		
//		di chuyển lên 2 ô
		if (position.getRow() == start) {
			Position forward2 = new Position (position.getRow() + 2*direction, position.getColumn());
			if (isValidPosition(forward2) && board[forward2.getRow()][forward2.getColumn()] ==null){
				moves.add(forward2);
			}
		}
//		ăn chéo của con tốt
		Position left = new Position (position.getRow() + direction , position.getColumn() - 1);
		Position right = new Position (position.getRow() + direction , position.getColumn() +1);
		if (isValidPosition(left) && isEnemyPiece(board, left)) {
			moves.add(left);
		}
		if (isValidPosition(right) && isEnemyPiece(board, right)) {
			moves.add(right);
		}
		return moves;
	}
	
//	Di chuyển con mã
	private List<Position> getKnightMove(ChessPiece[][] board) {
	    List<Position> moves = new ArrayList<>();
	    int[][] directions = {
	        {2, -1}, {2, 1}, {1, 2}, {-1, -2},
	        {-2, -1}, {-2, 1}, {-1, 2}, {1, -2}
	    };

	    for (int[] forward : directions) {
	        int newRow = position.getRow() + forward[0];
	        int newCol = position.getColumn() + forward[1];

	        // Kiểm tra nếu vị trí mới nằm trong phạm vi bàn cờ
	        if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
	            Position pos = new Position(newRow, newCol);
	            if (board[newRow][newCol] == null || isEnemyPiece(board, pos)) {
	                moves.add(pos);
	            }
	        }
	    }
	    return moves;
	}

	
//	Phương thức hỗ trợ con cờ khi gặp vật cản
	private List<Position> getMovesInDirection (ChessPiece [][] board, int rowDelta , int colDelta){
		List<Position> moves = new ArrayList<>();
		int row = position.getRow() + rowDelta;
		int col = position.getColumn() + colDelta;
		while (row>=0 && row <8 && col>=0 && col<8) {
			if (board[row][col] == null) {
				moves.add(new Position (row,col));
			}
			else {
				if (isEnemyPiece (board, new Position(row, col))) {
					moves.add (new Position(row, col));
				}
				break;
			}
			row += rowDelta;
			col += colDelta;
		}
		return moves;
	}
	
//	Di chuyển con xe
	private List<Position> getRookMove (ChessPiece [][] board){
		List<Position> moves = new ArrayList<>();
		moves.addAll(getMovesInDirection (board, 0,1));
		moves.addAll(getMovesInDirection (board, 0,-1));
		moves.addAll(getMovesInDirection (board, 1,0));
		moves.addAll(getMovesInDirection (board, -1,0));
		return moves;
	}
	
//	Di chuyển con tượng
	private List<Position> getBishopMove (ChessPiece [][] board){
		List<Position> moves = new ArrayList<>();
		moves.addAll(getMovesInDirection (board, 1,1));
		moves.addAll(getMovesInDirection (board, 1,-1));
		moves.addAll(getMovesInDirection (board, -1,-1));
		moves.addAll(getMovesInDirection (board, -1,1));
		return moves;
	}
	
//	Di chuyển con hậu
	private List<Position> getQueenMove (ChessPiece [][] board){
		List<Position> moves = new ArrayList<>();
		moves.addAll(getRookMove(board));
		moves.addAll(getBishopMove(board));
		return moves;
		
	}
	
//	Di chuyển con vua
	private List<Position> getKingMove(ChessPiece[][] board) {
	    int[][] direction = {
	        {0, -1}, {0, 1}, {1, 0}, {-1, 0},
	        {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
	    };
	    List<Position> moves = new ArrayList<>();
	    for (int[] forward : direction) {
	        Position pos = new Position(position.getRow() + forward[0], position.getColumn() + forward[1]);
	        if (isValidPosition(pos) && (board[pos.getRow()][pos.getColumn()] == null || isEnemyPiece(board, pos))) {
	            moves.add(pos);
	        }
	    }
	    return moves;
	}

	
	public List<Position> getMovesList (ChessPiece [][] board){
		List<Position> Move = new ArrayList<>();
		switch (type) {
		case pawn:
			Move.addAll(getPawnMove(board));
			break;
		case knight:
			Move.addAll(getKnightMove(board));
			break;
		case bishop:
			Move.addAll(getBishopMove(board));
			break;
		case rook:
			Move.addAll(getRookMove(board));
			break;
		case queen:
			Move.addAll(getQueenMove(board));
			break;
		case king:
			Move.addAll(getKingMove(board));
			break;
		}
		return Move;
	}
}

package Client_Game;

import java.util.List;

public class ChessMove {
    private ChessPiece piece;
    private Position from;
    private Position to;

    public ChessMove(ChessPiece piece, Position to) {
        this.piece = piece;
        this.from = (piece != null) ? piece.getPosition() : null;  // Kiểm tra null trước khi gọi getPosition
        this.to = to;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    // Kiểm tra nước đi hợp lệ không dựa vào trạng thái của bàn cờ
    public boolean isValid(ChessPiece[][] board) {
        if (piece == null || to == null) {
            return false;  // Trả về false nếu piece hoặc to là null
        }
        List<Position> validMove = piece.getMovesList(board);
        return validMove != null && validMove.contains(to);
    }

    // Thực hiện nước đi trên bàn cờ
    public void execute(ChessPiece[][] board) {
        if (piece == null || from == null || to == null) {
            return;  // Không thực hiện nếu piece, from, hoặc to là null
        }
        // Xóa quân cờ ở vị trí ban đầu
        board[from.getRow()][from.getColumn()] = null;
        // Đặt quân cờ vào vị trí đích
        board[to.getRow()][to.getColumn()] = piece;
        // Cập nhật vị trí của quân cờ
        piece.setPosition(to);
    }
}
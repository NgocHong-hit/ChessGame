package Client_Game;

import java.util.HashMap;
import java.util.Map;

import Client_Game.ChessPiece.PieceColor;
import Client_Game.ChessPiece.PieceType;

public class ChessGame {
    private ChessPiece[][] board;
    private ChessPiece.PieceColor currentTurn = PieceColor.white;
    private double whitePoint = 0.0;
    private double blackPoint = 0.0;
    private Map<String, Integer> boardUpdate = new HashMap<>();

    public ChessGame() {
        board = new ChessPiece[8][8];
        initializeBoard();
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    // Khởi tạo bàn cờ với các quân cờ
    public void initializeBoard() {
        // Quân đen
        board[0][0] = new ChessPiece(PieceType.rook, PieceColor.black, new Position(0, 0));
        board[0][1] = new ChessPiece(PieceType.knight, PieceColor.black, new Position(0, 1));
        board[0][2] = new ChessPiece(PieceType.bishop, PieceColor.black, new Position(0, 2));
        board[0][3] = new ChessPiece(PieceType.queen, PieceColor.black, new Position(0, 3));
        board[0][4] = new ChessPiece(PieceType.king, PieceColor.black, new Position(0, 4));
        board[0][5] = new ChessPiece(PieceType.bishop, PieceColor.black, new Position(0, 5));
        board[0][6] = new ChessPiece(PieceType.knight, PieceColor.black, new Position(0, 6));
        board[0][7] = new ChessPiece(PieceType.rook, PieceColor.black, new Position(0, 7));
        for (int i = 0; i < 8; i++) {
            board[1][i] = new ChessPiece(PieceType.pawn, PieceColor.black, new Position(1, i));
        }

        // Quân trắng
        board[7][0] = new ChessPiece(PieceType.rook, PieceColor.white, new Position(7, 0));
        board[7][1] = new ChessPiece(PieceType.knight, PieceColor.white, new Position(7, 1));
        board[7][2] = new ChessPiece(PieceType.bishop, PieceColor.white, new Position(7, 2));
        board[7][3] = new ChessPiece(PieceType.queen, PieceColor.white, new Position(7, 3));
        board[7][4] = new ChessPiece(PieceType.king, PieceColor.white, new Position(7, 4));
        board[7][5] = new ChessPiece(PieceType.bishop, PieceColor.white, new Position(7, 5));
        board[7][6] = new ChessPiece(PieceType.knight, PieceColor.white, new Position(7, 6));
        board[7][7] = new ChessPiece(PieceType.rook, PieceColor.white, new Position(7, 7));
        for (int i = 0; i < 8; i++) {
            board[6][i] = new ChessPiece(PieceType.pawn, PieceColor.white, new Position(6, i));
        }
    }

    // Thực hiện nước đi hợp lệ
    public boolean makeMove(ChessMove move) {
        if (move == null || move.getPiece() == null) {
            return false; // Kiểm tra an toàn
        }

        if (move.isValid(board) && move.getPiece().getColor() == currentTurn) {
            // Tính điểm nếu có quân bị bắt
            ChessPiece capturedPiece = board[move.getTo().getRow()][move.getTo().getColumn()];
            if (capturedPiece != null) {
                updatePoints(capturedPiece);
            }

            // Thực hiện nước đi
            move.execute(board);
            updateBoard();

            // Chuyển lượt
            switchTurn();
            return true;
        }
        return false;
    }

    // Cập nhật trạng thái bàn cờ
    private void updateBoard() {
        String boardHash = getBoardHash();
        boardUpdate.put(boardHash, boardUpdate.getOrDefault(boardHash, 0) + 1);
    }

    // Tạo chuỗi đại diện cho trạng thái bàn cờ
    private String getBoardHash() {
        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board[i][j];
                if (piece == null) {
                    hash.append("..");
                } else {
                    hash.append(piece.getColor().toString().charAt(0))
                        .append(piece.getType().toString().charAt(0));
                }
            }
        }
        return hash.toString();
    }

    // Chuyển lượt đi
    private void switchTurn() {
        currentTurn = (currentTurn == PieceColor.white) ? PieceColor.black : PieceColor.white;
    }

    // Cập nhật điểm số khi ăn quân
    private void updatePoints(ChessPiece capturedPiece) {
        double points = getPieceValue(capturedPiece.getType());
        if (capturedPiece.getColor() == PieceColor.white) {
            blackPoint += points;
        } else {
            whitePoint += points;
        }
    }

    // Giá trị điểm của từng loại quân cờ
    private double getPieceValue(PieceType type) {
        switch (type) {
            case pawn: return 1.0;
            case knight: return 3.0;
            case bishop: return 3.0;
            case rook: return 5.0;
            case queen: return 9.0;
            case king: return 0.0; // Vua không có giá trị vì không thể bị bắt
            default: return 0.0;
        }
    }

    // Kiểm tra trò chơi có kết thúc không (ví dụ: chiếu hết hoặc hòa)
    public boolean isGameOver() {
        // Logic kiểm tra chiếu hết hoặc hòa có thể thêm ở đây
        // Ví dụ: kiểm tra xem bên currentTurn có nước đi hợp lệ nào không
        return false; // Placeholder
    }

    // Trả về người chơi hiện tại
    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

    // Trả về điểm số
    public double getWhitePoint() {
        return whitePoint;
    }

    public double getBlackPoint() {
        return blackPoint;
    }
}
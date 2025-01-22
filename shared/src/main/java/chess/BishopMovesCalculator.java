package chess;

import java.util.ArrayList;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        int currentRow = position.getRow();
        int currentColumn = position.getColumn();
        if (board.getPiece(position) == null) {
            return moves;
        }
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        for (int i = currentRow + 1, j = currentColumn + 1; i < 9 && j < 9; i++, j++) {
            if (board.getPiece(new ChessPosition(i, j)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(i, j), null));
            } else {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
                break;
            }
        }
        for (int i = currentRow - 1, j = currentColumn - 1; i > 0 && j > 0; i--, j--) {
            if (board.getPiece(new ChessPosition(i, j)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(i, j), null));
            } else {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
                break;
            }
        }
        for (int i = currentRow + 1, j = currentColumn - 1; i < 9 && j > 0; i++, j--) {
            if (board.getPiece(new ChessPosition(i, j)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(i, j), null));
            } else {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
                break;
            }
        }
        for (int i = currentRow - 1, j = currentColumn + 1; i > 0 && j < 9; i--, j++) {
            if (board.getPiece(new ChessPosition(i, j)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(i, j), null));
            } else {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
                break;
            }
        }
        return moves;
    }
}

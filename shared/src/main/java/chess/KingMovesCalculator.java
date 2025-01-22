package chess;

import java.util.ArrayList;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        int currentColumn = position.getColumn();
        int currentRow = position.getRow();
        if (board.getPiece(position) == null) {
            return moves;
        }
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        if (currentRow < 8) {
            ChessPiece up = board.getPiece(new ChessPosition(currentRow + 1, currentColumn));
            if (up == null || up.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn), null));
            }
        }
        if (currentRow > 1) {
            ChessPiece down = board.getPiece(new ChessPosition(currentRow - 1, currentColumn));
            if (down == null || down.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn), null));
            }
        }
        if (currentColumn > 1) {
            ChessPiece left = board.getPiece(new ChessPosition(currentRow, currentColumn - 1));
            if (left == null || left.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow, currentColumn - 1), null));
            }
        }
        if (currentColumn < 8) {
            ChessPiece right = board.getPiece(new ChessPosition(currentRow, currentColumn + 1));
            if (right == null || right.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow, currentColumn + 1), null));
            }
        }
        if (currentRow < 8 && currentColumn > 1) {
            ChessPiece upLeft = board.getPiece(new ChessPosition(currentRow + 1, currentColumn - 1));
            if (upLeft == null || upLeft.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1), null));
            }
        }
        if (currentRow < 8 && currentColumn < 8) {
            ChessPiece upRight = board.getPiece(new ChessPosition(currentRow + 1, currentColumn + 1));
            if (upRight == null || upRight.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1), null));
            }
        }
        if (currentRow > 1 && currentColumn > 1) {
            ChessPiece downLeft = board.getPiece(new ChessPosition(currentRow - 1, currentColumn - 1));
            if (downLeft == null || downLeft.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1), null));
            }
        }
        if (currentRow > 1 && currentColumn < 8) {
            ChessPiece downRight = board.getPiece(new ChessPosition(currentRow - 1, currentColumn + 1));
            if (downRight == null || downRight.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1), null));
            }
        }
        return moves;
    }
}

package chess;

import java.util.ArrayList;
import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<ChessMove>();
        int currentColumn = position.getColumn();
        int currentRow = position.getRow();
        if (board.getPiece(position) == null) {
            return moves;
        }
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        if (currentRow < 7 && currentColumn > 1) {
            ChessPiece upLeft = board.getPiece(new ChessPosition(currentRow + 2, currentColumn - 1));
            if (upLeft == null || upLeft.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow + 2, currentColumn - 1), null));
            }
        }
        if (currentRow < 7 && currentColumn < 8) {
            ChessPiece upRight = board.getPiece(new ChessPosition(currentRow + 2, currentColumn + 1));
            if (upRight == null || upRight.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow + 2, currentColumn + 1), null));
            }
        }
        if (currentRow < 8 && currentColumn > 2) {
            ChessPiece leftUp = board.getPiece(new ChessPosition(currentRow + 1, currentColumn - 2));
            if (leftUp == null || leftUp.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 2), null));
            }
        }
        if (currentRow > 1 && currentColumn > 2) {
            ChessPiece leftDown = board.getPiece(new ChessPosition(currentRow - 1, currentColumn - 2));
            if (leftDown == null || leftDown.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 2), null));
            }
        }
        if (currentRow > 2 && currentColumn > 1) {
            ChessPiece downLeft = board.getPiece(new ChessPosition(currentRow - 2, currentColumn - 1));
            if (downLeft == null || downLeft.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow - 2, currentColumn - 1), null));
            }
        }
        if (currentRow > 2 && currentColumn < 8) {
            ChessPiece downRight = board.getPiece(new ChessPosition(currentRow - 2, currentColumn + 1));
            if (downRight == null || downRight.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow - 2, currentColumn + 1), null));
            }
        }
        if (currentRow > 1 && currentColumn < 7) {
            ChessPiece rightDown = board.getPiece(new ChessPosition(currentRow - 1, currentColumn + 2));
            if (rightDown == null || rightDown.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 2), null));
            }
        }
        if (currentRow < 8 && currentColumn < 7) {
            ChessPiece rightUp = board.getPiece(new ChessPosition(currentRow + 1, currentColumn + 2));
            if (rightUp == null || rightUp.getTeamColor() != color) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 2), null));
            }
        }
        return moves;
    }
}

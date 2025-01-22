package chess;

import java.util.ArrayList;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<ChessMove>();
        if (board.getPiece(position) == null) {
            return moves;
        }
        if (1 < position.getRow() && position.getRow() < 8) {
            if (1 < position.getColumn() && position.getColumn() < 8) {

            } else if (position.getColumn() == 1) {

            } else if (position.getColumn() == 8) {

            }
        } else if (position.getRow() == 1) {
            if (1 < position.getColumn() && position.getColumn() < 8) {

            } else if (position.getColumn() == 1) {

            } else if (position.getColumn() == 8) {

            }
        } else if (position.getRow() == 8) {
            if (1 < position.getColumn() && position.getColumn() < 8) {

            } else if (position.getColumn() == 1) {

            } else if (position.getColumn() == 8) {

            }
        }
        return moves;
    }
}

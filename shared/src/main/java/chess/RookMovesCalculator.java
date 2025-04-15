package chess;

import java.util.ArrayList;
import java.util.List;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        int currentRow = position.getRow();
        int currentColumn = position.getColumn();
        if (board.getPiece(position) == null) {
            return moves;
        }
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        var horizontalMoves = QueenMovesCalculator.getHorizontalMoves(board, position, currentRow, currentColumn,
                color);
        moves.addAll(horizontalMoves);
        return moves;
    }
}

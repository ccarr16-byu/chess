package chess;
import java.util.List;

public interface PieceMovesCalculator {
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);
}

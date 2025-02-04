package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position) == null) {
            return null;
        }
        List<ChessMove> moves;
        switch (board.getPiece(position).getPieceType()) {
            case PAWN:
                moves = new ArrayList<>(new PawnMovesCalculator().pieceMoves(board, position));
                return moves;
            case KING:
                moves = new ArrayList<>(new KingMovesCalculator().pieceMoves(board, position));
                return moves;
            case KNIGHT:
                moves = new ArrayList<>(new KnightMovesCalculator().pieceMoves(board, position));
                return moves;
            case ROOK:
                moves = new ArrayList<>(new RookMovesCalculator().pieceMoves(board, position));
                return moves;
            case BISHOP:
                moves = new ArrayList<>(new BishopMovesCalculator().pieceMoves(board, position));
                return moves;
            case QUEEN:
                moves = new ArrayList<>(new QueenMovesCalculator().pieceMoves(board, position));
                return moves;
            default:
                moves = new ArrayList<>();
        }
        return moves;
    }

    @Override
    public int hashCode() {
        int color;
        int pieceNum;

        if (this.pieceColor == ChessGame.TeamColor.WHITE) {
            color = 1;
        } else {
            color = 0;
        }

        pieceNum = switch (this.type) {
            case KING -> 1;
            case QUEEN -> 2;
            case ROOK -> 3;
            case BISHOP -> 4;
            case KNIGHT -> 5;
            case PAWN -> 6;
        };

        return (pieceNum * 10) + color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        ChessPiece obj2 = (ChessPiece)obj;
        return this.hashCode() == obj2.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s %s",this.pieceColor.name(), this.type.name());
    }
}

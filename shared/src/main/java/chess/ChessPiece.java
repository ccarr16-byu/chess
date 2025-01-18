package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

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
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition) == null) {
            return null;
        }
        return null;
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

        switch (this.type) {
            case KING:
                pieceNum = 1;
                break;
            case QUEEN:
                pieceNum = 2;
                break;
            case ROOK:
                pieceNum = 3;
                break;
            case BISHOP:
                pieceNum = 4;
                break;
            case KNIGHT:
                pieceNum = 5;
                break;
            case PAWN:
                pieceNum = 6;
                break;
            default:
                pieceNum = 0;
        }

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
        if (this.hashCode() == obj2.hashCode()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s",this.pieceColor.name(), this.type.name());
    }
}

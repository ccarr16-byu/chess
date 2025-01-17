package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int hashCode() {
        return (this.getStartPosition().getRow() * 1000) + (this.getStartPosition().getColumn() * 100) + (this.getEndPosition().getRow() * 10) + this.getEndPosition().getColumn();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        ChessMove obj2 = (ChessMove)obj;
        if (this.getStartPosition() == obj2.getStartPosition() && this.getEndPosition() == obj2.getEndPosition() && this.getPromotionPiece() == obj2.getPromotionPiece()) {
            return true;
        } else {
            return false;
        }
    }
}

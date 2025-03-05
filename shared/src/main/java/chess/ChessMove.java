package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotionPiece;
    }

    @Override
    public int hashCode() {
        int pieceNum;
        if (this.promotionPiece == null) {
            pieceNum = 0;
        } else {
            pieceNum = switch (this.promotionPiece) {
                case QUEEN -> 1;
                case ROOK -> 2;
                case BISHOP -> 3;
                case KNIGHT -> 4;
                default -> 0;
            };
        }
        return (this.getStartPosition().getRow() * 10000) + (this.getStartPosition().getColumn() * 1000) +
                (this.getEndPosition().getRow() * 100) + (this.getEndPosition().getColumn() * 10) + pieceNum;
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
        return this.hashCode() == obj2.hashCode();
    }
}

package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

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
        if (this.promotionPiece == null) {
            return null;
        } else {
            return this.promotionPiece;
        }
    }

    @Override
    public int hashCode() {
        int pieceNum;
        if (this.promotionPiece == null) {
            pieceNum = 0;
        } else {
            switch (this.promotionPiece) {
                case QUEEN:
                    pieceNum = 1;
                    break;
                case ROOK:
                    pieceNum = 2;
                    break;
                case BISHOP:
                    pieceNum = 3;
                    break;
                case KNIGHT:
                    pieceNum = 4;
                    break;
                default:
                    pieceNum = 0;
            }
        }
        return (this.getStartPosition().getRow() * 10000) + (this.getStartPosition().getColumn() * 1000) + (this.getEndPosition().getRow() * 100) + (this.getEndPosition().getColumn() * 10) + pieceNum;
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

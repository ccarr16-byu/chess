package chess;

import java.lang.Math;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {
    }

    public ChessBoard(ChessBoard copy) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                this.addPiece(new ChessPosition(i, j), copy.getPiece(new ChessPosition(i, j)));
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }

        for (int i = 1; i < 9; i++) {
            ChessPosition square = new ChessPosition(2, i);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            this.addPiece(square, piece);

            square = new ChessPosition(7, i);
            piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            this.addPiece(square, piece);
        }

        ChessPosition square = new ChessPosition(1, 5);
        ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        this.addPiece(square, piece);

        square = new ChessPosition(8, 5);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        this.addPiece(square, piece);

        square = new ChessPosition(1, 4);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        this.addPiece(square, piece);

        square = new ChessPosition(8, 4);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        this.addPiece(square, piece);

        square = new ChessPosition(1, 3);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        this.addPiece(square, piece);

        square = new ChessPosition(8, 3);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.addPiece(square, piece);

        square = new ChessPosition(1, 6);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        this.addPiece(square, piece);

        square = new ChessPosition(8, 6);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.addPiece(square, piece);

        square = new ChessPosition(1, 2);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        this.addPiece(square, piece);

        square = new ChessPosition(8, 2);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.addPiece(square, piece);

        square = new ChessPosition(1, 7);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        this.addPiece(square, piece);

        square = new ChessPosition(8, 7);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.addPiece(square, piece);

        square = new ChessPosition(1, 1);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        this.addPiece(square, piece);

        square = new ChessPosition(8, 1);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        this.addPiece(square, piece);

        square = new ChessPosition(1, 8);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        this.addPiece(square, piece);

        square = new ChessPosition(8, 8);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        this.addPiece(square, piece);
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    @Override
    public int hashCode() {
        int code = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    code += (Math.pow((new ChessPosition(i + 1, j + 1)).hashCode(), 2));
                } else {
                    code += (board[i][j].hashCode() * (Math.pow((new ChessPosition(i + 1, j + 1)).hashCode(), 2)));
                }
            }
        }
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        ChessBoard obj2 = (ChessBoard)obj;
        return obj2.hashCode() == this.hashCode();
    }
}

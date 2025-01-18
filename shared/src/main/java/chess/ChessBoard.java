package chess;

import java.util.HashMap;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private HashMap<ChessPosition, ChessPiece> boardState;

    public ChessBoard() {
        boardState = new HashMap<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                ChessPosition newSquare = new ChessPosition(i, j);
                boardState.put(newSquare, null);
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
        boardState.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return boardState.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (ChessPosition square : boardState.keySet()) {
            boardState.put(square, null);
        }

        for (int i = 1; i < 9; i++) {
            ChessPosition square = new ChessPosition(2, i);
            ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            boardState.put(square, piece);

            square = new ChessPosition(7, i);
            piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            boardState.put(square, piece);
        }

        ChessPosition square = new ChessPosition(1, 5);
        ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        boardState.put(square, piece);

        square = new ChessPosition(8, 5);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        boardState.put(square, piece);

        square = new ChessPosition(1, 4);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        boardState.put(square, piece);

        square = new ChessPosition(8, 4);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        boardState.put(square, piece);

        square = new ChessPosition(1, 3);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        boardState.put(square, piece);

        square = new ChessPosition(8, 3);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        boardState.put(square, piece);

        square = new ChessPosition(1, 6);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        boardState.put(square, piece);

        square = new ChessPosition(8, 6);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        boardState.put(square, piece);

        square = new ChessPosition(1, 2);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        boardState.put(square, piece);

        square = new ChessPosition(8, 2);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        boardState.put(square, piece);

        square = new ChessPosition(1, 7);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        boardState.put(square, piece);

        square = new ChessPosition(8, 7);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        boardState.put(square, piece);

        square = new ChessPosition(1, 1);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        boardState.put(square, piece);

        square = new ChessPosition(8, 1);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        boardState.put(square, piece);

        square = new ChessPosition(1, 8);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        boardState.put(square, piece);

        square = new ChessPosition(8, 8);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        boardState.put(square, piece);
    }

    @Override
    public int hashCode() {
        int code = 0;
        for (ChessPosition checkSquare : boardState.keySet()) {
            if (boardState.get(checkSquare) == null) {
                code += (checkSquare.hashCode() * checkSquare.hashCode());
            } else {
                code += (boardState.get(checkSquare).hashCode() * (checkSquare.hashCode()) * checkSquare.hashCode());
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
        if (obj2.hashCode() == this.hashCode()) {
            return true;
        } else {
            return false;
        }
    }
}

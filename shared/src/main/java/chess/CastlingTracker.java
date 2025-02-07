package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class CastlingTracker {
    private boolean whiteKingMoved = false;
    private boolean whiteKSRookMoved = false;
    private boolean whiteQSRookMoved = false;
    private boolean blackKingMoved = false;
    private boolean blackKSRookMoved = false;
    private boolean blackQSRookMoved = false;

    public CastlingTracker() {
    }

    public boolean isWhiteKingMoved() {
        return whiteKingMoved;
    }

    public void setWhiteKingMoved(boolean whiteKingMoved) {
        this.whiteKingMoved = whiteKingMoved;
    }

    public boolean isWhiteKSRookMoved() {
        return whiteKSRookMoved;
    }

    public void setWhiteKSRookMoved(boolean whiteKSRookMoved) {
        this.whiteKSRookMoved = whiteKSRookMoved;
    }

    public boolean isWhiteQSRookMoved() {
        return whiteQSRookMoved;
    }

    public void setWhiteQSRookMoved(boolean whiteQSRookMoved) {
        this.whiteQSRookMoved = whiteQSRookMoved;
    }

    public boolean isBlackKingMoved() {
        return blackKingMoved;
    }

    public void setBlackKingMoved(boolean blackKingMoved) {
        this.blackKingMoved = blackKingMoved;
    }

    public boolean isBlackKSRookMoved() {
        return blackKSRookMoved;
    }

    public void setBlackKSRookMoved(boolean blackKSRookMoved) {
        this.blackKSRookMoved = blackKSRookMoved;
    }

    public boolean isBlackQSRookMoved() {
        return blackQSRookMoved;
    }

    public void setBlackQSRookMoved(boolean blackQSRookMoved) {
        this.blackQSRookMoved = blackQSRookMoved;
    }

    public boolean canKSCastle (ChessBoard board, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            if (!isWhiteKSRookMoved() && board.getPiece(new ChessPosition(1, 8)) != null && board.getPiece(new ChessPosition(1, 8)).equals(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK))) {
                ChessPosition wKOrigin = new ChessPosition(1, 5);
                ChessPosition wKSSquare1 = new ChessPosition(1, 6);
                ChessPosition wKSSquare2 = new ChessPosition(1, 7);
                if (board.getPiece(wKSSquare1) == null && board.getPiece(wKSSquare2) == null) {
                    ChessGame simulatedGame = new ChessGame();
                    simulatedGame.setTeamTurn(color);
                    ChessMove wKSMove1 = new ChessMove(wKOrigin, wKSSquare1, null);
                    ChessMove wKSMove2 = new ChessMove(wKOrigin, wKSSquare2, null);
                    ChessBoard simulatedBoard1 = simulatedGame.simulateMove(wKSMove1, board);
                    ChessBoard simulatedBoard2 = simulatedGame.simulateMove(wKSMove2, board);
                    simulatedGame.setBoard(simulatedBoard1);
                    if (!simulatedGame.isInCheck(color)) {
                        simulatedGame.setBoard(simulatedBoard2);
                        return !simulatedGame.isInCheck(color);
                    }
                }
            }
        } else {
            if (!isBlackKSRookMoved() && board.getPiece(new ChessPosition(8, 8)) != null && board.getPiece(new ChessPosition(8, 8)).equals(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK))) {
                ChessPosition bKOrigin = new ChessPosition(8, 5);
                ChessPosition bKSSquare1 = new ChessPosition(8, 6);
                ChessPosition bKSSquare2 = new ChessPosition(8, 7);
                if (board.getPiece(bKSSquare1) == null && board.getPiece(bKSSquare2) == null) {
                    ChessGame simulatedGame = new ChessGame();
                    simulatedGame.setTeamTurn(color);
                    ChessMove bKSMove1 = new ChessMove(bKOrigin, bKSSquare1, null);
                    ChessMove bKSMove2 = new ChessMove(bKOrigin, bKSSquare2, null);
                    ChessBoard simulatedBoard1 = simulatedGame.simulateMove(bKSMove1, board);
                    ChessBoard simulatedBoard2 = simulatedGame.simulateMove(bKSMove2, board);
                    simulatedGame.setBoard(simulatedBoard1);
                    if (!simulatedGame.isInCheck(color)) {
                        simulatedGame.setBoard(simulatedBoard2);
                        return !simulatedGame.isInCheck(color);
                    }
                }
            }
        }
        return false;
    }

    public boolean canQSCastle (ChessBoard board, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            if (!isWhiteQSRookMoved() && board.getPiece(new ChessPosition(1, 1)) != null && board.getPiece(new ChessPosition(1, 1)).equals(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK))) {
                ChessPosition wKOrigin = new ChessPosition(1, 5);
                ChessPosition wQSSquare1 = new ChessPosition(1, 4);
                ChessPosition wQSSquare2 = new ChessPosition(1, 3);
                if (board.getPiece(wQSSquare1) == null && board.getPiece(wQSSquare2) == null) {
                    ChessGame simulatedGame = new ChessGame();
                    simulatedGame.setTeamTurn(color);
                    ChessMove wQSMove1 = new ChessMove(wKOrigin, wQSSquare1, null);
                    ChessMove wQSMove2 = new ChessMove(wKOrigin, wQSSquare2, null);
                    ChessBoard simulatedBoard1 = simulatedGame.simulateMove(wQSMove1, board);
                    ChessBoard simulatedBoard2 = simulatedGame.simulateMove(wQSMove2, board);
                    simulatedGame.setBoard(simulatedBoard1);
                    if (!simulatedGame.isInCheck(color)) {
                        simulatedGame.setBoard(simulatedBoard2);
                        return !simulatedGame.isInCheck(color);
                    }
                }
            }
        } else {
            if (!isBlackQSRookMoved() && board.getPiece(new ChessPosition(8, 1)) != null && board.getPiece(new ChessPosition(8, 1)).equals(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK))) {
                ChessPosition bKOrigin = new ChessPosition(8, 5);
                ChessPosition bQSSquare1 = new ChessPosition(8, 4);
                ChessPosition bQSSquare2 = new ChessPosition(8, 3);
                if (board.getPiece(bQSSquare1) == null && board.getPiece(bQSSquare2) == null) {
                    ChessGame simulatedGame = new ChessGame();
                    simulatedGame.setTeamTurn(color);
                    ChessMove bQSMove1 = new ChessMove(bKOrigin, bQSSquare1, null);
                    ChessMove bQSMove2 = new ChessMove(bKOrigin, bQSSquare2, null);
                    ChessBoard simulatedBoard1 = simulatedGame.simulateMove(bQSMove1, board);
                    ChessBoard simulatedBoard2 = simulatedGame.simulateMove(bQSMove2, board);
                    simulatedGame.setBoard(simulatedBoard1);
                    if (!simulatedGame.isInCheck(color)) {
                        simulatedGame.setBoard(simulatedBoard2);
                        return !simulatedGame.isInCheck(color);
                    }
                }
            }
        }
        return false;
    }

    public Collection<ChessMove> possibleCastlingMoves(ChessBoard board, ChessGame.TeamColor color) {
        List<ChessMove> possibleCastlingMoves = new ArrayList<>();
        if (color == ChessGame.TeamColor.WHITE) {
            if (isWhiteKingMoved() || board.getPiece(new ChessPosition(1, 5)) == null || !board.getPiece(new ChessPosition(1, 5)).equals(new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING))) {
                return possibleCastlingMoves;
            }
            if (canKSCastle(board, color)) {
                ChessMove whiteKSCastle = new ChessMove(new ChessPosition(1, 5), new ChessPosition(1, 7), null);
                possibleCastlingMoves.add(whiteKSCastle);
            }
            if (canQSCastle(board, color)) {
                ChessMove whiteQSCastle = new ChessMove(new ChessPosition(1, 5), new ChessPosition(1, 3), null);
                possibleCastlingMoves.add(whiteQSCastle);
            }
        } else {
            if (isBlackKingMoved() || board.getPiece(new ChessPosition(8, 5)) == null || !board.getPiece(new ChessPosition(8, 5)).equals(new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING))) {
                return possibleCastlingMoves;
            }
            if (canKSCastle(board, color)) {
                ChessMove blackKSCastle = new ChessMove(new ChessPosition(8, 5), new ChessPosition(8, 7), null);
                possibleCastlingMoves.add(blackKSCastle);
            }
            if (canQSCastle(board, color)) {
                ChessMove blackQSCastle = new ChessMove(new ChessPosition(8, 5), new ChessPosition(8, 3), null);
                possibleCastlingMoves.add(blackQSCastle);
            }
        }
        return possibleCastlingMoves;
    }
}

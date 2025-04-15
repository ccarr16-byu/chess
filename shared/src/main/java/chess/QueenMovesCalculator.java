package chess;

import java.util.ArrayList;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        int currentRow = position.getRow();
        int currentColumn = position.getColumn();
        if (board.getPiece(position) == null) {
            return moves;
        }
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        var horizontalMoves = getHorizontalMoves(board, position, currentRow, currentColumn, color);
        var diagonalMoves = getDiagonalMoves(board, position, currentRow, currentColumn, color);
        moves.addAll(horizontalMoves);
        moves.addAll(diagonalMoves);
        return moves;
    }

    public static List<ChessMove> getHorizontalMoves(ChessBoard board, ChessPosition position, int currentRow,
                                                     int currentColumn,
                                              ChessGame.TeamColor color) {
        List<ChessMove> moves = new ArrayList<>();
        for (int i = currentRow + 1; i < 9; i++) {
            var potentialMove = isValidMove(board, position, i, currentColumn, color);
            if (potentialMove != null) {
                moves.add(potentialMove);
                if (isKillingMove(board, potentialMove, color)) {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = currentRow - 1; i > 0; i--) {
            var potentialMove = isValidMove(board, position, i, currentColumn, color);
            if (potentialMove != null) {
                moves.add(potentialMove);
                if (isKillingMove(board, potentialMove, color)) {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = currentColumn + 1; i < 9; i++) {
            var potentialMove = isValidMove(board, position, currentRow, i, color);
            if (potentialMove != null) {
                moves.add(potentialMove);
                if (isKillingMove(board, potentialMove, color)) {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = currentColumn - 1; i > 0; i--) {
            var potentialMove = isValidMove(board, position, currentRow, i, color);
            if (potentialMove != null) {
                moves.add(potentialMove);
                if (isKillingMove(board, potentialMove, color)) {
                    break;
                }
            } else {
                break;
            }
        }
        return moves;
    }

    public static List<ChessMove> getDiagonalMoves(ChessBoard board, ChessPosition position, int currentRow,
                                                   int currentColumn, ChessGame.TeamColor color) {
        List<ChessMove> moves = new ArrayList<>();
        for (int i = currentRow + 1, j = currentColumn + 1; i < 9 && j < 9; i++, j++) {
            var potentialMove = isValidMove(board, position, i, j, color);
            if (potentialMove != null) {
                moves.add(potentialMove);
                if (isKillingMove(board, potentialMove, color)) {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = currentRow - 1, j = currentColumn - 1; i > 0 && j > 0; i--, j--) {
            var potentialMove = isValidMove(board, position, i, j, color);
            if (potentialMove != null) {
                moves.add(potentialMove);
                if (isKillingMove(board, potentialMove, color)) {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = currentRow + 1, j = currentColumn - 1; i < 9 && j > 0; i++, j--) {
            var potentialMove = isValidMove(board, position, i, j, color);
            if (potentialMove != null) {
                moves.add(potentialMove);
                if (isKillingMove(board, potentialMove, color)) {
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = currentRow - 1, j = currentColumn + 1; i > 0 && j < 9; i--, j++) {
            var potentialMove = isValidMove(board, position, i, j, color);
            if (potentialMove != null) {
                moves.add(potentialMove);
                if (isKillingMove(board, potentialMove, color)) {
                    break;
                }
            } else {
                break;
            }
        }
        return moves;
    }

    public static ChessMove isValidMove(ChessBoard board, ChessPosition position, int row, int column,
                                       ChessGame.TeamColor color) {
        ChessMove move;
        if ((board.getPiece(new ChessPosition(row, column)) == null) ||
                (board.getPiece(new ChessPosition(row, column)).getTeamColor() != color)) {
            move = new ChessMove(position, new ChessPosition(row, column), null);
        } else {
            move = null;
        }
        return move;
    }

    public static boolean isKillingMove(ChessBoard board, ChessMove move, ChessGame.TeamColor color) {
        var endPosition = move.getEndPosition();
        var endPiece = board.getPiece(endPosition);
        if (endPiece == null) {
            return false;
        }
        return board.getPiece(move.getEndPosition()).getTeamColor() != color;
    }
}

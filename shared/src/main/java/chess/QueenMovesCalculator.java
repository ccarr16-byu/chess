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
            if (board.getPiece(new ChessPosition(i, currentColumn)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(i, currentColumn), null));
            } else {
                if (board.getPiece(new ChessPosition(i, currentColumn)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(i, currentColumn), null));
                }
                break;
            }
        }
        for (int i = currentRow - 1; i > 0; i--) {
            if (board.getPiece(new ChessPosition(i, currentColumn)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(i, currentColumn), null));
            } else {
                if (board.getPiece(new ChessPosition(i, currentColumn)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(i, currentColumn), null));
                }
                break;
            }
        }
        for (int i = currentColumn + 1; i < 9; i++) {
            if (board.getPiece(new ChessPosition(currentRow, i)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow, i), null));
            } else {
                if (board.getPiece(new ChessPosition(currentRow, i)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(currentRow, i), null));
                }
                break;
            }
        }
        for (int i = currentColumn - 1; i > 0; i--) {
            if (board.getPiece(new ChessPosition(currentRow, i)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow, i), null));
            } else {
                if (board.getPiece(new ChessPosition(currentRow, i)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(currentRow, i), null));
                }
                break;
            }
        }
        return moves;
    }

    public static List<ChessMove> getDiagonalMoves(ChessBoard board, ChessPosition position, int currentRow,
                                                   int currentColumn, ChessGame.TeamColor color) {
        List<ChessMove> moves = new ArrayList<>();
        for (int i = currentRow + 1, j = currentColumn + 1; i < 9 && j < 9; i++, j++) {
            if (board.getPiece(new ChessPosition(i, j)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(i, j), null));
            } else {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
                break;
            }
        }
        for (int i = currentRow - 1, j = currentColumn - 1; i > 0 && j > 0; i--, j--) {
            if (board.getPiece(new ChessPosition(i, j)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(i, j), null));
            } else {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
                break;
            }
        }
        for (int i = currentRow + 1, j = currentColumn - 1; i < 9 && j > 0; i++, j--) {
            if (board.getPiece(new ChessPosition(i, j)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(i, j), null));
            } else {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
                break;
            }
        }
        for (int i = currentRow - 1, j = currentColumn + 1; i > 0 && j < 9; i--, j++) {
            if (board.getPiece(new ChessPosition(i, j)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(i, j), null));
            } else {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != color) {
                    moves.add(new ChessMove(position, new ChessPosition(i, j), null));
                }
                break;
            }
        }
        return moves;
    }
}

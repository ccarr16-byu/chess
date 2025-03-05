package chess;

import java.util.ArrayList;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        int currentRow = position.getRow();
        int currentColumn = position.getColumn();
        if (board.getPiece(position) == null) {
            return moves;
        }
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        if (color == ChessGame.TeamColor.WHITE) {
            if (currentRow == 2 && board.getPiece(new ChessPosition(currentRow + 1, currentColumn)) == null &&
                    board.getPiece(new ChessPosition(currentRow + 2, currentColumn)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow + 2, currentColumn),
                        null));
            }
            if (currentRow < 7) {
                if (board.getPiece(new ChessPosition(currentRow + 1, currentColumn)) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn),
                            null));
                }
                if (currentColumn > 1 && board.getPiece(new ChessPosition(currentRow + 1,
                        currentColumn - 1)) != null) {
                    if (board.getPiece(new ChessPosition(currentRow + 1,
                            currentColumn - 1)).getTeamColor() != ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1),
                                null));
                    }
                }
                if (currentColumn < 8 &&
                        board.getPiece(new ChessPosition(currentRow + 1, currentColumn + 1)) != null) {
                    if (board.getPiece(new ChessPosition(currentRow + 1, currentColumn + 1)).getTeamColor() !=
                            ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1),
                                null));
                    }
                }
            }
            if (currentRow == 7) {
                if (board.getPiece(new ChessPosition(currentRow + 1, currentColumn)) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn),
                            ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn),
                            ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn),
                            ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn),
                            ChessPiece.PieceType.QUEEN));
                }
                if (currentColumn > 1 &&
                        board.getPiece(new ChessPosition(currentRow + 1, currentColumn - 1)) != null) {
                    if (board.getPiece(new ChessPosition(currentRow + 1, currentColumn - 1)).getTeamColor() !=
                            ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1),
                                ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1),
                                ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1),
                                ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1),
                                ChessPiece.PieceType.QUEEN));
                    }
                }
                if (currentColumn < 8 &&
                        board.getPiece(new ChessPosition(currentRow + 1, currentColumn + 1)) != null) {
                    if (board.getPiece(new ChessPosition(currentRow + 1, currentColumn + 1)).getTeamColor() !=
                            ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1),
                                ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1),
                                ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1),
                                ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1),
                                ChessPiece.PieceType.QUEEN));
                    }
                }
            }
        } else {
            if (currentRow == 7 && board.getPiece(new ChessPosition(currentRow - 1, currentColumn)) == null &&
                    board.getPiece(new ChessPosition(currentRow - 2, currentColumn)) == null) {
                moves.add(new ChessMove(position, new ChessPosition(currentRow - 2, currentColumn),
                        null));
            }
            if (currentRow > 2) {
                if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn)) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn),
                            null));
                }
                if (currentColumn > 1 &&
                        board.getPiece(new ChessPosition(currentRow - 1, currentColumn - 1)) != null) {
                    if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn - 1)).getTeamColor() !=
                            ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1),
                                null));
                    }
                }
                if (currentColumn < 8 &&
                        board.getPiece(new ChessPosition(currentRow - 1, currentColumn + 1)) != null) {
                    if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn + 1)).getTeamColor() !=
                            ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1),
                                null));
                    }
                }
            }
            if (currentRow == 2) {
                if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn)) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn),
                            ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn),
                            ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn),
                            ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn),
                            ChessPiece.PieceType.QUEEN));
                }
                if (currentColumn > 1 &&
                        board.getPiece(new ChessPosition(currentRow - 1, currentColumn - 1)) != null) {
                    if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn - 1)).getTeamColor() !=
                            ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1),
                                ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1),
                                ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1),
                                ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1),
                                ChessPiece.PieceType.QUEEN));
                    }
                }
                if (currentColumn < 8 &&
                        board.getPiece(new ChessPosition(currentRow - 1, currentColumn + 1)) != null) {
                    if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn + 1)).getTeamColor() !=
                            ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1),
                                ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1),
                                ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1),
                                ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1),
                                ChessPiece.PieceType.QUEEN));
                    }
                }
            }
        }
        return moves;
    }
}

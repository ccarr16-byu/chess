package chess;

import java.util.ArrayList;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public List<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<ChessMove> moves = new ArrayList<>();
        if (board.getPiece(position) == null) {
            return moves;
        }
        ChessPiece piece = board.getPiece(position);
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (position.getRow() == 2 && board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn())) == null && board.getPiece(new ChessPosition(position.getRow() + 2, position.getColumn())) == null) {
                moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 2, position.getColumn()), null));
            }
            if (position.getRow() < 7) {
                if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn())) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn()), null));
                }
                if (position.getColumn() > 1 && board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 1)).getTeamColor() != ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() - 1), null));
                    }
                }
                if (position.getColumn() < 8 && board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 1)).getTeamColor() != ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 1), null));
                    }
                }
            }
            if (position.getRow() == 7) {
                if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn())) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn()), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn()), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn()), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn()), ChessPiece.PieceType.QUEEN));
                }
                if (position.getColumn() > 1 && board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() - 1)).getTeamColor() != ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() - 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() - 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                    }
                }
                if (position.getColumn() < 8 && board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow() + 1, position.getColumn() + 1)).getTeamColor() != ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                    }
                }
            }
        } else {
            if (position.getRow() == 7 && board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn())) == null && board.getPiece(new ChessPosition(position.getRow() - 2, position.getColumn())) == null) {
                moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 2, position.getColumn()), null));
            }
            if (position.getRow() > 2) {
                if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn())) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn()), null));
                }
                if (position.getColumn() > 1 && board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 1)).getTeamColor() != ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 1), null));
                    }
                }
                if (position.getColumn() < 8 && board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 1)).getTeamColor() != ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 1), null));
                    }
                }
            }
            if (position.getRow() == 2) {
                if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn())) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn()), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn()), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn()), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn()), ChessPiece.PieceType.QUEEN));
                }
                if (position.getColumn() > 1 && board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() - 1)).getTeamColor() != ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                    }
                }
                if (position.getColumn() < 8 && board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 1)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow() - 1, position.getColumn() + 1)).getTeamColor() != ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                    }
                }
            }
        }
        return moves;
    }
}

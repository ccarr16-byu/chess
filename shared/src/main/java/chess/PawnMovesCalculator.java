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
//        if (color == ChessGame.TeamColor.WHITE) {
//            if (currentRow == 2 && board.getPiece(new ChessPosition(currentRow + 1, currentColumn)) == null &&
//                    board.getPiece(new ChessPosition(currentRow + 2, currentColumn)) == null) {
//                moves.add(new ChessMove(position, new ChessPosition(currentRow + 2, currentColumn),
//                        null));
//            }
//            if (currentRow < 7) {
//                if (board.getPiece(new ChessPosition(currentRow + 1, currentColumn)) == null) {
//                    moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn),
//                            null));
//                }
//                if (currentColumn > 1 && board.getPiece(new ChessPosition(currentRow + 1,
//                        currentColumn - 1)) != null) {
//                    if (board.getPiece(new ChessPosition(currentRow + 1,
//                            currentColumn - 1)).getTeamColor() != ChessGame.TeamColor.WHITE) {
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1),
//                                null));
//                    }
//                }
//                if (currentColumn < 8 &&
//                        board.getPiece(new ChessPosition(currentRow + 1, currentColumn + 1)) != null) {
//                    if (board.getPiece(new ChessPosition(currentRow + 1, currentColumn + 1)).getTeamColor() !=
//                            ChessGame.TeamColor.WHITE) {
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1),
//                                null));
//                    }
//                }
//            }
//            if (currentRow == 7) {
//                if (board.getPiece(new ChessPosition(currentRow + 1, currentColumn)) == null) {
//                    moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn),
//                            ChessPiece.PieceType.KNIGHT));
//                    moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn),
//                            ChessPiece.PieceType.BISHOP));
//                    moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn),
//                            ChessPiece.PieceType.ROOK));
//                    moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn),
//                            ChessPiece.PieceType.QUEEN));
//                }
//                if (currentColumn > 1 &&
//                        board.getPiece(new ChessPosition(currentRow + 1, currentColumn - 1)) != null) {
//                    if (board.getPiece(new ChessPosition(currentRow + 1, currentColumn - 1)).getTeamColor() !=
//                            ChessGame.TeamColor.WHITE) {
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1),
//                                ChessPiece.PieceType.KNIGHT));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1),
//                                ChessPiece.PieceType.BISHOP));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1),
//                                ChessPiece.PieceType.ROOK));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn - 1),
//                                ChessPiece.PieceType.QUEEN));
//                    }
//                }
//                if (currentColumn < 8 &&
//                        board.getPiece(new ChessPosition(currentRow + 1, currentColumn + 1)) != null) {
//                    if (board.getPiece(new ChessPosition(currentRow + 1, currentColumn + 1)).getTeamColor() !=
//                            ChessGame.TeamColor.WHITE) {
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1),
//                                ChessPiece.PieceType.KNIGHT));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1),
//                                ChessPiece.PieceType.BISHOP));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1),
//                                ChessPiece.PieceType.ROOK));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow + 1, currentColumn + 1),
//                                ChessPiece.PieceType.QUEEN));
//                    }
//                }
//            }
//        } else {
//            if (currentRow == 7 && board.getPiece(new ChessPosition(currentRow - 1, currentColumn)) == null &&
//                    board.getPiece(new ChessPosition(currentRow - 2, currentColumn)) == null) {
//                moves.add(new ChessMove(position, new ChessPosition(currentRow - 2, currentColumn),
//                        null));
//            }
//            if (currentRow > 2) {
//                if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn)) == null) {
//                    moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn),
//                            null));
//                }
//                if (currentColumn > 1 &&
//                        board.getPiece(new ChessPosition(currentRow - 1, currentColumn - 1)) != null) {
//                    if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn - 1)).getTeamColor() !=
//                            ChessGame.TeamColor.BLACK) {
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1),
//                                null));
//                    }
//                }
//                if (currentColumn < 8 &&
//                        board.getPiece(new ChessPosition(currentRow - 1, currentColumn + 1)) != null) {
//                    if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn + 1)).getTeamColor() !=
//                            ChessGame.TeamColor.BLACK) {
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1),
//                                null));
//                    }
//                }
//            }
//            if (currentRow == 2) {
//                if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn)) == null) {
//                    moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn),
//                            ChessPiece.PieceType.KNIGHT));
//                    moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn),
//                            ChessPiece.PieceType.BISHOP));
//                    moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn),
//                            ChessPiece.PieceType.ROOK));
//                    moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn),
//                            ChessPiece.PieceType.QUEEN));
//                }
//                if (currentColumn > 1 &&
//                        board.getPiece(new ChessPosition(currentRow - 1, currentColumn - 1)) != null) {
//                    if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn - 1)).getTeamColor() !=
//                            ChessGame.TeamColor.BLACK) {
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1),
//                                ChessPiece.PieceType.KNIGHT));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1),
//                                ChessPiece.PieceType.BISHOP));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1),
//                                ChessPiece.PieceType.ROOK));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn - 1),
//                                ChessPiece.PieceType.QUEEN));
//                    }
//                }
//                if (currentColumn < 8 &&
//                        board.getPiece(new ChessPosition(currentRow - 1, currentColumn + 1)) != null) {
//                    if (board.getPiece(new ChessPosition(currentRow - 1, currentColumn + 1)).getTeamColor() !=
//                            ChessGame.TeamColor.BLACK) {
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1),
//                                ChessPiece.PieceType.KNIGHT));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1),
//                                ChessPiece.PieceType.BISHOP));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1),
//                                ChessPiece.PieceType.ROOK));
//                        moves.add(new ChessMove(position, new ChessPosition(currentRow - 1, currentColumn + 1),
//                                ChessPiece.PieceType.QUEEN));
//                    }
//                }
//            }
//        }
//        return moves;
//    }
        return moves;
    }

    public ChessPiece checkSquare(int row, int col, ChessBoard board) {
        return board.getPiece(new ChessPosition(row, col));
    }

    public void addMoves(int row, int col, ChessBoard board, ChessGame.TeamColor color,
                                  ArrayList<ChessMove> moves) {
        if ((color == ChessGame.TeamColor.WHITE) && (row == 7)) {
            checkPromotionMoves(row, col, board, color, moves);
        } else if ((color == ChessGame.TeamColor.BLACK) && (row == 2)) {
            checkPromotionMoves(row, col, board, color, moves);
        } else {
            checkNormalMoves(row, col, board, color, moves);
        }
    }

    public void checkNormalMoves(int row, int col, ChessBoard board, ChessGame.TeamColor color,
                                 ArrayList<ChessMove> moves) {
        if (color == ChessGame.TeamColor.WHITE) {
            if (row == 2) {
                if ((checkSquare(row + 1, col, board) == null) && (checkSquare(row + 2, col, board) == null)) {
                    moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 2, col),
                            null));
                }
            }
            if (checkSquare(row + 1, col, board) == null) {
                moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col),
                        null));
            }
        } else {
            if (row == 7) {
                if ((checkSquare(row - 1, col, board) == null) && (checkSquare(row - 2, col, board) == null)) {
                    moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 2, col),
                            null));
                }
            }
            if (checkSquare(row - 1, col, board) == null) {
                moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col),
                        null));
            }
        }
        checkNormalKillingMoves(row, col, board, color, moves);
    }

    public void checkPromotionMoves(int row, int col, ChessBoard board, ChessGame.TeamColor color,
                                 ArrayList<ChessMove> moves) {
        if (color == ChessGame.TeamColor.WHITE) {

        } else {

        }
    }

    public void checkNormalKillingMoves (int row, int col, ChessBoard board, ChessGame.TeamColor color,
                                         ArrayList<ChessMove> moves) {
        if (color == ChessGame.TeamColor.WHITE) {
            if (col > 1) {
                ChessPiece diagonalPiece = checkSquare(row + 1, col - 1, board);
                if (diagonalPiece != null) {
                    if (diagonalPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(new ChessPosition(row, col),
                                new ChessPosition(row + 1, col - 1), null));
                    }
                }
            }
            if (col < 8) {
                ChessPiece diagonalPiece = checkSquare(row + 1, col + 1, board);
                if (diagonalPiece != null) {
                    if (diagonalPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        moves.add(new ChessMove(new ChessPosition(row, col),
                                new ChessPosition(row + 1, col + 1), null));
                    }
                }
            }
        } else {
            if (col > 1) {
                ChessPiece diagonalPiece = checkSquare(row - 1, col - 1, board);
                if (diagonalPiece != null) {
                    if (diagonalPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(new ChessPosition(row, col),
                                new ChessPosition(row - 1, col - 1), null));
                    }
                }
            }
            if (col < 8) {
                ChessPiece diagonalPiece = checkSquare(row - 1, col + 1, board);
                if (diagonalPiece != null) {
                    if (diagonalPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        moves.add(new ChessMove(new ChessPosition(row, col),
                                new ChessPosition(row - 1, col + 1), null));
                    }
                }
            }
        }
    }
}

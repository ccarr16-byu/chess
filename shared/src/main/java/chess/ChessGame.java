package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    private CastlingTracker castlingTracker;
    private EnPassantTracker enPassantTracker;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
        castlingTracker = new CastlingTracker();
        enPassantTracker = new EnPassantTracker();
    }

    public ChessGame(ChessGame copy) {
        board = new ChessBoard(copy.getBoard());
        teamTurn = copy.getTeamTurn();
        castlingTracker = new CastlingTracker();
        enPassantTracker = copy.getEnPassantTracker();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public ChessPosition getKingPosition(TeamColor color) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition square = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(square);
                if (piece == null) {
                    continue;
                }
                if (piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                    return square;
                }
            }
        }
        return null;
    }

    public ChessBoard simulateMove(ChessMove move, ChessBoard oldBoard) {
        ChessBoard newBoard = new ChessBoard(oldBoard);
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = oldBoard.getPiece(start);
        newBoard.addPiece(end, piece);
        newBoard.addPiece(start, null);
        return newBoard;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        if (board.getPiece(startPosition) == null) {
            return validMoves;
        }
        if (ChessPiece.pieceMoves(board, startPosition) == null) {
            return validMoves;
        }
        TeamColor color = board.getPiece(startPosition).getTeamColor();
        for (ChessMove move : Objects.requireNonNull(ChessPiece.pieceMoves(board, startPosition))) {
            ChessBoard simulatedBoard = simulateMove(move, board);
            ChessGame simulatedGame = new ChessGame();
            simulatedGame.setBoard(simulatedBoard);
            if (!simulatedGame.isInCheck(color)) {
                validMoves.add(move);
            }
        }
        if (board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING) {
            validMoves.addAll(castlingTracker.possibleCastlingMoves(board, color));
        }
        if (board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
            if (enPassantTracker.isEnPassantAllowed() && enPassantTracker.getAttackingSquares().contains(startPosition)) {
                validMoves.add(new ChessMove(startPosition, enPassantTracker.getKillDestination(), null));
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        if (piece == null) {
            throw new InvalidMoveException("Invalid move!");
        }
        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Invalid move!");
        }
        if (!validMoves(start).contains(move)) {
            throw new InvalidMoveException("Invalid move!");
        }
        if (move.getPromotionPiece() == null) {
            board.addPiece(end, piece);
            board.addPiece(start, null);
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                if (enPassantTracker.isEnPassantAllowed()) {
                    enPassantTracker.setEnPassantAllowed();
                }
                ChessMove whiteKSCastle = new ChessMove(new ChessPosition(1, 5), new ChessPosition(1, 7), null);
                ChessMove whiteQSCastle = new ChessMove(new ChessPosition(1, 5), new ChessPosition(1, 3), null);
                ChessMove blackKSCastle = new ChessMove(new ChessPosition(8, 5), new ChessPosition(8, 7), null);
                ChessMove blackQSCastle = new ChessMove(new ChessPosition(8, 5), new ChessPosition(8, 3), null);
                if (move.equals(whiteKSCastle)) {
                    board.addPiece(new ChessPosition(1, 6), new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                    board.addPiece(new ChessPosition(1, 8), null);
                } else if (move.equals(whiteQSCastle)) {
                    board.addPiece(new ChessPosition(1, 4), new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                    board.addPiece(new ChessPosition(1, 1), null);
                } else if (move.equals(blackKSCastle)) {
                    board.addPiece(new ChessPosition(8, 6), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                    board.addPiece(new ChessPosition(8, 8), null);
                } else if (move.equals(blackQSCastle)) {
                    board.addPiece(new ChessPosition(8, 4), new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                    board.addPiece(new ChessPosition(8, 1), null);
                }
            } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                if (move.getEndPosition().getRow() == move.getStartPosition().getRow() + 2 || move.getEndPosition().getRow() == move.getStartPosition().getRow() - 2) {
                    enPassantTracker = new EnPassantTracker(move.getEndPosition(), piece.getTeamColor());
                } else if (enPassantTracker.isEnPassantAllowed() && enPassantTracker.getKillDestination().equals(move.getEndPosition())) {
                    board.addPiece(new ChessPosition(enPassantTracker.getVulnerablePawn()), null);
                    enPassantTracker.setEnPassantAllowed();
                } else {
                    enPassantTracker.setEnPassantAllowed();
                }
            } else {
                if (enPassantTracker.isEnPassantAllowed()) {
                    enPassantTracker.setEnPassantAllowed();
                }
            }
        } else {
            if (enPassantTracker.isEnPassantAllowed()) {
                enPassantTracker.setEnPassantAllowed();
            }
            switch (move.getPromotionPiece()) {
                case KNIGHT -> board.addPiece(end, new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.KNIGHT));
                case BISHOP -> board.addPiece(end, new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.BISHOP));
                case ROOK -> board.addPiece(end, new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.ROOK));
                case QUEEN -> board.addPiece(end, new ChessPiece(piece.getTeamColor(), ChessPiece.PieceType.QUEEN));
            }
            board.addPiece(start, null);
        }
        if (teamTurn == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
        if (start.equals(new ChessPosition(1, 5)) || end.equals(new ChessPosition(1, 5))) {
            castlingTracker.setWhiteKingMoved(true);
        } else if (start.equals(new ChessPosition(1, 8)) || end.equals(new ChessPosition(1, 8))) {
            castlingTracker.setWhiteKSRookMoved(true);
        } else if (start.equals(new ChessPosition(1, 1)) || end.equals(new ChessPosition(1, 1))) {
            castlingTracker.setWhiteQSRookMoved(true);
        } else if (start.equals(new ChessPosition(8, 5)) || end.equals(new ChessPosition(8, 5))) {
            castlingTracker.setBlackKingMoved(true);
        } else if (start.equals(new ChessPosition(8, 8)) || end.equals(new ChessPosition(8, 8))) {
            castlingTracker.setBlackKSRookMoved(true);
        } else if (start.equals(new ChessPosition(8, 1)) || end.equals(new ChessPosition(8, 1))) {
            castlingTracker.setBlackQSRookMoved(true);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = getKingPosition(teamColor);
        List<ChessPosition> targetedSquares = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition checkPosition = new ChessPosition(i, j);
                ChessPiece checkPiece = board.getPiece(checkPosition);
                if (checkPiece != null && checkPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> enemyMoves = ChessPiece.pieceMoves(board, checkPosition);
                    if (enemyMoves == null) {
                        continue;
                    }
                    for (ChessMove enemyMove : enemyMoves) {
                        targetedSquares.add(enemyMove.getEndPosition());
                    }
                }
            }
        }
        return (targetedSquares.contains(kingPos));
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition square = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(square);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(square).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition square = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(square);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(square).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        castlingTracker = new CastlingTracker();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    public EnPassantTracker getEnPassantTracker() {
        return this.enPassantTracker;
    }
}

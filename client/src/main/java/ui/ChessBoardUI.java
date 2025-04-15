package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    public static void drawChessBoard(ChessGame.TeamColor team, ChessBoard board, int method, ChessMove lastMove, ChessPosition position, Collection<ChessMove> validMoves) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_TEXT_ITALIC);
        drawBoard(out, team, board, method, lastMove, position, validMoves);
    }

    private static HashMap<Integer, String> getPiecePositions(ChessGame.TeamColor team, ChessBoard board) {
        HashMap<Integer, String> piecePositions = new HashMap<>();
        var boardPositions = board.getBoard();
        if (team == ChessGame.TeamColor.WHITE) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (boardPositions[i][j] != null) {
                        piecePositions.put(((i + 1) * 10) + (j + 1), convertPieceToString(boardPositions[i][j]));
                    }
                }
            }
        } else {
            for (int i = 0, x = 7; i < 8; i++, x--) {
                for (int j = 0, y = 7; j < 8; j++, y--) {
                    if (boardPositions[i][j] != null) {
                        piecePositions.put(((x + 1) * 10) + (y + 1), convertPieceToString(boardPositions[i][j]));
                    }
                }
            }
        }
        return piecePositions;
    }

    private static void drawBoard(PrintStream out, ChessGame.TeamColor team, ChessBoard board, int method,
                                  ChessMove lastMove, ChessPosition position, Collection<ChessMove> validMoves) {
        var piecePositions = getPiecePositions(team, board);
        ArrayList<ChessPosition> validEndSquares = new ArrayList<>();
        if (validMoves != null) {
            for (ChessMove move : validMoves) {
                validEndSquares.add(move.getEndPosition());
            }
        }
        for (int i = 0; i < ((BOARD_SIZE_IN_SQUARES + 2) * SQUARE_SIZE_IN_PADDED_CHARS); i++) {
            if ((i / SQUARE_SIZE_IN_PADDED_CHARS) == 0 ||
                    (i / SQUARE_SIZE_IN_PADDED_CHARS) == BOARD_SIZE_IN_SQUARES + 1) {
                out.print(printColumnHeaders(team));
                out.print("\n");
                continue;
            }
            for (int j = 0; j <= BOARD_SIZE_IN_SQUARES + 1; j++) {
                if (j == 0 || j == (BOARD_SIZE_IN_SQUARES + 1)) {
                    out.print(RESET_BG_COLOR);
                    out.print(printRowHeader(i, team));
                    continue;
                }
                ChessPosition currentSquare;
                if (team == WHITE) {
                    currentSquare = new ChessPosition(9 - (i / SQUARE_SIZE_IN_PADDED_CHARS), j);
                } else {
                    currentSquare = new ChessPosition((i / SQUARE_SIZE_IN_PADDED_CHARS), 9 - j);
                }
                if (((i / SQUARE_SIZE_IN_PADDED_CHARS) + j) % 2 == 0) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    out.print(SET_BG_COLOR_DARK_GREY);
                }
                if (method == 1 && currentSquare.equals(lastMove.getStartPosition())) {
                    out.print(SET_BG_COLOR_GREEN);
                } else if (method == 1 && currentSquare.equals(lastMove.getEndPosition())) {
                    out.print(SET_BG_COLOR_DARK_GREEN);
                }
                if (method == 2 && validEndSquares.contains(currentSquare)) {
                    out.print(SET_BG_COLOR_YELLOW);
                }
                if ((i % SQUARE_SIZE_IN_PADDED_CHARS) == (SQUARE_SIZE_IN_PADDED_CHARS / 2)) {
                    out.print(printPiece(i, j, piecePositions));
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }
            }

            out.print(RESET_BG_COLOR + "\n");
        }
    }

    private static String printPiece(int i, int j, HashMap<Integer, String> piecePositions) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int postfixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
        int piecePosition = ((9 - (i / SQUARE_SIZE_IN_PADDED_CHARS)) * 10) + j;
        String piece = piecePositions.getOrDefault(piecePosition, EMPTY);
        return EMPTY.repeat(prefixLength) + piece + EMPTY.repeat(postfixLength);
    }

    private static String printColumnHeaders(ChessGame.TeamColor team) {
        var returnString = new StringBuilder();
        List<String> headers = new ArrayList<>();
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int postfixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
        if (team == ChessGame.TeamColor.WHITE) {
            headers.add(" a\u2003");
            headers.add(" b\u2003");
            headers.add(" c\u2003");
            headers.add(" d\u2003");
            headers.add(" e\u2003");
            headers.add(" f\u2003");
            headers.add(" g\u2003");
            headers.add(" h\u2003");
        } else {
            headers.add(" h\u2003");
            headers.add(" g\u2003");
            headers.add(" f\u2003");
            headers.add(" e\u2003");
            headers.add(" d\u2003");
            headers.add(" c\u2003");
            headers.add(" b\u2003");
            headers.add(" a\u2003");
        }
        for (int j = 0; j < (BOARD_SIZE_IN_SQUARES + 2); j++) {
            if (j == 0 || j == (BOARD_SIZE_IN_SQUARES + 1)) {
                returnString.append(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
            } else {
                returnString.append(EMPTY.repeat(prefixLength));
                returnString.append(headers.get(j - 1));
                returnString.append(EMPTY.repeat(postfixLength));
            }
        }
        return returnString.toString();
    }

    private static String printRowHeader(int i, ChessGame.TeamColor team) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int postfixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
        int row;
        if (team == ChessGame.TeamColor.WHITE) {
            row = 9 - i;
        } else {
            row = i;
        }
        String header = " " + Integer.toString(row) + "\u2003";
        return EMPTY.repeat(prefixLength) + header + EMPTY.repeat(postfixLength);
    }

    private static String convertPieceToString(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        if (piece.getTeamColor() == WHITE) {
            switch (piece.getPieceType()) {
                case ChessPiece.PieceType.KING -> {
                    return WHITE_KING;
                }
                case ChessPiece.PieceType.QUEEN -> {
                    return WHITE_QUEEN;
                }
                case ChessPiece.PieceType.ROOK -> {
                    return WHITE_ROOK;
                }
                case ChessPiece.PieceType.BISHOP -> {
                    return WHITE_BISHOP;
                }
                case ChessPiece.PieceType.KNIGHT -> {
                    return WHITE_KNIGHT;
                }
                case ChessPiece.PieceType.PAWN -> {
                    return WHITE_PAWN;
                }
                default -> {
                    return EMPTY;
                }
            }
        } else {
            switch (piece.getPieceType()) {
                case ChessPiece.PieceType.KING -> {
                    return BLACK_KING;
                }
                case ChessPiece.PieceType.QUEEN -> {
                    return BLACK_QUEEN;
                }
                case ChessPiece.PieceType.ROOK -> {
                    return BLACK_ROOK;
                }
                case ChessPiece.PieceType.BISHOP -> {
                    return BLACK_BISHOP;
                }
                case ChessPiece.PieceType.KNIGHT -> {
                    return BLACK_KNIGHT;
                }
                case ChessPiece.PieceType.PAWN -> {
                    return BLACK_PAWN;
                }
                default -> {
                    return EMPTY;
                }
            }
        }
    }
}

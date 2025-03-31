package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    public static void drawChessBoard(ChessGame.TeamColor team) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_TEXT_ITALIC);
        drawSquares(out, team);
    }

    private static HashMap<Integer, String> getPiecePositions(ChessGame.TeamColor team) {
        HashMap<Integer, String> piecePositions = new HashMap<>();
        if (team == ChessGame.TeamColor.WHITE) {
            piecePositions.put(11, WHITE_ROOK);
            piecePositions.put(12, WHITE_KNIGHT);
            piecePositions.put(13, WHITE_BISHOP);
            piecePositions.put(14, WHITE_QUEEN);
            piecePositions.put(15, WHITE_KING);
            piecePositions.put(16, WHITE_BISHOP);
            piecePositions.put(17, WHITE_KNIGHT);
            piecePositions.put(18, WHITE_ROOK);
            for (int i = 21; i <= 28; i++) {
                piecePositions.put(i, WHITE_PAWN);
            }
            for (int i = 71; i <= 78; i++) {
                piecePositions.put(i, BLACK_PAWN);
            }
            piecePositions.put(81, BLACK_ROOK);
            piecePositions.put(82, BLACK_KNIGHT);
            piecePositions.put(83, BLACK_BISHOP);
            piecePositions.put(84, BLACK_QUEEN);
            piecePositions.put(85, BLACK_KING);
            piecePositions.put(86, BLACK_BISHOP);
            piecePositions.put(87, BLACK_KNIGHT);
            piecePositions.put(88, BLACK_ROOK);
        } else {
            piecePositions.put(11, BLACK_ROOK);
            piecePositions.put(12, BLACK_KNIGHT);
            piecePositions.put(13, BLACK_BISHOP);
            piecePositions.put(14, BLACK_KING);
            piecePositions.put(15, BLACK_QUEEN);
            piecePositions.put(16, BLACK_BISHOP);
            piecePositions.put(17, BLACK_KNIGHT);
            piecePositions.put(18, BLACK_ROOK);
            for (int i = 21; i <= 28; i++) {
                piecePositions.put(i, BLACK_PAWN);
            }
            for (int i = 71; i <= 78; i++) {
                piecePositions.put(i, WHITE_PAWN);
            }
            piecePositions.put(81, WHITE_ROOK);
            piecePositions.put(82, WHITE_KNIGHT);
            piecePositions.put(83, WHITE_BISHOP);
            piecePositions.put(84, WHITE_KING);
            piecePositions.put(85, WHITE_QUEEN);
            piecePositions.put(86, WHITE_BISHOP);
            piecePositions.put(87, WHITE_KNIGHT);
            piecePositions.put(88, WHITE_ROOK);
        }
        return piecePositions;
    }

    private static void drawSquares(PrintStream out, ChessGame.TeamColor team) {
        var piecePositions = getPiecePositions(team);
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
                if (((i / SQUARE_SIZE_IN_PADDED_CHARS) + j) % 2 == 0) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    out.print(SET_BG_COLOR_DARK_GREY);
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
        int piecePosition = (((i / SQUARE_SIZE_IN_PADDED_CHARS)) * 10) + j;
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
}

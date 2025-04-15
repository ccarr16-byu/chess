package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import websocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final Client client;

    public Repl(String serverUrl) {
        client = new Client(serverUrl, this);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_GREEN + "♔ Welcome to Chess ♕");
        System.out.print(SET_TEXT_COLOR_BLUE + client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(NotificationMessage notificationMessage) {
        System.out.println("\n" + SET_TEXT_COLOR_YELLOW + notificationMessage.message);
        printPrompt();
    }

    public void error(ErrorMessage errorMessage) {
        System.out.println("\n" + SET_TEXT_COLOR_RED + errorMessage.errorMessage);
        printPrompt();
    }

    public void loadGame(LoadGameMessage loadGameMessage) {
        ChessGame.TeamColor team = client.currentTeam;
        int method = loadGameMessage.method;
        ChessGame game = loadGameMessage.game;
        ChessMove lastMove = loadGameMessage.lastMove;
        ChessPosition position = loadGameMessage.position;

        client.currentGameState = game;
        if (method == 1) {
            client.lastMove = lastMove;
        }

        System.out.print("\n");
        if (method == 2) {
            ChessBoardUI.drawChessBoard(team, game.getBoard(), method, lastMove, position, game.validMoves(position));
        } else {
            ChessBoardUI.drawChessBoard(team, game.getBoard(), method, lastMove, position, null);
        }
        printPrompt();
    }

    private void printPrompt() {
        String stateMessage;
        if (client.state == 0) {
            stateMessage = "[Logged Out]";
        } else {
            stateMessage = "[Logged In]";
        }
        System.out.print("\n" + SET_TEXT_COLOR_MAGENTA + stateMessage + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
    }
}

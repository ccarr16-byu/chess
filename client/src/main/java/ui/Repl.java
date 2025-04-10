package ui;

import websocket.NotificationHandler;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

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

    public void notify(NotificationMessage notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification.message);
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

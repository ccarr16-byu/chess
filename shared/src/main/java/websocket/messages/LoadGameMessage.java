package websocket.messages;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

public class LoadGameMessage extends ServerMessage {
    public final ChessGame game;
    public final int method;
    public final ChessMove lastMove;
    public final ChessPosition position;

    public LoadGameMessage(ServerMessageType type, ChessGame game, int method, ChessMove lastMove,
                           ChessPosition position) {
        super(type);
        this.game = game;
        this.method = method;
        this.lastMove = lastMove;
        this.position = position;
    }
}

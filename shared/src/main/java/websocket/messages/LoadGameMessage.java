package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    public final Integer game;

    public LoadGameMessage(ServerMessageType type, Integer game) {
        super(type);
        this.game = game;
    }
}

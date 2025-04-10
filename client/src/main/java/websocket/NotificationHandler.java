package websocket;

import websocket.messages.ServerMessage;

import javax.management.Notification;

public interface NotificationHandler {
    void notify(Notification notification);
}
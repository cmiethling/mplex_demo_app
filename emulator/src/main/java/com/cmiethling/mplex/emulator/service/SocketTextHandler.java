package com.cmiethling.mplex.emulator.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@Component
public class SocketTextHandler extends TextWebSocketHandler {

    private static WebSocketSession session1;

    /**
     * Sends an event to all clients. This method is used by the user interface or any scheduled operation to send an
     * event to all registered clients.
     *
     * @param eventMessage the message to send
     */
    public static void broadcastEvent(final JSONObject eventMessage) throws IOException {
        if (session1 != null && session1.isOpen()) {
            log.info("Sending Event: " + eventMessage.toString());
            session1.sendMessage(new TextMessage(eventMessage.toString()));
            log.info("Event sent");
        }
    }

    @Override
    public void handleTextMessage(final WebSocketSession session, final TextMessage message)
            throws InterruptedException, IOException {

        final String payload = message.getPayload();
        final JSONObject jsonObject = new JSONObject(payload);
        System.out.println("message: " + message);
        System.out.println("payload: " + payload);
        System.out.println("json: " + jsonObject);
        broadcastEvent(jsonObject);
        //        session.sendMessage(new TextMessage("Hi " + jsonObject.get("user") + " how may we help you?"));
        //        System.out.println(session); // uri: ws://localhost:8091/user
    }

    //    ############# open close #################

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        if (session1 != null) throw new IllegalStateException("session was already established");
        session1 = session;
        log.info("Emulator Session opened");
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        session1 = null;
        log.info(String.format("Emulator Session %s closed because %s, %s", session.getId(),
                status.getReason(), Integer.valueOf(status.getCode())));
        super.afterConnectionClosed(session, status);
    }
}

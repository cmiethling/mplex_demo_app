package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.message.EventMessage;
import com.cmiethling.mplex.device.service.DeviceMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@Component
public class WebSocketServerService extends TextWebSocketHandler {

    private static WebSocketSession session1;

    @Autowired
    private DeviceMessageService deviceMessageService;

    /**
     * Sends an event to the client.
     *
     * @param eventMessage the message to send
     */
    public void broadcastEvent(final EventMessage eventMessage) throws IOException, DeviceException {
        if (session1 != null && session1.isOpen()) {
            final var json = this.deviceMessageService.serializeMessage(eventMessage);
            log.info("Sending Event: " + json);
            session1.sendMessage(new TextMessage(json));
            log.info("Event sent");
        }
    }

    @Override
    public void handleTextMessage(final WebSocketSession session, final TextMessage message)
            throws DeviceException {

        final String json = message.getPayload();
        // final var deviceMessage = this.deviceMessageService.deserializeMessage(json);
        System.out.println("Raw message: " + message);
        System.out.println("json: " + json);
        // System.out.println("DeviceMessage: " + deviceMessage);
        // switch (deviceMessage) {
        //     case final RequestMessage r -> System.out.println("request: " + r);
        //     case final EventMessage e -> System.out.println("event: " + e);
        //     default -> throw new IllegalStateException("Unexpected value: " + deviceMessage);
        // }
        // // can only be request message
        // final var request = deviceMessage.asRequest();
    }

    //    ############# open close #################

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        if (session1 != null) throw new IllegalStateException("session was already established");
        session1 = session;
        log.info("Emulator Session opened: " + session.getId());
        session1.sendMessage(new TextMessage("from server"));
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
        session1 = null;
        log.info(String.format("Emulator Session closed: %s with %s", session.getId(), status));
        super.afterConnectionClosed(session, status);
    }
}

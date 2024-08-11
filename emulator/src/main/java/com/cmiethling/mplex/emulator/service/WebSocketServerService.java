package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.DeviceMessageException;
import com.cmiethling.mplex.device.message.*;
import com.cmiethling.mplex.device.service.DeviceMessageService;
import lombok.NonNull;
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

    private WebSocketSession session1;

    @Autowired
    private DeviceMessageService deviceMessageService;

    /**
     * Sends an event to the client.
     *
     * @param eventMessage the message to send
     */
    public void broadcastEvent(@NonNull final EventMessage eventMessage) throws IOException, DeviceException {
        if (this.session1 != null && this.session1.isOpen()) {
            sendMessage(eventMessage);
        } else
            log.info("could send event. Session: " + this.session1);
    }

    @Override
    public void handleTextMessage(@NonNull final WebSocketSession session, @NonNull final TextMessage message)
            throws DeviceException, IOException {

        final String json = message.getPayload();
        log.info("received: " + json);

        final var deviceMessage = this.deviceMessageService.deserializeMessage(json);
        switch (deviceMessage) {
            case final RequestMessage r -> {
                final var result = new ResultMessage(r.getId(), r.getSubsystem(), r.getTopic());
                result.setError(ResultError.NONE);
                sendMessage(result);
            }
            case final EventMessage e -> System.out.println("event: " + e);
            default -> throw new IllegalStateException("Unexpected value: " + deviceMessage);
        }
        // // can only be request message
        // final var request = deviceMessage.asRequest();
    }

    @Override
    public void afterConnectionEstablished(@NonNull final WebSocketSession session) throws Exception {
        if (this.session1 != null) throw new IllegalStateException("session was already established");
        this.session1 = session;
        log.info("Emulator Session opened: " + session.getId());
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull final WebSocketSession session, @NonNull final CloseStatus status) throws Exception {
        this.session1 = null;
        log.info(String.format("Emulator Session closed: %s with %s", session.getId(), status));
        super.afterConnectionClosed(session, status);
    }

    private void sendMessage(@NonNull final DeviceMessage eventMessage) throws DeviceMessageException, IOException {
        final var json = this.deviceMessageService.serializeMessage(eventMessage);
        this.session1.sendMessage(new TextMessage(json));
        log.info("Message sent: " + json);
    }
}

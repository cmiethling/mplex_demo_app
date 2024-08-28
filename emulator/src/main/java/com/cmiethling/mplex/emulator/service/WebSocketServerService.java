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

    @Autowired
    private EventLoggingService eventLoggingService;

    /**
     * Sends an event to the client.
     *
     * @param eventMessage the message to send
     */
    public boolean broadcastEvent(@NonNull final EventMessage eventMessage) throws IOException, DeviceException {
        if (this.session1 != null && this.session1.isOpen()) {
            sendMessage(eventMessage);
            return true;
        } else {
            // TODO improve logging
            log.info("could not send event. Session: " + this.session1);
            return false;
        }
    }

    private void sendMessage(@NonNull final DeviceMessage eventMessage) throws DeviceMessageException, IOException {
        final var json = this.deviceMessageService.serializeMessage(eventMessage);
        this.session1.sendMessage(new TextMessage(json));
        log.info("Message sent: " + json);
    }

    @Override
    public void handleTextMessage(@NonNull final WebSocketSession session, @NonNull final TextMessage message)
            throws DeviceException, IOException {

        final String json = message.getPayload();
        log.info("received: " + json);

        final var deviceMessage = this.deviceMessageService.deserializeMessage(json);
        switch (deviceMessage) {
            case final RequestMessage request -> {
                final var result = new ResultMessage(request.getId(), request.getSubsystem(), request.getTopic());
                result.setError(ResultError.NONE);
                try {
                    sendMessage(result);
                    this.eventLoggingService.logFullCommand(request, result);
                } catch (final IOException | DeviceMessageException ex) {
                    this.eventLoggingService.logRequest(request);
                    throw ex;
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + deviceMessage);
        }
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
}

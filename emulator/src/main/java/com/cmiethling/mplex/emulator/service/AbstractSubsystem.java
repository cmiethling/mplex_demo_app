package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.device.api.SubsystemError;
import com.cmiethling.mplex.device.message.EventMessage;
import com.cmiethling.mplex.device.message.Subsystem;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSubsystem {
    protected static final String NEW_STATE_EQUALS_CURRENT_STATE = "new state didn't change the current state.";

    private final Subsystem subsystem;

    @Autowired
    private LogService logService;
    @Autowired
    private WebSocketServerService webSocketServerService;

    protected AbstractSubsystem(@NonNull final Subsystem subsystem) {
        this.subsystem = subsystem;
    }

    protected final EventMessage createErrorEvent(final SubsystemError error, final String topic,
                                                  final String errorcode) {
        final var event = createEventMessage(topic);
        event.parameters().putInt(errorcode, error.code());
        return event;
    }

    private EventMessage createEventMessage(final String topic) {
        return new EventMessage(this.subsystem, topic);
    }

    protected void sendEvent(final EventMessage event) {
        final var errorMsg = this.webSocketServerService.broadcastEvent(event);
        this.logService.logEvent(event, errorMsg);
    }

    protected void logEventNotSent(final EventMessage event, final String reason) {
        this.logService.logEvent(event, "event not sent: " + reason);
    }
}


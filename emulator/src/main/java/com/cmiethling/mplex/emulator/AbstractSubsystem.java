package com.cmiethling.mplex.emulator;

import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.api.SubsystemError;
import com.cmiethling.mplex.device.message.EventMessage;
import com.cmiethling.mplex.device.message.Subsystem;
import com.cmiethling.mplex.emulator.service.EventLoggingService;
import com.cmiethling.mplex.emulator.service.WebSocketServerService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public abstract class AbstractSubsystem {

    private final Subsystem subsystem;

    @Autowired
    private EventLoggingService eventLoggingService;
    @Autowired
    private WebSocketServerService webSocketServerService;

    protected AbstractSubsystem(@NonNull final Subsystem subsystem) {
        this.subsystem = subsystem;
    }

    protected final void sendErrorEvent(final SubsystemError error, final String topic, final String errorcode) throws IOException, DeviceException {
        final var event = createEventMessage(topic);
        event.parameters().putInt(errorcode, error.code());
        sendEventMessage(event);
    }

    private EventMessage createEventMessage(final String topic) {
        return new EventMessage(this.subsystem, topic);
    }

    private void sendEventMessage(final EventMessage eventMessage) throws IOException, DeviceException {
        final var isSent = this.webSocketServerService.broadcastEvent(eventMessage);
        if (isSent) this.eventLoggingService.logEvent(eventMessage);
    }
}


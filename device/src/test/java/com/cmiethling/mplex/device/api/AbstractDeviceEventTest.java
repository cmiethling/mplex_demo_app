package com.cmiethling.mplex.device.api;

import com.cmiethling.mplex.device.message.EventMessage;
import org.springframework.lang.NonNull;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public abstract class AbstractDeviceEventTest extends AbstractDeviceTest {

    protected <T extends DeviceEvent> T fromEventMessage(@NonNull final Class<T> class1,
                                                         @NonNull final String resourceName) throws Exception {
        final var event = this.eventCommandFactory.event(class1);

        final var message = loadMessage(resourceName);
        final var eventMessage = assertInstanceOf(EventMessage.class, message);

        event.fromEventMessage(eventMessage);

        return event;
    }
}

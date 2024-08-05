package com.cmiethling.mplex.device.api;

import lombok.NonNull;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractDeviceEventTest extends AbstractDeviceTest {

    protected <T extends DeviceEvent> T fromEventMessage(@NonNull final Class<T> class1,
                                                         @NonNull final String resourceName) throws Exception {
        final var message = loadMessage(resourceName);
        assertTrue(message.isEvent());

        final var event = this.eventCommandFactory.event(class1);
        event.fromEventMessage(message.asEvent());

        return event;
    }
}

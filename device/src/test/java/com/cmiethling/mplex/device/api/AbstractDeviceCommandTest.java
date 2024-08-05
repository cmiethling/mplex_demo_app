package com.cmiethling.mplex.device.api;

import com.cmiethling.mplex.device.DeviceException;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractDeviceCommandTest extends AbstractDeviceTest {
    /**
     * This id is used in the JSON files for testing.
     */
    private static final UUID TESTING_UUID = UUID.fromString("2e4107c4-8773-4e62-a400-7e7c8195e918");

    protected void toRequestMessage(final DeviceCommand command, final String resourceName)
            throws IOException, DeviceException {
        final var command1 = (AbstractDeviceCommand<?>) command;
        command1.setIdGenerator(() -> TESTING_UUID);
        final var commandMessage = command1.toRequestMessage();
        final var json = super.deviceMessageService.serializeMessage(commandMessage);

        final var expectedJson = super.loadJson(resourceName);

        assertEquals(expectedJson, json, resourceName);
    }

    protected <T extends DeviceCommand> T fromResultMessage(final T command, final String resourceName) throws IOException,
            DeviceException {
        final var message = loadMessage(resourceName);
        assertTrue(message.isResult());

        command.fromResultMessage(message.asResult());
        return command;
    }
}


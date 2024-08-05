package com.cmiethling.mplex.device.message;

import com.cmiethling.mplex.device.DeviceMessageException;
import com.cmiethling.mplex.device.config.DeviceMessageConfig;
import com.cmiethling.mplex.device.service.DeviceMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DeviceMessageService.class, DeviceMessageConfig.class})
public final class EventMessageTest {

    @Autowired
    private DeviceMessageService deviceMessageService;

    @Test
    public void eventToJson() throws DeviceMessageException {
        final var message = new EventMessage(Subsystem.FLUIDICS, "status");
        message.parameters().putString("sampleDoor", "open");
        message.parameters().putString("consumableDoor", "closed");

        final var json = this.deviceMessageService.serializeMessage(message);
        final var expected = """
                {
                  "type": "event",
                  "subsystem": "fluidics",
                  "topic": "status",
                  "data": {
                    "sampleDoor": "open",
                    "consumableDoor": "closed"
                  }
                }
                """;
        assertEquals(expected, json);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void eventFromJson() throws DeviceMessageException {
        final var json = """
                {
                    "type": "event",
                    "subsystem": "fluidics",
                    "topic": "states",
                    "data": {
                        "GelPumpOn": true,
                        "GelValveOpen": true
                    }
                }
                """;
        final var message = this.deviceMessageService.deserializeMessage(json);
        assertFalse(message.isRequest());
        assertFalse(message.isResult());
        assertTrue(message.isEvent());

        final var event = message.asEvent();
        // System.out.println(event);

        assertNotNull(event);
        assertEquals(Subsystem.FLUIDICS, event.getSubsystem());
        assertEquals("states", event.getTopic());
        assertEquals(true, event.parameters().getBoolean("GelPumpOn").get());
        assertEquals(true, event.parameters().getBoolean("GelValveOpen").get());
    }

    @Test
    public void testEquals() {
        final var event1 = new EventMessage(Subsystem.HIGH_VOLTAGE, "topic1");
        final var event2 = new EventMessage(Subsystem.FLUIDICS, "topic2");

        assertEquals(event1, event1);
        assertEquals(event2, event2);

        assertNotEquals(event1, event2);
        assertNotEquals(event2, event1);
    }
}




package com.cmiethling.mplex.device;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = TestConfiguration.class)
public final class EventMessageTest {

    @Test
    public void eventToJson() throws DeviceMessageException {
        final var message = new EventMessage(Subsystem.SAFETY, "status");
        message.parameters().putString("sampleDoor", "open");
        message.parameters().putString("consumableDoor", "closed");

        final var json = DeviceMessage.toJson(message);
        final var expected = """
                {
                  "type" : "event",
                  "subsystem" : "safety",
                  "topic" : "status",
                  "data" : {
                    "sampleDoor" : "open",
                    "consumableDoor" : "closed"
                  }
                }""";
        assertEquals(expected, json);
    }

    @Test
    public void eventFromJson() throws DeviceMessageException {
        final var json = """
                {
                    "type": "event",
                    "subsystem": "safety",
                    "topic": "status",
                    "data" : {
                        "sampleDoor": "open",
                        "consumableDoor": "closed"
                    }
                }
                """;
        final var message = DeviceMessage.toDeviceMessage(json);
        assertFalse(message.isRequest());
        assertFalse(message.isResult());
        assertTrue(message.isEvent());

        final var event = message.asEvent();
        System.out.println(event);

        assertNotNull(event);
        assertEquals(Subsystem.SAFETY, event.getSubsystem());
        assertEquals("status", event.getTopic());
        assertEquals("open", event.parameters().getString("sampleDoor").get());
        assertEquals("closed", event.parameters().getString("consumableDoor").get());
    }

    @Test
    public void testEquals() {
        final var event1 = new EventMessage(Subsystem.OPTICS, "topic1");
        final var event2 = new EventMessage(Subsystem.FLUIDICS, "topic2");

        assertEquals(event1, event1);
        assertEquals(event2, event2);

        assertNotEquals(event1, event2);
        assertNotEquals(event2, event1);
    }
}




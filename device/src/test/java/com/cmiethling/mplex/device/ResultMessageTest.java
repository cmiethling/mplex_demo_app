package com.cmiethling.mplex.device;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = TestConfiguration.class)
public final class ResultMessageTest {

    private static final UUID ANY_UUID = UUID.fromString("2e4107c4-8773-4e62-a400-7e7c8195e918");

    @Test
    public void fromJson() throws DeviceMessageException, JsonProcessingException {
        final var json = """
                {
                    "type": "result",
                    "id": "2e4107c4-8773-4e62-a400-7e7c8195e918",
                    "subsystem": "motorcontrol",
                    "topic": "move",
                    "error": "NoError",
                    "result": {
                        "x-pos": 42.43
                    }
                }""";

        final var message = DeviceMessage.toDeviceMessage(json);
        assertFalse(message.isRequest());
        assertTrue(message.isResult());
        assertFalse(message.isEvent());

        final var result = message.asResult();
        // System.out.println(result);
        assertNotNull(result);
        assertEquals(ANY_UUID, result.getId());
        assertEquals(Subsystem.MOTOR_CONTROL, result.getSubsystem());
        assertEquals("move", result.getTopic());
        assertEquals(42.43, result.parameters().getRequiredDouble("x-pos"));
        assertEquals(ResultError.NONE, result.getError());
    }

    @Test
    public void toJson() throws DeviceMessageException {
        final var message = new ResultMessage(ANY_UUID, Subsystem.MOTOR_CONTROL, "move");
        message.setError(ResultError.NONE);
        message.parameters().putString("mode", "smooth");
        message.parameters().putDouble("x-pos", 42.42);
        message.parameters().putInt("speed", 100);
        message.parameters().putBoolean("async", true);
        final var params2 = message.parameters().addNested("innerParams");
        params2.putString("insideMode", "not smooth");
        final var json = DeviceMessage.toJson(message);
        // System.out.println(json);
        final var expected = """
                {
                  "type" : "result",
                  "id" : "2e4107c4-8773-4e62-a400-7e7c8195e918",
                  "subsystem" : "motorcontrol",
                  "topic" : "move",
                  "error" : "NoError",
                  "result" : {
                    "mode" : "smooth",
                    "async" : true,
                    "innerParams" : {
                      "insideMode" : "not smooth"
                    },
                    "x-pos" : 42.42,
                    "speed" : 100
                  }
                }""";
        assertEquals(expected, json);
    }

    @Test
    public void testEquals() {
        final var result1 = new ResultMessage(UUID.randomUUID(), Subsystem.OPTICS, "topic1");
        final var result2 = new ResultMessage(UUID.randomUUID(), Subsystem.FLUIDICS, "topic2");

        assertEquals(result1, result1);
        assertEquals(result2, result2);

        assertNotEquals(result1, result2);
        assertNotEquals(result2, result1);
    }
}


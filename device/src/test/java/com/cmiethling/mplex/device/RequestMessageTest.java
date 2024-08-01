package com.cmiethling.mplex.device;

import com.cmiethling.mplex.device.impl.MessageParametersImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = TestConfiguration.class)
public final class RequestMessageTest {

    private static final UUID ANY_UUID = UUID.fromString("2e4107c4-8773-4e62-a400-7e7c8195e918");

    public static Stream<Arguments> invalidJson() {
        return Stream.of(
                Arguments.of("hello"), //
                Arguments.of("{}"), //
                Arguments.of("""
                        {
                            "type": "command"
                        }
                        """), //
                Arguments.of("""
                        {
                            "type": "command",
                            "uuid": "2e4107c4-8773-4e62-a400-7e7c8195e918"
                        }
                        """), //
                Arguments.of("""
                        {
                            "type": " "
                        }
                        """), //
                Arguments.of("""
                        {
                            "type": "hello"
                        }
                        """),//
                Arguments.of("""
                        {
                            "type": "command",
                            "uuid": "2e4107c4-8773-4e62-a400-7e7c8195e918",
                            "system": "motorcontrol"
                        }
                        """),
                Arguments.of("""
                        {
                            "type": "command",
                            "uuid": "2e4107c4-8773-4e62-a400-7e7c8195e918",
                            "system": "motorcontrol",
                            "method": "move",
                            "parameters": "mode"
                        }
                        """), //
                Arguments.of("""
                        {
                            "type": "command",
                            "uuid": "2e4107c4-8773-4e62-a400-7e7c8195e918",
                            "system": "motorcontrol",
                            "method": "move",
                            "parameters": {
                                "mode": ""
                            }
                        }
                        """));
    }

    @Test
    public void fromJson() throws DeviceMessageException, JsonProcessingException {
        final var json = """
                {
                    "type": "request",
                    "id": "2e4107c4-8773-4e62-a400-7e7c8195e918",
                    "subsystem": "motorcontrol",
                    "topic": "move",
                    "parameters" : {
                        "mode": "smooth",
                        "x-pos": 42.42,
                        "speed": 100,
                        "async": true,
                        "innerParams" : {
                            "insideMode" : "not smooth"
                        }
                    }
                }""";

        final var message = DeviceMessage.toDeviceMessage(json);
        assertTrue(message.isRequest());
        assertFalse(message.isResult());
        assertFalse(message.isEvent());

        final var request = message.asRequest();
        System.out.println(request);
        assertNotNull(request);
        assertEquals(ANY_UUID, request.getId());
        assertEquals(Subsystem.MOTOR_CONTROL, request.getSubsystem());
        assertEquals("move", request.getTopic());
        assertEquals("smooth", request.parameters().getString("mode").get());
        assertEquals(42.42, request.parameters().getRequiredDouble("x-pos"));
        assertEquals(100, request.parameters().getRequiredInt("speed"));
        assertTrue(request.parameters().getBoolean("async").get().booleanValue());
        final var innerParams = new MessageParametersImpl();
        innerParams.putString("insideMode", "not smooth");
        assertEquals(innerParams, request.parameters().getNested("innerParams").get());
    }

    @Test
    public void toJson() throws DeviceMessageException {
        final var message = new RequestMessage(ANY_UUID, Subsystem.MOTOR_CONTROL, "move");
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
                  "type" : "request",
                  "id" : "2e4107c4-8773-4e62-a400-7e7c8195e918",
                  "subsystem" : "motorcontrol",
                  "topic" : "move",
                  "parameters" : {
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

    @ParameterizedTest()
    @MethodSource("invalidJson")
    public void fromInvalidJson(final String json) {
        assertThrows(DeviceMessageException.class, () -> DeviceMessage.toDeviceMessage(json).asRequest());
    }

    @Test
    public void testEquals() {
        final var request1 = new RequestMessage(UUID.randomUUID(), Subsystem.OPTICS, "topic1");
        final var request2 = new RequestMessage(UUID.randomUUID(), Subsystem.FLUIDICS, "topic2");

        assertEquals(request1, request1);
        assertEquals(request2, request2);

        assertNotEquals(request1, request2);
        assertNotEquals(request2, request1);
    }
}


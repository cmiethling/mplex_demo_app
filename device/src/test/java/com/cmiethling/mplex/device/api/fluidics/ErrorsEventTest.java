package com.cmiethling.mplex.device.api.fluidics;

import com.cmiethling.mplex.device.api.AbstractDeviceEventTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ErrorsEventTest extends AbstractDeviceEventTest {

    @Test
    public void event() throws Exception {
        final var event = fromEventMessage(ErrorsEvent.class, "Errors_event");
        assertEquals(FluidicsError.GEL_PUMP_PRESSURE_TOO_HIGH, event.getErrorCode());
    }
}

package com.cmiethling.mplex.device.api.hv;

import com.cmiethling.mplex.device.api.AbstractDeviceEventTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ErrorEventTest extends AbstractDeviceEventTest {

    @Test
    public void event() throws Exception {
        final var event = fromEventMessage(ErrorEvent.class, "Error_event");
        assertEquals(HighVoltageError.CAPILLARY_ERROR, event.getErrorCode());
    }
}

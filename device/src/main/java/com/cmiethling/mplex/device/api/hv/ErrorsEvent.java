package com.cmiethling.mplex.device.api.hv;

import com.cmiethling.mplex.device.DeviceMessageException;
import com.cmiethling.mplex.device.api.AbstractDeviceEvent;
import com.cmiethling.mplex.device.message.EventMessage;
import com.cmiethling.mplex.device.message.Subsystem;
import lombok.Getter;
import lombok.NonNull;

/**
 * If there is an error or if the error is cleared this event is sent by the device.
 */
@Getter
public final class ErrorsEvent extends AbstractDeviceEvent {

    public static final String TOPIC = "errors";
    public static final String ERRORCODE = "errorcode";

    private HighVoltageError errorCode;

    /**
     * Creates a new event object.
     */
    public ErrorsEvent() {
        super(Subsystem.HIGH_VOLTAGE, TOPIC);
    }

    @Override
    public void fromEventMessage(@NonNull final EventMessage message) throws DeviceMessageException {
        super.fromEventMessage(message);

        final var code = message.parameters().getRequiredInt(ERRORCODE);
        this.errorCode = HighVoltageError.ofCode(code)
                .orElseThrow(() -> new DeviceMessageException("eventUnknownErrorCode: message=%s, code=%s".formatted(message, code)));
    }
}


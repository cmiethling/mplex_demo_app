package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.device.api.SubsystemError;
import com.cmiethling.mplex.device.api.hv.ErrorsEvent;
import com.cmiethling.mplex.device.api.hv.HighVoltageError;
import com.cmiethling.mplex.device.message.Subsystem;
import com.cmiethling.mplex.emulator.model.HighVoltageStatus;
import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HighVoltageService extends AbstractSubsystem {

    @Autowired
    private HighVoltageStatus highVoltageStatus;

    protected HighVoltageService() {
        super(Subsystem.HIGH_VOLTAGE);
    }

    public SubsystemError getHighVoltageError() {return this.highVoltageStatus.getHighVoltageError();}

    public void processError(@NonNull final String currentError, @NonNull final String newError) {
        final var error = HighVoltageError.valueOf(newError);
        final var event = createErrorEvent(error, ErrorsEvent.TOPIC, ErrorsEvent.ERRORCODE);

        if (!newError.equals(currentError)) {
            this.highVoltageStatus.setHighVoltageError(error);
            sendEvent(event);
        } else
            logEventNotSent(event, NEW_STATE_EQUALS_CURRENT_STATE);
    }
}

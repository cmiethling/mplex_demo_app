package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.device.api.SubsystemError;
import com.cmiethling.mplex.device.api.fluidics.ErrorsEvent;
import com.cmiethling.mplex.device.api.fluidics.FluidicsError;
import com.cmiethling.mplex.device.message.Subsystem;
import com.cmiethling.mplex.emulator.model.FluidicsStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class FluidicsService extends AbstractSubsystem {

    @Autowired
    private FluidicsStatus fluidicsStatus;

    protected FluidicsService() {
        super(Subsystem.FLUIDICS);
    }

    public SubsystemError getFluidicsError() {return this.fluidicsStatus.getFluidicsError();}

    public void processError(@NonNull final String currentError, @NonNull final String newError) {
        final var error = FluidicsError.valueOf(newError);
        final var event = createErrorEvent(error, ErrorsEvent.TOPIC, ErrorsEvent.ERRORCODE);
        if (!newError.equals(currentError)) {
            this.fluidicsStatus.setFluidicsError(error);
            sendEvent(event);
        } else
            logEventNotSent(event, NEW_STATE_EQUALS_CURRENT_STATE);
    }
}

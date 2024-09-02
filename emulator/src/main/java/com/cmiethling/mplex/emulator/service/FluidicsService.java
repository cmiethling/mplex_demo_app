package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.api.SubsystemError;
import com.cmiethling.mplex.device.api.fluidics.ErrorsEvent;
import com.cmiethling.mplex.device.api.fluidics.FluidicsError;
import com.cmiethling.mplex.device.message.Subsystem;
import com.cmiethling.mplex.emulator.model.FluidicsStatus;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FluidicsService extends AbstractSubsystem {

    @Autowired
    private FluidicsStatus fluidicsStatus;

    protected FluidicsService() {
        super(Subsystem.FLUIDICS);
    }

    public SubsystemError getFluidicsError() {return this.fluidicsStatus.getFluidicsError();}

    public void processError(@NonNull final String currentError, @NonNull final String newError) throws IOException,
            DeviceException {
        if (!newError.equals(currentError)) {
            final var error = FluidicsError.valueOf(newError);
            this.fluidicsStatus.setFluidicsError(error);
            sendErrorEvent(error, ErrorsEvent.TOPIC, ErrorsEvent.ERRORCODE);
        }
    }
}

package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.device.api.SubsystemError;
import com.cmiethling.mplex.device.api.fluidics.FluidicsError;
import com.cmiethling.mplex.emulator.model.FluidicsStatus;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FluidicsService {

    @Autowired
    private FluidicsStatus fluidicsStatus;

    public SubsystemError getFluidicsError() {return this.fluidicsStatus.getFluidicsError();}

    public void processError(@NonNull final String currentError, @NonNull final String newError) {
        if (!newError.equals(currentError))
            this.fluidicsStatus.setFluidicsError(FluidicsError.valueOf(newError));
    }
}

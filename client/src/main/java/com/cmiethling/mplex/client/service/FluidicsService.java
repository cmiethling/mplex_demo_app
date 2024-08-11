package com.cmiethling.mplex.client.service;

import com.cmiethling.mplex.client.AbstractSubsystem;
import com.cmiethling.mplex.client.model.FluidicsStatus;
import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.api.SubsystemError;
import com.cmiethling.mplex.device.api.fluidics.SetGelPumpCommand;
import com.cmiethling.mplex.device.message.Subsystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FluidicsService extends AbstractSubsystem {

    @Autowired
    private FluidicsStatus fluidicsStatus;

    protected FluidicsService() {
        super(Subsystem.FLUIDICS);
    }

    public void sendGelPumpMode(final boolean isOn) throws ExecutionException, InterruptedException, DeviceException {
        final var command = command(SetGelPumpCommand.class);
        command.setOn(isOn);
        sendCommand(command);
    }

    public SubsystemError getFluidicsError() {return this.fluidicsStatus.getFluidicsError();}
}

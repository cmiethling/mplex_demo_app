package com.cmiethling.mplex.client.service;

import com.cmiethling.mplex.client.AbstractSubsystem;
import com.cmiethling.mplex.client.model.FluidicsStatus;
import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.api.fluidics.ErrorsEvent;
import com.cmiethling.mplex.device.api.fluidics.SetGelPumpCommand;
import com.cmiethling.mplex.device.api.fluidics.StatesEvent;
import com.cmiethling.mplex.device.message.Subsystem;
import com.cmiethling.mplex.device.websocket.DeviceEventWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Getter
@Slf4j
@Service
public class FluidicsService extends AbstractSubsystem {

    // ################# current states ##########################
    @Autowired
    private FluidicsStatus fluidicsStatus;

    protected FluidicsService() {
        super(Subsystem.FLUIDICS);
    }

    // ################# events ##########################
    @EventListener
    private void errorsEventReceived(final DeviceEventWrapper<ErrorsEvent> eventWrapper) {
        final var event = eventWrapper.getEvent();
        log.info("received {} with {}", event, event.getErrorCode());
        this.fluidicsStatus.setFluidicsError(event.getErrorCode());
    }

    @EventListener
    private void statesEventReceived(final DeviceEventWrapper<StatesEvent> eventWrapper) {
        final var event = eventWrapper.getEvent();
        log.info("received: {} with isGelPumpOn={}, isGelValveOpen={}",
                event, event.isGelPumpOn(), event.isGelValveOpen());
        event.isGelPumpOn().ifPresent(isOn -> this.fluidicsStatus.setGelPumpOn(isOn));
        event.isGelValveOpen().ifPresent(isOpen -> this.fluidicsStatus.setGelValveOpen(isOpen));
    }

    // ################# commands ##########################
    public void sendGelPumpMode(final boolean isOn) throws ExecutionException, InterruptedException, DeviceException {
        final var command = command(SetGelPumpCommand.class);
        command.setOn(isOn);
        sendCommand(command);
        // if there is no exception then command is successful
        this.fluidicsStatus.setGelPumpOn(isOn);
    }
}

package com.cmiethling.mplex.client.model;

import com.cmiethling.mplex.device.api.fluidics.ErrorEvent;
import com.cmiethling.mplex.device.message.Subsystem;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FluidicsStatus {
    private final EventEntry errorsEvent;
    private final EventEntry gelPump;
    private final EventEntry gelValve;
    @Getter
    private final List<EventEntry> entries;
    @Getter
    private boolean gelPumpOn;
    @Getter
    private boolean gelValveOpen;

    // initialize so that subsystem and topic are not null
    public FluidicsStatus() {
        final var error = new ErrorEvent();
        this.errorsEvent = new EventEntry(error.getSubsystem(), error.getTopic());
        // the states from StatesEvent separately, as it is possible to receive only 1 state with 1 StatesEvent
        this.gelPump = new EventEntry(Subsystem.FLUIDICS, "Gel Pump");
        this.gelValve = new EventEntry(Subsystem.FLUIDICS, "Gel Valve");
        this.entries = List.of(this.gelPump, this.gelValve, this.errorsEvent);
    }

    public void setErrorsEvent(final ErrorEvent errorEvent) {
        this.errorsEvent.setCurrentState(errorEvent.getErrorCode().toString());
    }

    public void setGelPump(final boolean isOn) {
        this.gelPump.setCurrentState(isOn ? "ON" : "OFF");
        this.gelPumpOn = isOn;
    }

    public void setGelValve(final boolean isOpen) {
        this.gelPump.setCurrentState(isOpen ? "OPEN" : "CLOSED");
        this.gelValveOpen = isOpen;
    }
}

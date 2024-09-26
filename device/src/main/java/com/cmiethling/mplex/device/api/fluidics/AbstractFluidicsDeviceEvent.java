package com.cmiethling.mplex.device.api.fluidics;

import com.cmiethling.mplex.device.api.AbstractDeviceEvent;
import com.cmiethling.mplex.device.message.Subsystem;
import org.springframework.lang.NonNull;

abstract class AbstractFluidicsDeviceEvent extends AbstractDeviceEvent {

    /**
     * Creates a new event object.
     *
     * @param topic the event topic
     */
    AbstractFluidicsDeviceEvent(@NonNull final String topic) {
        super(Subsystem.FLUIDICS, topic);
    }
}

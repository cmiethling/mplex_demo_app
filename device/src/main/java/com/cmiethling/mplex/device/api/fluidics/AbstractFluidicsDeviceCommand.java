package com.cmiethling.mplex.device.api.fluidics;

import com.cmiethling.mplex.device.api.AbstractDeviceCommand;
import com.cmiethling.mplex.device.message.Subsystem;

import java.util.Optional;

/**
 * Abstract base class for all fluidics commands.
 */
abstract class AbstractFluidicsDeviceCommand extends AbstractDeviceCommand<FluidicsError> {

    /**
     * Creates a new command with the specified name.
     *
     * @param topic the topic name as used in the device communication
     */
    protected AbstractFluidicsDeviceCommand(final String topic) {
        super(Subsystem.FLUIDICS, topic);
    }

    @Override
    protected Optional<FluidicsError> getSubsystemError(final int code) {
        return FluidicsError.ofCode(code);
    }
}


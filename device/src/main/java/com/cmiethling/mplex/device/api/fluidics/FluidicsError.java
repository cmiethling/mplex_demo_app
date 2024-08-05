package com.cmiethling.mplex.device.api.fluidics;

import com.cmiethling.mplex.device.api.SubsystemError;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Each method referring to the fluidics interface provides a return value describing generic success or failure of the
 * command call.
 */
public enum FluidicsError implements SubsystemError {

    NONE,
    BUFFER_PUMP_ERROR,
    INVALID_GEL_PUMP_STATE;

    /**
     * Returns the enum value associated with the specified code.
     *
     * @param code the code
     * @return an optional enum value
     */
    public static Optional<FluidicsError> ofCode(final int code) {
        return Stream.of(FluidicsError.values()) //
                .filter(value -> value.code() == code) //
                .findFirst();
    }

    @Override
    public int code() {
        return this.ordinal();
    }
}


package com.cmiethling.mplex.device.api.hv;

import com.cmiethling.mplex.device.api.SubsystemError;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Each method referring to the fluidics interface provides a return value describing generic success or failure of the
 * command call.
 */
public enum HighVoltageError implements SubsystemError {

    NONE,
    CAPILLARY_ERROR;

    /**
     * Returns the enum value associated with the specified code.
     *
     * @param code the code
     * @return an optional enum value
     */
    public static Optional<HighVoltageError> ofCode(final int code) {
        return Stream.of(HighVoltageError.values()) //
                .filter(value -> value.code() == code) //
                .findFirst();
    }

    @Override
    public int code() {
        return this.ordinal();
    }
}


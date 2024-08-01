package com.cmiethling.mplex.device;

import java.util.stream.Stream;

/**
 * Defines the subsystems available in the hardware interface.
 */
public enum Subsystem {

    /**
     * Safety functions (e.g. doors)
     */
    SAFETY("safety"),

    /**
     * Camera and laser control.
     */
    OPTICS("optics"),

    /**
     * High voltage system control hv.
     */
    HIGH_VOLTAGE("highvoltage"),

    /**
     * Fluidics control.
     */
    FLUIDICS("fluidics"),

    /**
     * Motors control.
     */
    MOTOR_CONTROL("motorcontrol"),

    /**
     * Gel cooling gc.
     */
    GEL_COOLING("gelcooling"),

    /**
     * Cartridge temperature control ctc.
     */
    CARTRIDGE_TEMPERATURE("cartridgetemp"),

    /**
     * Thermo cycler control tc. (Do not rename to CYCLER as the event log may have entries of THERMAL_CYCLER.)
     */
    THERMAL_CYCLER("thermocycler"),

    /**
     * Collection of scanner functions.
     */
    SCANNER("scanner"),

    /**
     * Collection of global functions.
     */
    GLOBAL("global"),

    /**
     * Commands used for testing only.
     */
    TEST("test");

    private final String id;

    Subsystem(final String id) {
        this.id = id;
    }

    /**
     * Returns the enum value that is associated with the specified id. If no enum value can be found for this id an
     * {@link IllegalArgumentException} will be thrown.
     *
     * @param id the id to look for
     * @return the value associated with this id
     * @throws IllegalArgumentException if no enum value can be found for this id
     */
    public static Subsystem valueOfId(final String id) {
        return Stream.of(Subsystem.values()) //
                .filter(value -> value.id().equals(id)) //
                .findFirst() //
                .orElseThrow(() -> new IllegalArgumentException(id));
    }

    /**
     * Returns the id of the subsystem that is used in communication.
     *
     * @return the id of the subsystem
     */
    public String id() {
        return this.id;
    }
}


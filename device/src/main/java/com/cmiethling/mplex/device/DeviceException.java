package com.cmiethling.mplex.device;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

/**
 * This is the base exception for all device-related exceptions. The application should never use this base class but
 * concrete subclasses.
 */
public sealed abstract class DeviceException extends Exception permits DeviceMessageException, DeviceCommandException {

    @Serial
    private static final long serialVersionUID = 1;

    private final Object[] args;

    public DeviceException(final String message, final Exception exception, final Object... args) {
        super(message, exception);
        this.args = args;
    }

    public DeviceException(final String message, final Object... args) {
        super(message);
        this.args = args;
    }

    public List<Object> getArgs() {
        return Arrays.asList(this.args);
    }

    public abstract String getId();
}
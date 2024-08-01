package com.cmiethling.mplex.device;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

/**
 * This exception is thrown if there is a problem while handling the messages, e.g. mapping the messages to and from
 * JSON.
 */
public final class DeviceMessageException extends Exception {

    @Serial
    private static final long serialVersionUID = 1;

    private final Object[] args;

    public DeviceMessageException(final String message, final Exception exception, final Object... args) {
        super(message, exception);
        this.args = args;
    }

    public DeviceMessageException(final String message, final Object... args) {
        super(message);
        this.args = args;
    }

    public List<Object> getArgs() {
        return Arrays.asList(this.args);
    }

    public String getId() {
        return "MSG";
    }
}
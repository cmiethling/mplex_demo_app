package com.cmiethling.mplex.device;

/**
 * This exception is thrown if there is a problem while handling the messages, e.g. mapping the messages to and from
 * JSON.
 */
public final class DeviceMessageException extends DeviceException {

    public DeviceMessageException(final String message, final Exception exception, final Object... args) {
        super(message, exception, args);
    }

    public DeviceMessageException(final String message, final Object... args) {
        super(message, args);
    }

    @Override
    public String getId() {
        return "MSG";
    }
}
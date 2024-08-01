package com.cmiethling.mplex.device;

import lombok.NonNull;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

/**
 * This exception is thrown if there is a problem while handling the messages, e.g. mapping the messages to and from
 * JSON.
 */
public final class DeviceCommandException extends Exception {

    @Serial
    private static final long serialVersionUID = 1;

    private final Object[] args;

    /**
     * Creates a new exception for a known command error.
     *
     * @param subsystem the subsystem
     * @param topic     the topic
     * @param code      the command error
     */
    public DeviceCommandException(final Subsystem subsystem, final String topic, @NonNull final String code,
                                  final Object... args) {
        super("%s=%s, %s=%s: %s".formatted(AbstractDeviceMessage.SYSTEM, subsystem,
                AbstractDeviceMessage.TOPIC, topic, code));
        this.args = args;
    }

    public List<Object> getArgs() {
        return Arrays.asList(this.args);
    }

    public String getId() {
        return "CMD";
    }
}
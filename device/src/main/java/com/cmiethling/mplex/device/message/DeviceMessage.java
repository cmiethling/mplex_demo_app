package com.cmiethling.mplex.device.message;

import java.util.UUID;

/**
 * This interface defines a device messages.
 */
public sealed interface DeviceMessage permits AbstractDeviceMessage {

    /**
     * Returns the unique id for this message. Depending on the workflow the id might not have been initialized yet. In
     * this case this method will return {@code null}.
     *
     * @return a unique id, never {@code null}
     */
    UUID getId();

    /**
     * Returns the subsystem this message is targeted to or originated from.
     *
     * @return the subsystem, never {@code null}
     */
    Subsystem getSubsystem();

    /**
     * Returns the topic of this message, e.g. the function to execute or the topic of the event.
     *
     * @return the topic, never {@code null}
     */
    String getTopic();

    /**
     * Provides access to the parameters of this message.
     *
     * @return the parameters
     */
    MessageParameters parameters();
}


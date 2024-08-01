package com.cmiethling.mplex.device;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

/**
 * This interface defines a device messages.
 */
public sealed interface DeviceMessage permits AbstractDeviceMessage {
    static String toJson(final DeviceMessage message) throws DeviceMessageException {
        try {
            return AbstractDeviceMessage.objectMapper.writeValueAsString(message);
        } catch (final JsonProcessingException ex) {
            throw new DeviceMessageException("Error writing DeviceMessage to Json string", ex, message);
        }
    }

    static DeviceMessage toDeviceMessage(final String json) throws DeviceMessageException {
        try {
            return AbstractDeviceMessage.objectMapper.readValue(json, AbstractDeviceMessage.class);
        } catch (final JsonProcessingException ex) {
            throw new DeviceMessageException("error reading Json", ex, json);
        }
    }

    /**
     * Checks if this is a request message.
     *
     * @return {@code true} if this message is a request, otherwise {@code false}
     */
    boolean isRequest();

    boolean isResult();

    boolean isEvent();

    /**
     * Returns this message as a {@link RequestMessage}.
     *
     * @return the request message
     * @throws DeviceMessageException if this message cannot be cast into a command
     */
    RequestMessage asRequest() throws DeviceMessageException;

    ResultMessage asResult() throws DeviceMessageException;

    EventMessage asEvent() throws DeviceMessageException;

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


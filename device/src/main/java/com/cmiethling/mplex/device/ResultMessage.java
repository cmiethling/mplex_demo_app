package com.cmiethling.mplex.device;

import com.cmiethling.mplex.device.impl.JsonMapping;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

import static com.cmiethling.mplex.device.AbstractDeviceMessage.*;

/**
 * This class represents a result that is sent from the device interface to the application. It is a response to a
 * request message.
 */
@JsonPropertyOrder({TYPE, ID, SYSTEM, TOPIC, ERROR, RESULT})
public final class ResultMessage extends AbstractDeviceMessage {

    /**
     * Name of the parameter used for the command return value.
     */
    public static final String COMMAND_RETURN_VALUE = "retVal";

    @JsonDeserialize(using = JsonMapping.ResultErrorDeserializer.class)
    @JsonSerialize(using = JsonMapping.ResultErrorSerializer.class)
    @JsonProperty(ERROR)
    @NonNull
    @Getter
    @Setter
    private ResultError error = ResultError.NONE;

    /**
     * Creates a new result message object.
     *
     * @param id     a unique id
     * @param system the subsystem
     * @param topic  the topic
     */
    public ResultMessage(@NonNull final UUID id, @NonNull final Subsystem system, @NonNull final String topic) {
        super(id, system, topic);
    }

    // for deserialization
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ResultMessage(@NonNull @JsonProperty(ID) final UUID id,
                         @NonNull @JsonProperty(SYSTEM) final Subsystem subsystem,
                         @NonNull @JsonProperty(TOPIC) final String topic,
                         @NonNull @JsonProperty(ERROR) final ResultError error,
                         @NonNull @JsonProperty(RESULT) final MessageParameters params) {
        super(id, subsystem, topic);
        this.parameters().putAll(params);
        this.error = error;
    }

    @Override
    @JsonProperty(RESULT)
    public MessageParameters parameters() {
        return super.parameters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.parameters, this.error);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj))
            return false;
        final var other = (ResultMessage) obj;
        return this.error == other.error;
    }

    @Override
    public String toString() {
        return String.format("%s [%s, %s=%s, %s=%s]",
                getClass().getSimpleName(),
                super.toString(),
                ERROR,
                this.error,
                RESULT,
                this.parameters());
    }
}


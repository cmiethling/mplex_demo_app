package com.cmiethling.mplex.device;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * Error given back by HAL as the "error" parameter of a result (not {@code retVal}!).
 */
public enum ResultError {

    /**
     * This is the value for any unknown error.
     *
     * @implNote the String must not be {@code null}+empty otherwise the <strong>emulator</strong> can't throw a
     * {@link ResultError#UNKNOWN}
     */
    UNKNOWN("Some String", false),

    /**
     * This is for the emulator only.
     */
    EMULATOR_BUSY("EmulatorBusy", false),
    /**
     * HAL reports no error. The request sent to the device was processed successfully.
     */
    NONE("NoError", false),
    /**
     * HAL reports an invalid subsystem. This is a syntax error, meaning the request sent to HAL could not be read
     * properly.
     */
    INVALID_SUBSYSTEM("InvalidSubsystem", true),
    /**
     * HAL reports an invalid topic name. This is a syntax error, meaning the
     * request sent to HAL could not be read properly.
     */
    INVALID_TOPIC("InvalidTopic", true),
    /**
     * HAL reports invalid parameters. At least one parameter (name or type) is invalid. This is a syntax error, meaning
     * the request sent to HAL could not be read properly.
     */
    INVALID_PARAMETERS("InvalidParameters", true),
    /**
     * HAL reports a "topic error". (This is a command error)
     */
    COMMAND_ERROR("TopicError", false);

    private final String code;
    /**
     * -- GETTER --
     * Returns {@code true} if this error is a syntactical error (HAL could not read sent request), otherwise
     * {@code false}.
     *
     * @return if this is a syntax error
     */
    @Getter
    private final boolean syntaxError;

    ResultError(final String code, final boolean syntaxError) {
        this.code = code;
        this.syntaxError = syntaxError;
    }

    /**
     * Converts the String into an ErrorParameter enum-value.
     *
     * @param code the String to convert
     * @return the ErrorParameter
     * @throws IllegalArgumentException if the input-String was not a valid ErrorParameter-String
     */
    public static ResultError ofCode(final String code) {
        return Stream.of(ResultError.values()) // all enu values
                .filter(value -> value.code() != null) // remove those with no code equivalent
                .filter(value -> value.code().equals(code)) // try to match the code
                .findFirst().orElseThrow(() -> new IllegalArgumentException(code));
    }

    /**
     * Returns the errorParameter as String.
     *
     * @return the value as String.
     */
    public String code() {
        return this.code;
    }
}


package com.cmiethling.mplex.emulator.model;

import com.cmiethling.mplex.device.message.EventMessage;
import com.cmiethling.mplex.device.message.RequestMessage;
import com.cmiethling.mplex.device.message.ResultMessage;
import com.cmiethling.mplex.device.message.Subsystem;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageEntry {

    @Getter
    private final LocalDateTime timestamp;
    @Getter
    private final MessageEntryType type;
    @Getter
    private final Subsystem subsystem;
    @Getter
    private final String topic;
    private final String in;
    private final String out;
    @Getter
    private final String thymeleafTimestamp;

    public MessageEntry(final LocalDateTime timestamp, final MessageEntryType type, final Subsystem subsystem,
                        final String topic, final String in,
                        final String out) {
        this.timestamp = timestamp;
        this.type = type;
        this.subsystem = subsystem;
        this.topic = topic;
        this.in = in;
        this.out = out;
        this.thymeleafTimestamp = timestamp.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss"));
    }

    public static MessageEntry ofEvent(final EventMessage event) {
        return new MessageEntry(LocalDateTime.now(), MessageEntryType.EVENT, event.getSubsystem(), event.getTopic(),
                null, event.toString());
    }

    public static MessageEntry ofRequest(final RequestMessage request) {
        return new MessageEntry(LocalDateTime.now(), MessageEntryType.REQUEST, request.getSubsystem(),
                request.getTopic(), request.toString(), null);
    }

    public static MessageEntry ofResult(final ResultMessage result) {
        return new MessageEntry(LocalDateTime.now(), MessageEntryType.RESULT, result.getSubsystem(),
                result.getTopic(), null, result.toString());
    }

    /**
     * Returns incoming request in JSON format.
     *
     * @return incoming request in JSON format
     */
    public String getRequest() {
        if (this.type == MessageEntryType.REQUEST)
            return this.in;
        return null;
    }

    /**
     * Returns outgoing result in JSON format.
     *
     * @return outgoing result in JSON format
     */
    public String getResult() {
        if (this.type == MessageEntryType.RESULT)
            return this.out;
        return null;
    }

    /**
     * Returns outgoing event in JSON format.
     *
     * @return outgoing event in JSON format
     */
    public String getEvent() {
        if (this.type == MessageEntryType.EVENT)
            return this.out;
        return null;
    }
}

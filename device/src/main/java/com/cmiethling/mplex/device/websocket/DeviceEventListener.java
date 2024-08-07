package com.cmiethling.mplex.device.websocket;

import com.cmiethling.mplex.device.api.DeviceEvent;

/**
 * This functional interface is used to receive device events.
 */
@FunctionalInterface
public interface DeviceEventListener {

    /**
     * Processes an event.
     *
     * @param <T>   the type of the event
     * @param event the event to process
     */
    <T extends DeviceEvent> void onEvent(T event);
}

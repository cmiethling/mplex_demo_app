package com.cmiethling.mplex.client.core;

import com.cmiethling.mplex.device.api.DeviceEvent;

/**
 * This functional interface is used to receive device events.
 */
@FunctionalInterface
public interface DeviceEventListener<E extends DeviceEvent> {

    /**
     * Processes an event.
     *
     * @param event the event to process
     */
    void onEvent(E event);
}

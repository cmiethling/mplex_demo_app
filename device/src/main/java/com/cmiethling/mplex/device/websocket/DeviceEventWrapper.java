package com.cmiethling.mplex.device.websocket;

import com.cmiethling.mplex.device.api.DeviceEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeviceEventWrapper<E extends DeviceEvent> extends ApplicationEvent {
    private final E event;

    public DeviceEventWrapper(final Object source, final E event) {
        super(source);
        this.event = event;
    }
}

package com.cmiethling.mplex.client.core;

import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.api.DeviceCommand;
import com.cmiethling.mplex.device.service.EventCommandFactory;
import com.cmiethling.mplex.device.service.WebSocketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class DeviceCorePart {
    @Autowired
    private final WebSocketServiceImpl webSocketService;
    @Autowired
    private final EventCommandFactory eventCommandFactory;

    public DeviceCorePart(final WebSocketServiceImpl webSocketService, final EventCommandFactory eventCommandFactory) throws DeviceException {
        this.webSocketService = webSocketService;
        this.eventCommandFactory = eventCommandFactory;
        // this.webSocketService.openConnection();
    }
    // TODO: init(), stop() rein (irgendwie mit Clientapp verknüpfen...)

    public <T extends DeviceCommand> T sendCommand(final T command) throws DeviceException, ExecutionException,
            InterruptedException {
        return this.webSocketService.sendCommand(command).get();
    }

    public <T extends DeviceCommand> T command(final Class<T> command) {
        return this.eventCommandFactory.command(command);
    }

    public void openConnection() throws DeviceException {
        this.webSocketService.openConnection();
    }

    public boolean closeConnection() throws ExecutionException, InterruptedException,
            TimeoutException {
        return this.webSocketService.sendClose().get(500, TimeUnit.MILLISECONDS);
    }
}

package com.cmiethling.mplex.client.core;

import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.api.DeviceCommand;
import com.cmiethling.mplex.device.service.EventCommandFactory;
import com.cmiethling.mplex.device.service.WebSocketService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class DeviceCorePart {

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private EventCommandFactory eventCommandFactory;

    // @PostConstruct
    // public void init() throws DeviceException {
    //     openConnection();
    // }

    @PreDestroy
    public void cleanup() throws ExecutionException, InterruptedException, TimeoutException {
        closeConnection();
    }

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

    @SuppressWarnings("UnusedReturnValue")
    public boolean closeConnection() throws ExecutionException, InterruptedException,
            TimeoutException {
        return this.webSocketService.sendClose().get(1, TimeUnit.SECONDS);
    }
}

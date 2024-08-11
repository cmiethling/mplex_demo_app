package com.cmiethling.mplex.client;

import com.cmiethling.mplex.client.core.DeviceCorePart;
import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.api.DeviceCommand;
import com.cmiethling.mplex.device.message.Subsystem;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;

public abstract class AbstractSubsystem {

    private final Subsystem subsystem;

    @Autowired
    private DeviceCorePart deviceCorePart;

    protected AbstractSubsystem(@NonNull final Subsystem subsystem) {
        this.subsystem = subsystem;
    }

    protected <T extends DeviceCommand> T command(final Class<T> command) {
        return this.deviceCorePart.command(command);
    }

    protected <T extends DeviceCommand> T sendCommand(final T command)
            throws DeviceException, ExecutionException, InterruptedException {
        return this.deviceCorePart.sendCommand(command);
    }
}


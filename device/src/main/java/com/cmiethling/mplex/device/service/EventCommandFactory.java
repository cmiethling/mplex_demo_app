package com.cmiethling.mplex.device.service;

import com.cmiethling.mplex.device.api.DeviceCommand;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service to return a Command or an Event.
 */
@Service
public class EventCommandFactory {
    /**
     * Returns the desired command.
     *
     * @param commandClass the class for the desired command
     * @return the command
     */
    public <T extends DeviceCommand> T command(@NonNull final Class<T> commandClass) {
        try {
            return commandClass.getDeclaredConstructor().newInstance();
        } catch (final Exception ex) {
            throw new RuntimeException("Error creating command instance", ex);
        }
    }
}

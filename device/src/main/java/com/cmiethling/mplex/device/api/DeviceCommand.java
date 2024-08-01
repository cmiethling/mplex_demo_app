package com.cmiethling.mplex.device.api;

import com.cmiethling.mplex.device.*;
import com.cmiethling.mplex.device.impl.DeviceModule;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This interface describes a command that could be sent to the device. It combines the {@link RequestMessage} with the
 * {@link ResultMessage}.
 */
public interface DeviceCommand {

    /**
     * Returns all classes implementing device commands. This method can be used to dynamically work with the provided
     * commands. If the application wants to use a specific command it should use the appropriate command class
     * directly.
     *
     * @return a collection a command classes
     */
    static Collection<Class<? extends DeviceCommand>> getAvailableCommands() {

        final var apiPackageName = "com/cmiethling/mplex/device/api/";
        final var classFileExtension = ".class";
        final var resourceNameSeparator = '/';
        final var packageNameSeparator = '.';

        final var resolvedModule = ModuleLayer.boot().configuration().findModule(DeviceModule.NAME)
                .orElseThrow(() -> new InternalError("Module not found: " + DeviceModule.NAME));
        try (final var moduleReader = resolvedModule.reference().open()) {
            return moduleReader.list()
                    // api package only
                    .filter(resourceName -> resourceName.startsWith(apiPackageName))
                    // class resources only
                    .filter(resourceName -> resourceName.endsWith(classFileExtension))
                    // remove the extension
                    .map(resourceName -> resourceName.substring(0, resourceName.length() - classFileExtension.length()))
                    // convert to class name
                    .map(resourceName -> resourceName.replace(resourceNameSeparator, packageNameSeparator))
                    // load the class
                    .map(DeviceCommand::toDeviceCommandClass)
                    // filter out nulls
                    .filter(Objects::nonNull)
                    // create a collection of the classes
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (final IOException ex) {
            DeviceModule.logger().warn("", ex);
            return Collections.emptyList();
        }
    }

    private static Class<? extends DeviceCommand> toDeviceCommandClass(final String className) {
        try {
            // load the class
            final var candidateClass = Class.forName(className);

            // must be public and not abstract
            final var modifiers = candidateClass.getModifiers();
            if (!Modifier.isPublic(modifiers) || Modifier.isAbstract(modifiers))
                return null;

            // must implement DeviceCommand
            if (!DeviceCommand.class.isAssignableFrom(candidateClass))
                return null;

            @SuppressWarnings("unchecked") final var deviceCommandClass =
                    (Class<? extends DeviceCommand>) candidateClass;

            return deviceCommandClass;
        } catch (final ReflectiveOperationException ex) {
            DeviceModule.logger().warn("", ex);
            return null;
        }
    }

    /**
     * Returns the subsystem of this command. The subsystem is used to configure the request message and validate the
     * result message.
     *
     * @return the subsystem this command is targeted to
     */
    Subsystem getSubsystem();

    /**
     * Returns the topic of the command which will be invoked at the subsystem. The topic is used to configure the
     * request message and validate the result message.
     *
     * @return the topic name of this command
     */
    String getTopic();

    /**
     * Sets a new id generator for this command. Usually the command uses a default id generator. This may be overridden
     * for special cases like tests. If the specified generator is {@code null} the default generator is used.
     *
     * @param idGenerator the new id generator
     */
    void setIdGenerator(Supplier<UUID> idGenerator);

    /**
     * Create a request message from this command. The message will be created from the subsystem, the method and all
     * parameters.
     *
     * @return a request message to be sent to the device
     * @throws DeviceMessageException if the request message could not be created, e.g. because of missing properties
     */
    RequestMessage toRequestMessage() throws DeviceMessageException;

    /**
     * Populates the results of this command from a result message received from the device. This method also checks if
     * the result message matches the request message.
     *
     * @param message the result message received from the device
     * @throws DeviceMessageException if there is a problem evaluating the result message
     * @throws DeviceCommandException if the device returned with an error
     */
    void fromResultMessage(ResultMessage message) throws DeviceMessageException, DeviceCommandException;
}


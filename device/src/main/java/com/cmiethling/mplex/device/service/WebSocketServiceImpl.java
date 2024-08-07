package com.cmiethling.mplex.device.service;

import com.cmiethling.mplex.device.DeviceCommunicationException;
import com.cmiethling.mplex.device.DeviceException;
import com.cmiethling.mplex.device.api.DeviceCommand;
import com.cmiethling.mplex.device.config.DeviceModule;
import com.cmiethling.mplex.device.websocket.CommandTaskInfo;
import com.cmiethling.mplex.device.websocket.DeviceEventListener;
import com.cmiethling.mplex.device.websocket.MyWebSocketListener;
import com.cmiethling.mplex.device.websocket.WebSocketUtils;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the websocket client interface.
 */
public final class WebSocketServiceImpl implements WebSocketService {

    private static final Logger log = DeviceModule.logger();

    // beans
    private final URI uri;
    private final ConcurrentMap<UUID, CommandTaskInfo<? extends DeviceCommand>> commandTasks;
    private final List<DeviceEventListener> deviceEventListeners;
    private final MyWebSocketListener myWebSocketListener;
    private final DeviceMessageService deviceMessageService;

    private WebSocket webSocketClient;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public WebSocketServiceImpl(final URI uri,
                                final ConcurrentMap<UUID, CommandTaskInfo<? extends DeviceCommand>> commandTasks,
                                final List<DeviceEventListener> deviceEventListeners,
                                final MyWebSocketListener myWebSocketListener,
                                final DeviceMessageService deviceMessageService) {
        this.uri = uri;
        this.commandTasks = commandTasks;
        this.deviceEventListeners = deviceEventListeners;
        this.myWebSocketListener = myWebSocketListener;
        this.deviceMessageService = deviceMessageService;
    }

    // ########################## Device methods ##########################
    @Override
    public void openConnection() throws DeviceException {
        try (final var client = HttpClient.newHttpClient()) {
            final var webSocket = client.newWebSocketBuilder().buildAsync(this.uri, this.myWebSocketListener).get();
            log.info("Device connection established");
            this.webSocketClient = webSocket;
        } catch (final ExecutionException ex) {// unwrap ExeExc
            throw new DeviceCommunicationException("connectionFailed", (Exception) ex.getCause());
        } catch (final InterruptedException ex) {
            throw new DeviceCommunicationException("connectionFailed", ex);
        }
    }

    @Override
    public Future<Boolean> sendClose() {
        this.executor.shutdownNow();
        if (this.webSocketClient != null) {
            return this.webSocketClient.sendClose(WebSocket.NORMAL_CLOSURE, "closed WebSocket connection")
                    // ### use thenApplyAsync() as main thread could be used with thenApply()!### see:
                    // https://stackoverflow.com/questions/27723546/completablefuture-supplyasync-and-thenapply
                    .thenApplyAsync(ws -> Boolean.TRUE)// if no exception >> ws is closed properly
                    .exceptionally(ex -> {
                        log.error("", ex);
                        return Boolean.FALSE;
                    });
        }
        return CompletableFuture.completedFuture(Boolean.FALSE);
    }

    @Override
    public <T extends DeviceCommand> Future<T> sendCommand(final T command, final Duration duration) throws DeviceException {

        // before we create a task for sending the message we need to perform same preparations
        // get the message to send and the id for reference
        final var requestMessage = command.toRequestMessage();
        final var messageId = requestMessage.getId();

        // make sure we have a connection
        ensureConnected();

        final Callable<T> processCommandMessage = () -> {
            try {
                // convert the message to JSON
                final var json = this.deviceMessageService.serializeMessage(requestMessage);

                // log the request with a request-specific logger
                WebSocketUtils.logMessage(requestMessage);

                // send the text (can only be called one at a time!)
                WebSocketUtils.logJsonMessage(WebSocketUtils.sendLogger, "Sending request to Device", json);
                synchronized (this) {
                    this.webSocketClient.sendText(json, true).get();
                }
                WebSocketUtils.sendLogger.debug("Request sent.");

                final var taskInfo = this.commandTasks.get(messageId);
                if (taskInfo == null)
                    throw new IllegalStateException("CommandTaskInfo must be put in map BEFORE it is started");
                // wait for the result
                if (!taskInfo.waitForResult(TimeUnit.MILLISECONDS.convert(duration), TimeUnit.MILLISECONDS))
                    throw new TimeoutException();

                // get the result
                final var resultMessage = taskInfo.getResultMessage();

                // set the result to the command and return it
                command.fromResultMessage(resultMessage);
                return command;
            } finally {
                // make sure we unregister the task
                this.commandTasks.remove(messageId);
            }
        };
        final var commandTaskInfo = new CommandTaskInfo<>(processCommandMessage, this.executor);
        this.commandTasks.put(messageId, commandTaskInfo);
        // commandTaskInfo is in map >> now send the message and wait for the answer (no race condition can occur)
        commandTaskInfo.start();
        // null can never happen here
        return requireNonNull(commandTaskInfo.getTask());
    }

    @Override
    public void cancelAllCommands() {
        this.commandTasks.values().forEach(CommandTaskInfo::cancel);
    }

    @Override
    public synchronized void ensureConnected() throws DeviceException {
        if (this.webSocketClient == null || this.webSocketClient.isOutputClosed()) {
            log.info(this.webSocketClient == null ? "Device not connected yet, trying to connect to device..."
                    : "Device connection was closed, trying to reconnect to device...");
            openConnection();
        }
        if (this.executor.isShutdown()) // for testing {@code sendClose()} only, can't happen after closing app :)
            this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void addDeviceEventListener(final DeviceEventListener l) {this.deviceEventListeners.add(l);}

    @Override
    public void removeDeviceEventListener(final DeviceEventListener l) {this.deviceEventListeners.remove(l);}

    // #########################################

    /**
     * For test purposes only.
     *
     * @return the map with the waiting command tasks
     */
    ConcurrentMap<UUID, CommandTaskInfo<?>> getCommandTasks() {return this.commandTasks;}
}


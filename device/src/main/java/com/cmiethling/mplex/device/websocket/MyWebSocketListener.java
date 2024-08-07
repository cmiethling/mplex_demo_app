package com.cmiethling.mplex.device.websocket;

import com.cmiethling.mplex.device.DeviceMessageException;
import com.cmiethling.mplex.device.api.DeviceCommand;
import com.cmiethling.mplex.device.api.DeviceEvent;
import com.cmiethling.mplex.device.config.DeviceModule;
import com.cmiethling.mplex.device.message.DeviceMessage;
import com.cmiethling.mplex.device.message.EventMessage;
import com.cmiethling.mplex.device.message.ResultMessage;
import com.cmiethling.mplex.device.service.DeviceMessageService;
import org.slf4j.Logger;

import java.net.http.WebSocket;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentMap;

public class MyWebSocketListener implements WebSocket.Listener {

    private static final Logger log = DeviceModule.logger();
    // beans
    private final DeviceMessageService deviceMessageService;
    private final List<DeviceEventListener> deviceEventListeners;
    private final ConcurrentMap<UUID, CommandTaskInfo<? extends DeviceCommand>> commandTasks;
    private final StringBuilder textData = new StringBuilder();

    public MyWebSocketListener(final List<DeviceEventListener> deviceEventListeners, final ConcurrentMap<UUID,
            CommandTaskInfo<? extends DeviceCommand>> commandTasks, final DeviceMessageService deviceMessageService) {
        this.deviceEventListeners = deviceEventListeners;
        this.commandTasks = commandTasks;
        this.deviceMessageService = deviceMessageService;
    }

    /**
     * The application receives a {@link ResultMessage} or {@link EventMessage} from the hardware (via the
     * {@link java.net.http.WebSocket.Listener WebSocket.Listener}) and informs all {@link DeviceEventListener
     * DeviceListeners}.
     * <p>
     * Note that {@code onText()} itself is synchronous so it can only be called one at a time, but the returned
     * CompletionStage (=CompletableFuture) is asynchronous so one has to differentiate
     * </p>
     * <p>
     * For instance: given are 2 messages msg with delay d: msg1(d1=50ms), msg2(d2=0)
     * </p>
     * <ul>
     * <li>if delay was in synchronous onText() >> order will stay the same as without delay: {msg1, msg2}</li>
     * <li>if delay was in asynchronous CompletableFuture >> order will (most likely) change: {msg2, msg1}</li>
     * </ul>
     */
    @Override
    public CompletionStage<?> onText(final WebSocket webSocket, final CharSequence data, final boolean last) {
        webSocket.request(1);
        return onText(data, last);
    }

    CompletableFuture<?> onText(final CharSequence data, final boolean last) {
        // StringBuilder must be processed in (synchronous) onText() otherwise there might be raceConditions
        this.textData.append(data);

        if (last) {
            final var json = this.textData.toString();
            this.textData.setLength(0); // reset for next message
            return CompletableFuture.runAsync(() -> {
                WebSocketUtils.logJsonMessage(WebSocketUtils.receiveLogger, "Received from device", json);
                try {
                    computeReceivedMessage(this.deviceMessageService.deserializeMessage(json));
                } catch (final DeviceMessageException ex) {
                    log.error("", ex);
                    throw new CompletionException(ex);
                }
            });
        }
        // no idea what this return really does... "an indication [the WebSocket] may reclaim the CharSequence" ???
        return new CompletableFuture<>();// this is safest because null lets the "CharSequence reclaim immediately"???
    }

    void computeReceivedMessage(final DeviceMessage message) throws DeviceMessageException {
        if (message.isResult()) {
            final var resultMessage = message.asResult();
            // log the result with a result-specific logger
            WebSocketUtils.logMessage(resultMessage);

            // find a waiting task by the id
            final var messageId = resultMessage.getId();
            final var taskInfo = this.commandTasks.get(messageId);
            if (taskInfo != null) {
                // provide the message to the task
                taskInfo.setResultMessage(resultMessage);
            } else
                WebSocketUtils.receiveLogger.warn("Unexpected result message, id: " + messageId);
        } else if (message.isEvent()) {
            final var eventMessage = message.asEvent(); // outside ComFut because of checkedExc

            // log the event with an event-specific logger
            WebSocketUtils.logMessage(eventMessage);

            final var event = DeviceEvent.of(eventMessage.getSubsystem(), eventMessage.getTopic());
            event.fromEventMessage(eventMessage);

            this.deviceEventListeners.forEach(listener -> listener.onEvent(event));
        } else {
            throw new DeviceMessageException("invalidMessageType: " + message);
        }
    }

    @Override
    public CompletionStage<?> onClose(final WebSocket webSocket, final int statusCode, final String reason) {
        log.info("WebSocketClient onClose={}   >> status={}, reason: {}", webSocket, statusCode, reason);
        this.commandTasks.clear();
        return null;
    }

    @Override
    public void onError(final WebSocket webSocket, final Throwable error) {
        log.error("Device connection error: ", error);
    }

    /**
     * For test purposes only.
     *
     * @return the map with the waiting command tasks
     */
    ConcurrentMap<UUID, CommandTaskInfo<?>> getCommandTasks() {return this.commandTasks;}
}
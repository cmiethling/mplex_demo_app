package com.cmiethling.mplex.device.config;

import com.cmiethling.mplex.device.api.DeviceCommand;
import com.cmiethling.mplex.device.service.DeviceMessageService;
import com.cmiethling.mplex.device.service.WebSocketServiceImpl;
import com.cmiethling.mplex.device.websocket.CommandTaskInfo;
import com.cmiethling.mplex.device.websocket.DeviceEventListener;
import com.cmiethling.mplex.device.websocket.MyWebSocketListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Configuration
public class WebSocketConfig {

    @Value("${websocket.uri}")
    private String webSocketUri;

    @Bean("uri")
    public URI getWebSocketUri() {
        System.out.println(this.webSocketUri);
        return URI.create(this.webSocketUri);
    }

    @Bean
    public List<DeviceEventListener> deviceEventListeners() {
        return new CopyOnWriteArrayList<>();
    }

    @Bean
    public ConcurrentMap<UUID, CommandTaskInfo<? extends DeviceCommand>> commandTasks() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public MyWebSocketListener myWebSocketListener2(
            final List<DeviceEventListener> deviceEventListeners,
            final ConcurrentMap<UUID, CommandTaskInfo<? extends DeviceCommand>> commandTasks,
            final DeviceMessageService deviceMessageService) {
        return new MyWebSocketListener(deviceEventListeners, commandTasks, deviceMessageService);
    }

    @Bean
    public WebSocketServiceImpl webSocketServiceImpl(
            final URI uri,
            final ConcurrentMap<UUID, CommandTaskInfo<? extends DeviceCommand>> commandTasks,
            final List<DeviceEventListener> deviceEventListeners,
            final MyWebSocketListener myWebSocketListener,
            final DeviceMessageService deviceMessageService) {
        return new WebSocketServiceImpl(uri, commandTasks, deviceEventListeners, myWebSocketListener,
                deviceMessageService);
    }
}
package com.cmiethling.mplex.emulator.config;

import com.cmiethling.mplex.emulator.service.SocketTextHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final SocketTextHandler socketTextHandler;

    @Autowired
    public WebSocketConfig(final SocketTextHandler socketTextHandler) {
        this.socketTextHandler = socketTextHandler;
    }

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(this.socketTextHandler, "/hwAPI");
    }
}
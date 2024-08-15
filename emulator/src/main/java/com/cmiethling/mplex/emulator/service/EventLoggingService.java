package com.cmiethling.mplex.emulator.service;

import com.cmiethling.mplex.device.message.EventMessage;
import com.cmiethling.mplex.emulator.controller.LogController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventLoggingService {

    @Autowired
    private LogController logController;

    public void logEvent(final EventMessage eventMessage) {
        this.logController.logEvent(eventMessage);
    }
}



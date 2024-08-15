package com.cmiethling.mplex.emulator.controller;

import com.cmiethling.mplex.device.message.EventMessage;
import com.cmiethling.mplex.emulator.model.MessageEntry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LogController {

    private final List<MessageEntry> logEntries = new ArrayList<>();

    @GetMapping("/logs")
    public String showLogs(final Model model) {
        model.addAttribute("eventMessages", this.logEntries);
        return "logs";
    }

    @PostMapping("/log-event")
    public void logEvent(final EventMessage eventMessage) {
        this.logEntries.add(MessageEntry.ofEvent(eventMessage));
    }

    public List<MessageEntry> getLogEntries() {
        return this.logEntries;
    }
}

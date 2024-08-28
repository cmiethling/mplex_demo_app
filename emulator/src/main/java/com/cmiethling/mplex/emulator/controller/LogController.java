package com.cmiethling.mplex.emulator.controller;

import com.cmiethling.mplex.device.message.EventMessage;
import com.cmiethling.mplex.device.message.RequestMessage;
import com.cmiethling.mplex.device.message.ResultMessage;
import com.cmiethling.mplex.emulator.model.MessageEntry;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Getter
@Controller
public class LogController {

    private final List<MessageEntry> logEntries = new ArrayList<>();

    @GetMapping("/logs")
    public String showLogs(final Model model) {
        model.addAttribute("logEntries", this.logEntries);
        return "logs";
    }

    @PostMapping("/log-event")
    public void logEvent(final EventMessage eventMessage) {
        this.logEntries.add(MessageEntry.ofEvent(eventMessage));
    }

    @PostMapping("/log-request")
    public void logRequest(final RequestMessage request) {
        this.logEntries.add(MessageEntry.ofRequest(request));
    }

    @PostMapping("/log-result")
    public void logFullCommand(final RequestMessage request, final ResultMessage result) {
        this.logEntries.add(MessageEntry.ofCommand(request, result));
    }
}

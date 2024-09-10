package com.cmiethling.mplex.client.controller;

import com.cmiethling.mplex.client.core.DeviceCorePart;
import com.cmiethling.mplex.client.service.FluidicsService;
import com.cmiethling.mplex.device.DeviceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;

@SuppressWarnings("SameReturnValue")
@Controller
public class ServiceClientController {
    @Autowired
    private DeviceCorePart deviceCorePart;

    @Autowired
    private FluidicsService fluidicsService;

    @RequestMapping({"/service_client"})
    public String displayServiceClient() {
        return "service_client.html";
    }

    @PostMapping("/sendGelPumpModeCommand2")
    public String sendGelPumpModeCommand(@RequestParam final boolean isOn) throws DeviceException, ExecutionException
            , InterruptedException {
        this.fluidicsService.sendGelPumpMode(isOn);
        return "redirect:/service_client";
    }
}

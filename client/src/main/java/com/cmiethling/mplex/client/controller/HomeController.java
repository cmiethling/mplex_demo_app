package com.cmiethling.mplex.client.controller;

import com.cmiethling.mplex.client.core.DeviceCorePart;
import com.cmiethling.mplex.client.service.FluidicsService;
import com.cmiethling.mplex.client.service.HighVoltageService;
import com.cmiethling.mplex.device.DeviceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("SameReturnValue")
@Controller
public class HomeController {
    @Autowired
    private DeviceCorePart deviceCorePart;

    @Autowired
    private FluidicsService fluidicsService;
    @Autowired
    private HighVoltageService highVoltageService;

    @GetMapping({"/home", "/", ""})
    public String displayHome() {
        return "home.html";
    }

    @PostMapping("/sendGelPumpModeCommand")
    public String sendGelPumpModeCommand(@RequestParam final boolean isOn) throws DeviceException, ExecutionException
            , InterruptedException {
        this.fluidicsService.sendGelPumpMode(isOn);
        return "redirect:/home";
    }

    @PostMapping(value = "/test")
    public String openConnection(@RequestParam final String bla) throws DeviceException, InterruptedException,
            ExecutionException, TimeoutException {
        System.out.println(bla);
        if (bla.equals("true")) {
            this.deviceCorePart.openConnection();
        }
        if (bla.equals("false")) {
            this.deviceCorePart.closeConnection();
        }
        return "redirect:/home";
    }
}

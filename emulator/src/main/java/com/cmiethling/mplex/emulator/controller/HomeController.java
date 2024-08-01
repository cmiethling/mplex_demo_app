package com.cmiethling.mplex.emulator.controller;

import com.cmiethling.mplex.emulator.config.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping({Utils.HOME, "", "/"}) // 71, ex19
    public String displayHomePage() {
        return Utils.HOME_HTML; // die view (bei Spring MVC ModelViewController)
    }
}

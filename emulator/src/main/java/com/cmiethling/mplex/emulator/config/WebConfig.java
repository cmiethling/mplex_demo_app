package com.cmiethling.mplex.emulator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*   für statische htmls wo es keine Businesslogic gibt >> es braucht theoretisch keine Controller.java Klasse  */
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {

        // registry.addViewController("/logs").setViewName("logs");
    }
}

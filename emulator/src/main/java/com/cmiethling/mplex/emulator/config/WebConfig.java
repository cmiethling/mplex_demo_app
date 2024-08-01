package com.cmiethling.mplex.emulator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*   für statische htmls wo es keine Businesslogic gibt >> es braucht theoretisch keine Controller.java Klasse  */
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController(com.cmiethling.mplex.emulator.config.Utils.COURSES)// das wäre CoursesController.java
                .setViewName(com.cmiethling.mplex.emulator.config.Utils.COURSES.substring(1)); // die view: courses = courses.html
        //        83, ex21
        registry.addViewController(com.cmiethling.mplex.emulator.config.Utils.ABOUT).setViewName(com.cmiethling.mplex.emulator.config.Utils.ABOUT.substring(1));
    }
}

package com.cmiethling.mplex.emulator.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Utils {

    public static final String COURSES = "/courses";
    public static final String ABOUT = "/about";
    public static final String HOLIDAYS = "/holidays";
    public static final String HOME = "/home";
    public static final String CONTACT = "/contact";

    public static final String COURSES_HTML = COURSES.substring(1) + ".html";
    public static final String ABOUT_HTML = ABOUT.substring(1) + ".html";
    public static final String HOLIDAYS_HTML = HOLIDAYS.substring(1) + ".html";
    public static final String HOME_HTML = HOME.substring(1) + ".html";
    public static final String CONTACT_HTML = CONTACT.substring(1) + ".html";
}

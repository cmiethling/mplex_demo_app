package com.cmiethling.mplex.emulator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice // every exception in the whole emulator app will be sent here!
public class GlobalExceptionController {

    // @ExceptionHandler(RuntimeException.class)
    // public void exceptionHandler(final RuntimeException ex) {
    //     log.error("Unchecked: ", ex);
    // }

    // @ExceptionHandler(Exception.class)
    // public void exceptionHandler(final Exception ex) {
    //     log.error("Checked: ", ex);
    // }
}

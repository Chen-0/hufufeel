package me.rubick.transport.app.controller.common;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.HttpNoFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(HttpNoFoundException.class)
    public String httpNoFoundExceptionHandler(HttpNoFoundException e) {
        log.error("", e);
        return "redirect:/error/page_not_found";
    }


}

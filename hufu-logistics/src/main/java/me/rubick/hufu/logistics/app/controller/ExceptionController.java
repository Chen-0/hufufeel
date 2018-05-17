package me.rubick.hufu.logistics.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.exception.NotFoundException;
import me.rubick.hufu.logistics.app.library.Common;

@ControllerAdvice
public class ExceptionController {

    private Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(NotFoundException.class)
    public String catchNotFoundException(Exception e) {
        Common.logExceptionToFile(logger, e);
        return "exception/404";
    }

    @ExceptionHandler(BaseException.class)
    public String catchErrorException(Model model, Exception e) {
        Common.logExceptionToFile(logger, e);
        model.addAttribute("message", e.getMessage());
        return "exception/error";
    }

    @ExceptionHandler(Exception.class)
    public String catchException(Model model, Exception e) {
        Common.logExceptionToFile(logger, e);
        model.addAttribute("message", e.getMessage());
        return "exception/error";
    }

    @ExceptionHandler(RuntimeException.class)
    public String catchRuntimeException(Model model, Exception e) {
        Common.logExceptionToFile(logger, e);
        model.addAttribute("message", e.getMessage());
        return "exception/error";
    }
}

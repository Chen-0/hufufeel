package me.rubick.common.app.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class FormUtils {

    public static Map<String, String> toMap(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        return map;
    }
}

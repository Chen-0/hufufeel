package me.rubick.common.app.exception;

import java.util.Map;

public class FormException extends Exception {

    private Map<String, String> map;

    public FormException(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> getErrorField() {
        return this.map;
    }
}

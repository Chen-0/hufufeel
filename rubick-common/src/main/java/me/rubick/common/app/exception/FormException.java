package me.rubick.common.app.exception;

import java.util.Map;

public class FormException extends Exception {

    private Map<String, String> map;

    private Map<String, Object> form;

    public FormException(Map<String, String> map, Map<String, Object> form) {
        this.map = map;
        this.form = form;
    }

    public Map<String, String> getErrorField() {
        return this.map;
    }

    public Map<String, Object> getForm() {
        return this.form;
    }
}

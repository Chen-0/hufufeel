package me.rubick.common.app.helper;

import me.rubick.common.app.exception.FormException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FormHelper implements Serializable {

    private Map<String, String> field = new HashMap<>();

    private FormHelper() {}

    public static FormHelper getInstance() {
        return new FormHelper();
    }

    public void notNull(Object o, String fieldName) {
        notNull(o, fieldName, "不能为空");
    }

    public void notNull(Object o, String fieldName, String message) {
        if (o instanceof String) {
            String s = (String) o;
            notBlank(fieldName, s, message);
        } else {
            if (ObjectUtils.isEmpty(o)) {
                field.put(fieldName, message);
            }
        }
    }

    private void notBlank(String fieldName, String s, String message) {
        if (! StringUtils.hasText(s)) {
            field.put(fieldName, message);
        }
    }

    public void addError(String fieldName, String message) {
        field.put(fieldName, message);
    }

    public void hasError() throws FormException {
        if (field.size() > 0) {
            throw new FormException(this.field);
        }
    }
}

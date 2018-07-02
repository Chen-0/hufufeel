package me.rubick.common.app.helper;

import me.rubick.common.app.exception.FormException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FormHelper implements Serializable {

    private Map<String, String> field = new HashMap<>();

    private Map<String, Object> errorField = new HashMap<>();

    //粒度啊！！！！
    public class FormElement {

        private FormHelper formHelper;

        private String fieldName;

        private Object element;

        FormElement(FormHelper formHelper, String fieldName, Object o) {
            this.formHelper = formHelper;
            this.fieldName = fieldName;
            this.element = o;
        }

        public FormElement mustDecimal0() {
            String s = (String) this.element;
            if (!Pattern.matches(DECIMAL_PATTERN, s)) {
                addError("请输入整数或数字");
                return this;
            }

            BigDecimal t = new BigDecimal(s);
            BigDecimal x = BigDecimal.ZERO;
            BigDecimal y = new BigDecimal("999999999");

            if (t.compareTo(x) < 0 || t.compareTo(y) > 0) {
                addError("该元素的取值范围在【" + x + "，" + y + "】");
            }

            return this;
        }

        public FormElement notNull() {
            return notNull("不能为空");
        }

        private FormElement notNull(String message) {
            if (ObjectUtils.isEmpty(this.element)) {
                addError(message);
            }

            return this;
        }

        public FormElement notBlank() {
            return notBlank("不能为空");
        }

        private FormElement notBlank(String message) {
            String s = (String) this.element;
            if (!StringUtils.hasText(s)) {
                addError(message);
            }
            return this;
        }

        public FormElement maxLength(int max) {
            String s = (String) this.element;

            if (s.length() > max) {
                addError("长度不能超过" + max + "个字");
            }

            return this;
        }

        public FormElement range(BigDecimal x, BigDecimal y) {
            BigDecimal t = (BigDecimal) this.element;

            if (t.compareTo(x) < 0 || t.compareTo(y) > 0) {
                addError("该元素的取值范围在【" + x + "，" + y + "】");
            }

            return this;
        }

        private void addError(String message) {
            this.formHelper.field.put(fieldName, message);
            this.formHelper.errorField.put(fieldName, fieldName);
        }
    }

    //小数或数字的表达式
    private final static String DECIMAL_PATTERN = "^\\-?[0-9]{1,9}([.]{1}[0-9]{1,4}){0,1}$";

    private FormHelper() {
    }

    public static FormHelper getInstance() {
        return new FormHelper();
    }

    public FormElement validate(String fieldName, Object o) {
        return new FormElement(this, fieldName, o);
    }

    /**
     * 对表单进行常规校验（非空、最大最小值检查）
     * @param fieldName
     * @param o
     */
    public FormElement validateDefault0(String fieldName, Object o) {
        FormElement formElement = new FormElement(this, fieldName, o).notNull();

        if (o instanceof String) {
            return formElement.maxLength(32);
        } else if (o instanceof BigDecimal) {
            return formElement.range(BigDecimal.ZERO, new BigDecimal(100000));
        } else {
            return formElement;
        }
    }

    public void notNull(Object o, String fieldName) {
        notNull(o, fieldName, "不能为空");
    }

    public void mustDecimal(String s, String fieldName) {
        if (!Pattern.matches(DECIMAL_PATTERN, s)) {
            field.put(fieldName, "请输入整数或数字");
            errorField.put(fieldName, s);
        }
    }

    public void mustDate0(String fieldName, String s) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.parse(s);
        } catch (ParseException e) {
            field.put(fieldName, "请输入正确的日期（格式为：yyyy-MM-dd 例如：2018-01-01）");
            errorField.put(fieldName, s);
        }
    }

    public void notNull(Object o, String fieldName, String message) {
        if (o instanceof String) {
            String s = (String) o;
            notBlank(fieldName, s, message);
        } else {
            if (ObjectUtils.isEmpty(o)) {
                field.put(fieldName, message);
                errorField.put(fieldName, o);
            }
        }
    }

    public void notBlankAndMax(String s, String fieldName, int max) {
        notBlank(fieldName, s, "不能为空");
    }

    public void max(String s, String fieldName, int max) {

    }

    private void notBlank(String fieldName, String s, String message) {
        if (!StringUtils.hasText(s)) {
            field.put(fieldName, message);
            errorField.put(fieldName, s);
        }
    }

    public void addError(String fieldName, String message) {
        field.put(fieldName, message);
    }

    public void hasError() throws FormException {
        if (field.size() > 0) {
            throw new FormException(this.field, errorField);
        }
    }
}

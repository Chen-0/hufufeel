package me.rubick.common.app.utils;

import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Date;

public class TextUtils {

    public static String generateLink(String linkName, String url) {
        return MessageFormat.format("<a href=\"{0}\">{1}</a>", url, linkName);
    }

//    public static String getText(String o) {
//        if (StringUtils.hasText(o)) {
//            return o;
//        } else {
//        }
//    }

    public static String getOrDefault(String o, String d) {
        if (StringUtils.hasText(o)) {
            return o;
        } else {
            return d;
        }
    }
}

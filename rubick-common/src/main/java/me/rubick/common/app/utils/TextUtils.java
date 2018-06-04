package me.rubick.common.app.utils;

import java.text.MessageFormat;

public class TextUtils {

    public static String generateLink(String linkName, String url) {
        return MessageFormat.format("<a href=\"{0}\">{1}</a>", url, linkName);
    }
}

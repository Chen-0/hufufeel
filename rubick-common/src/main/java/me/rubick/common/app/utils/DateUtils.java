package me.rubick.common.app.utils;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.exception.CommonException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateUtils {

    public static Date stringToDate(String s) {
        if (!StringUtils.hasText(s)) {
            return null;
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(s);
        } catch (ParseException e) {
            log.error("", e);
            return new Date();
        }
    }

    public static String date2StringYMDHMS(Date date) {
        if (ObjectUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String date2String0(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    public static String date2String1(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getTimestamp0() {
        return DateUtils.date2String0(new Date()) + HashUtils.generateNumberString(2);
    }
}

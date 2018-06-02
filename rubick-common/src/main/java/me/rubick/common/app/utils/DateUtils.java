package me.rubick.common.app.utils;

import me.rubick.common.app.exception.CommonException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date stringToDate(String s) throws CommonException {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.parse(s);
        } catch (ParseException e) {
            throw new CommonException(e);
        }
    }
}
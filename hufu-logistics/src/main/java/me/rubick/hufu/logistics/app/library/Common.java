package me.rubick.hufu.logistics.app.library;

import org.slf4j.Logger;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Common {

    public static String generateString(int length) {
        final String word = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        return generateRandomString(length, word);
    }

    public static String generateNumber(int length) {
        final String word = "1234567890";
        return generateRandomString(length, word);
    }

    private static String generateRandomString(int length, String word) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int location = random.nextInt(word.length());
            stringBuilder.append(word.charAt(location));
        }

        return stringBuilder.toString();
    }


    public static String getSuffix(String fileName) {
        String[] temp = fileName.split("\\.");
        return temp[temp.length - 1];
    }

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }

        if (!checkIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!checkIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!checkIp(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (!checkIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean checkIp(String ip) {
        if (ip == null || ip.length() == 0 || "unkown".equalsIgnoreCase(ip)) {
            return false;
        }
        return true;
    }

    public static String md5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            //convert the byte to hex format method 2
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                String hex = Integer.toHexString(0xff & byteData[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            ;
            return "";
        }
    }

    public static void logExceptionToFile(Logger logger, Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter, true));
        logger.warn(stringWriter.toString());
//        sendException(stringWriter.toString());
    }


    public static Date generateStartTime(String timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return sdf.parse(timeString + " 00:00:00");
        } catch (ParseException e) {
            //do nothing
        }

        return null;
    }

    public static Date generateEndTime(String timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return sdf.parse(timeString + " 23:59:59");
        } catch (ParseException e) {
            //do nothing
        }

        return null;
    }

    public static String dateConvertString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String[] splitStringToArrayWithSpace(String string) {
        String[] strings = string.split(" ");
        List<String> array = new ArrayList<>();

        for (String s : strings) {
            if (s != null && s.length() > 0) {
                array.add(s);
            }
        }

        String[] result = new String[array.size()];
        array.toArray(result);
        return result;
    }

    public static void checkNotNull(Object o, String errorMsg) throws BaseException {
        if (o == null) {
            throw new BaseException(errorMsg);
        } else if (o instanceof String) {
            if (o != null && ((String) o).length() == 0) {
                throw new BaseException(errorMsg);
            }
        }
    }

    public static void checkNotNull(Object o) {
        if (o == null) {
            throw new NotFoundException(o.getClass() + " is not Found.");
        }
    }

    public static String generateTranCode() {
        String result = String.valueOf(System.currentTimeMillis());
        result = result.substring(0, 10) + generateNumber(5);
        return result;
    }

    public static String encodeStr(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isEmpty(String[] strings) {
        if (strings == null) {
            return true;
        } else if (strings.length == 0) {
            return true;
        }

        return false;
    }

    public static boolean isNotEmpty(String[] strings) {
        return !isEmpty(strings);
    }

    public static boolean hasText(String string) {
        if (string == null) {
            return false;
        } else {
            string = string.trim();
            return string.length() > 0;
        }
    }

    public static boolean isPhone(String string) {
        String regEx = "^[1][0-9]{10}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}

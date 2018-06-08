package me.rubick.common.app.utils;

import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 字符串辅助类
 */
public class HashUtils {

    //随机生成字符串的基准
    private final static String CHAR_STRING = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    private final static String NUM_STRING = "1234567890";

    public static String base64Encode(String a) {
        return new String(Base64.getEncoder().encode(a.getBytes()));
    }

    /**
     * 生成 32 位随机字符串
     *
     * @return
     */
    public static String generateString() {
        return generateRandom(16);
    }

    /**
     * 生成指定长度的随机字符串
     *
     * @param length
     * @return
     */
    private static String generateRandom(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(CHAR_STRING.length());

            sb.append(CHAR_STRING.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 随机生成数字字符串
     * @param length
     * @return
     */
    public static String generateNumberString(final int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i ++) {
            int j = ThreadLocalRandom.current().nextInt(NUM_STRING.length());
            stringBuilder.append(NUM_STRING.charAt(j));
        }

        return stringBuilder.toString();
    }

}

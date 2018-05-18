package me.rubick.transport.app.utils;

import java.util.Base64;
import java.util.Random;

public class HashUtils {

    private final static String str = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

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
            int number = random.nextInt(str.length());

            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}

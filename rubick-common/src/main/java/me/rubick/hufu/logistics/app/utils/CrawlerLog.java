package me.rubick.hufu.logistics.app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬虫日志工具类
 */
public class CrawlerLog {

    private static List<String> logs = new ArrayList<String>();

    private static Logger logger = LoggerFactory.getLogger(CrawlerLog.class);

    /**
     * 把变量写到日志中1
     * @param string
     */
    public static void info(String string) {
        logs.add(string);
        logger.info(string);
    }

    /**
     * 把变量写到日志中2
     * @param string
     * @param objects
     */
    public static void info(String string, Object... objects) {
        logs.add(MessageFormat.format(formatString(string), objects));
        logger.info(string, objects);
    }

    /**
     * 格式化字符串
     * @param string
     * @return
     */
    private static String formatString(String string) {
        int times = 0;
        int index = 0;
        while (string.indexOf("{", index) != -1) {
            int leftAt = string.indexOf("{", index);
            if (leftAt + 1 < string.length() && string.charAt(leftAt + 1) == '}') {
                index = leftAt + 1;
                string = string.substring(0, leftAt + 1) + times++ + string.substring(leftAt + 1);
            }
        }
        return string;
    }

    /**
     * 获取日志列表
     *
     * @return
     */
    public static List<String> getLogs() {
        return logs;
    }

    /**
     * 清空日志
     */
    public static void clear() {
        logs.clear();
    }
}

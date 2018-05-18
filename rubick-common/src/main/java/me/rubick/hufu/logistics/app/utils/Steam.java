package me.rubick.hufu.logistics.app.utils;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件流辅助类
 */
public class Steam {

    static OutputStream outputStream = new ByteArrayOutputStream();


    private static InputStream parse(OutputStream out) throws Exception {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;

    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    private static List<String> readSteam2List(OutputStream outputStream) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(parse(outputStream)));

        String line;
        List<String> strings = new ArrayList<>();

        do {
            line = bufferedReader.readLine();
            if (!isEmpty(line)) {
                strings.add(line);
            }
        } while (!isEmpty(line));

        return strings;
    }

    public static List<String> getLog() throws Exception {
        return readSteam2List(outputStream);
    }

    public static void clear() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isEmpty(String s) {
        if (s != null && !s.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}

package app;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JMain {

    public static String generateBatch() {
        String batch = "CK" + "20180723" + "0101";


        int zIndex = batch.lastIndexOf("0");
        if (zIndex != batch.length() - 1) {
            zIndex += 1;
        }
        String temp = batch.substring(0, zIndex);

        System.out.println(temp);
        Integer no = Integer.valueOf(batch.substring(zIndex)) + 1;
        return temp.toString() + no.toString();
    }

    public static void main(String[] args) {
        System.out.println(generateBatch());
    }
}

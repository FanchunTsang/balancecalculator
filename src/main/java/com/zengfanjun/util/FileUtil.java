package com.zengfanjun.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FileUtil
 *
 * @author ZengFanjun
 * @version 1.0
 * 2022/5/23 下午 10:17
 **/
public class FileUtil {
    private FileUtil() {}

    private static final Logger logger = Logger.getLogger("FileUtil");

    public static Map<String, Double> initData(String fileString) {
        Map<String, Double> dataMap = new ConcurrentHashMap<>();
        List<String> dataList = Arrays.asList(fileString.split(","));
        if (!dataList.isEmpty()) {
            dataList.forEach(data -> {
                String[] payment = data.split(" ");
                if (payment.length == 2 && isNumeric(payment[1])) {
                    dataMap.put(payment[0], Double.parseDouble(payment[1]));
                }
            });
        }
        return dataMap;
    }

    public static String readFile(String path) {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            logger.warning("read file error: " + e);
//            e.printStackTrace();
            return null;
        }
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

}

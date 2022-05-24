package com.zengfanjun.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * HttpRequestUtil
 *
 * @author ZengFanjun
 * @version 1.0
 * 2022/5/23 下午 8:46
 **/
public class CurrencyUtil {
    private CurrencyUtil() {}
    private static final Logger logger = Logger.getLogger("HttpRequestUtil");
    private static final String APP_ID = "rtnvfpgghwx1h1pb";
    private static final String APP_SECRET = "NG12MHQybEh2YXl1R3RJT0xELzRsQT09";
    private static final String EXCHANGE_RATE_URL = "https://www.mxnzp.com/api/exchange_rate/aim";
    private static final String EXCHANGE_RATE_LIST = "https://www.mxnzp.com/api/exchange_rate/configs";
    private static final String REQUEST_CODE_SUCCESS = "1";

    public static Double getCurrency(String from, String to) {
        String url = EXCHANGE_RATE_URL + "?app_id=" + APP_ID + "&app_secret=" + APP_SECRET + "&from=" + from + "&to=" + to;
        String result = sendGet(url);
        if (!"".equals(result)) {
            JSONObject jsonObject = JSON.parseObject(result);
            if (null != jsonObject && REQUEST_CODE_SUCCESS.equals(jsonObject.getString("code"))
                    && null != jsonObject.getJSONObject("data")) {
                return jsonObject.getJSONObject("data").getDouble("price");
            }
        }
        return null;
    }

    public static List<String> getExchangeRateList() {
        List<String> list = new ArrayList<>(10);
        String result = sendGet(EXCHANGE_RATE_LIST + "?app_id=" + APP_ID + "&app_secret=" + APP_SECRET);
        if (!"".equals(result)) {
            JSONObject jsonObject = JSON.parseObject(result);
            if (REQUEST_CODE_SUCCESS.equals(jsonObject.getString("code"))) {
                JSONArray array = jsonObject.getJSONArray("data");
                if (null != array && !array.isEmpty()) {
                    List<RateDataVo> rates = JSONArray.parseArray(jsonObject.getString("data"), RateDataVo.class);
                    rates.forEach(rate -> list.add(rate.getName()));
                }
            }
        }
        return list;
    }

    /**
     * Send a request with the GET method to the specified URL
     *
     * @param url URL
     * @return response response
     */
    public static String sendGet(String url) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        // 使用finally块来关闭输入流
        try {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            // Set common request attributes
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            logger.warning("send GET request error: " + e);
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

}

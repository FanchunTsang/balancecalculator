package com.zengfanjun.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CurrencyUtilTest {

    @Test
    public void testGetCurrency() {
        assertEquals(null, CurrencyUtil.getCurrency("CYN", "USD"));
        assertEquals(0.15, CurrencyUtil.getCurrency("CNY", "USD"), 0.01);
    }

    @Test
    public void testGetExchangeRateList() {
        String[] rateList = {"CHF", "MXN", "SAR", "ZAR", "CNY", "THB", "AUD", "KRW", "PLN", "GBP", "HUF", "100JPY", "TRY", "RUB", "HKD", "AED", "EUR", "DKK", "USD", "CAD", "MYR", "NOK", "SGD", "SEK", "NZD"};
        assertEquals(Arrays.asList(rateList), CurrencyUtil.getExchangeRateList());
    }

    @Test
    public void testSendGet() {
        String baidu = "<html><head>\t<script>\t\tlocation.replace(location.href.replace(\"https://\",\"http://\"));\t</script></head><body>\t<noscript><meta http-equiv=\"refresh\" content=\"0;url=http://www.baidu.com/\"></noscript></body></html>";
        assertEquals(baidu, CurrencyUtil.sendGet("https://www.baidu.com"));
    }
}

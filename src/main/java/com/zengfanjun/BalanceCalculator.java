package com.zengfanjun;

import com.zengfanjun.util.CurrencyUtil;
import com.zengfanjun.util.FileUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * BalanceCalculator
 *
 * @author ZengFanjun
 * @version 1.0
 * 2022/5/23 下午 8:29
 **/
public class BalanceCalculator {
    private static final String START = "start";
    private static final String QUIT = "quit";
    private static final String YES = "yes";
    private static final String USD = "USD";
    private static Map<String, Double> payments;
    private static Timer timer = new Timer();
    private static List<String> currencyList = CurrencyUtil.getExchangeRateList();

    public static void main(String[] args) {
        while (true) {
            System.out.println("Please type [start] or [quit]");
            Scanner sc = new Scanner(System.in);
            String begin = sc.next();
            if (START.equals(begin)) {
                System.out.println("Do you have the initial file? type [yes] or [no]");
                String initFileAnswer = sc.next();
                if (YES.equals(initFileAnswer)) {
                    System.out.println("Please enter the file path: ");
                    String path = sc.next();
                    System.out.println(path);
                    String fileString = FileUtil.readFile(path);
                    if (null == fileString || "".equals(fileString)) {
                        System.out.println("Wrong file path or wrong format");
                        continue;
                    }
                    payments = FileUtil.initData(fileString);
                } else {
                    payments = new HashMap<>();
                }
                outputTask();
                while (true) {
                    System.out.println("Please enter a currency, an amount and followed by hitting the enter key");
                    System.out.println("Or you can type quit to exit");
                    String paymentIn = sc.nextLine();
                    if (QUIT.equals(paymentIn)) {
                        return;
                    }
                    if (!validPaymentFormat(paymentIn)) {
                        System.out.println("wrong format");
                        continue;
                    }
                    addPayment(paymentIn);
                }
            } else if (QUIT.equals(begin)) {
                return;
            }
        }
    }

    private static void outputTask() {
        timer.schedule(new TimerTask() {
            public void run() {
                if (null != payments && !payments.isEmpty()) {
                    System.out.println("---------------------- payments output begin ----------------------");
                    for (Map.Entry<String, Double> entry : payments.entrySet()) {
                        String mapKey = entry.getKey();
                        Double mapValue = entry.getValue();
                        if (0.0 != mapValue) {
                            String usdAmount = getUsdRate(mapKey, mapValue);
                            System.out.println(mapKey + "：" + mapValue + " " + usdAmount);
                        }
                    }
                    System.out.println("---------------------- payments output end ----------------------");
                }
            }
        }, 10000, 10000);
    }

    private static boolean validPaymentFormat(String input) {
        String[] payment = input.split(" ");
        if (payment.length != 2) {
            return false;
        }
        if (!FileUtil.isNumeric(payment[1]) ||
                (FileUtil.isNumeric(payment[1]) && 0 == Double.parseDouble(payment[1]))) {
            return false;
        }
        String currency = payment[0];
        return null == currencyList || currencyList.isEmpty() || currencyList.contains(currency);
    }

    private static void addPayment(String input) {
        String[] payment = input.split(" ");
        String currency = payment[0];
        double newAmount = Double.parseDouble(payment[1]);
        double oldAmount = null == payments.get(currency) ? 0.0 : payments.get(currency);
        BigDecimal bdNew = BigDecimal.valueOf(newAmount);
        BigDecimal bdOld = BigDecimal.valueOf(oldAmount);
        Double amount = bdNew.add(bdOld).setScale(2, RoundingMode.HALF_UP).doubleValue();
        payments.put(payment[0], amount);
    }

    private static String getUsdRate(String from, Double amount) {
        Double result = CurrencyUtil.getCurrency(from, USD);
        if (null != result) {
            BigDecimal bdResult = BigDecimal.valueOf(result);
            BigDecimal bdAmount = BigDecimal.valueOf(amount);
            String usdAmount = bdAmount.multiply(bdResult).setScale(2, RoundingMode.HALF_UP).toString();
            return "(USD " + usdAmount + ")";
        }
        return "";
    }
}

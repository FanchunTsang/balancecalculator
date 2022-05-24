package com.zengfanjun;

import com.zengfanjun.util.CurrencyUtil;
import com.zengfanjun.util.FileUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    private static final Timer timer = new Timer();
    private static final List<String> currencyList = CurrencyUtil.getExchangeRateList();

    public static void main(String[] args) {
        while (true) {
            print("Please type [start] or [quit]");
            Scanner sc1 = new Scanner(System.in);
            String begin = sc1.next();
            if (START.equals(begin)) {
                print("Do you have the initial file? type [yes] or [no]");
                String initFileAnswer = sc1.next();
                if (YES.equals(initFileAnswer)) {
                    print("Please enter the file path: ");
                    String path = sc1.next();
                    print(path);
                    String fileString = FileUtil.readFile(path);
                    if (null == fileString || "".equals(fileString)) {
                        print("Wrong file path or wrong format");
                        continue;
                    }
                    payments = FileUtil.initData(fileString);
                } else {
                    payments = new ConcurrentHashMap<>();
                }
                outputTask();
                while (true) {
                    print("Please enter a currency, an amount and followed by hitting the enter key");
                    print("Or you can type quit to exit");
                    Scanner sc2 = new Scanner(System.in);
                    String paymentIn = sc2.nextLine();
                    if (QUIT.equals(paymentIn)) {
                        timer.cancel();
                        return;
                    }
                    if (!validPaymentFormat(paymentIn)) {
                        print("wrong format");
                        continue;
                    }
                    addPayment(paymentIn);
                    print("payment received");
                }
            } else if (QUIT.equals(begin)) {
                timer.cancel();
                return;
            }
        }
    }

    private static void outputTask() {
        timer.schedule(new TimerTask() {
            public void run() {
                printPayments();
            }
        }, 10000, 60000);
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
        return currencyList.isEmpty() || currencyList.contains(currency);
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
        if (!USD.equals(from)) {
            Double result = CurrencyUtil.getCurrency(from, USD);
            if (null != result) {
                BigDecimal bdResult = BigDecimal.valueOf(result);
                BigDecimal bdAmount = BigDecimal.valueOf(amount);
                String usdAmount = bdAmount.multiply(bdResult).setScale(2, RoundingMode.HALF_UP).toString();
                return "(USD " + usdAmount + ")";
            }
        }
        return "";
    }

    private static void printPayments() {
        if (null != payments && !payments.isEmpty()) {
            print("---------------------- payments output begin ----------------------");
            for (Map.Entry<String, Double> entry : payments.entrySet()) {
                String mapKey = entry.getKey();
                Double mapValue = entry.getValue();
                if (0.0 != mapValue) {
                    String usdAmount = getUsdRate(mapKey, mapValue);
                    print(mapKey + "：" + mapValue + " " + usdAmount);
                }
            }
            print("---------------------- payments output end ----------------------");
        }
    }

    private static void print(String msg) {
        System.out.println(msg);
        System.out.print("\n");
    }
}

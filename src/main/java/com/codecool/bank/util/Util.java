package com.codecool.bank.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public final class Util {

    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{4}-\\d{4}-\\d{4}-\\d{4}$");
    private static final Pattern CVV_PATTERN = Pattern.compile("^\\d{3}$");

    private Util() {
    }

    public static boolean isValidCardNumber(String cardNumber) {
        return CARD_NUMBER_PATTERN.matcher(cardNumber).matches();
    }

    public static boolean isValidCvv(String cvv) {
        return CVV_PATTERN.matcher(cvv).matches();
    }

    public static boolean isValidValidThru(String validThru) {
        try {
            new SimpleDateFormat("MM/YY").parse(validThru);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isNulLOrEmpty(String string) {
        return string == null || "".equals(string);
    }
}

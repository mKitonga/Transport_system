package com.transport.liby.service;

import org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Service
@Slf4j
public class FormatUtil {
    public final static int BIG_DECIMAL_SCALE = 20;
    public final static int BIG_DECIMAL_PRECISION = 30;
    public final static MathContext ROUND_DOWN_MATH_CONTEXT = new MathContext(
            BIG_DECIMAL_PRECISION,
            RoundingMode.DOWN
    );

    public final static MathContext ROUND_UP_MATH_CONTEXT = new MathContext(
            BIG_DECIMAL_PRECISION,
            RoundingMode.UP
    );

    public final static String AFRICA_NAIROBI_ZONE = "Africa/Nairobi";

    public final static DateTimeFormatter ISO_DATE_FORMAT =  DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public final static DateTimeFormatter ISO_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");


    public static String formatAmount(BigDecimal amount) {
        final NumberFormat NUM_FORMAT_WITH_COMMA_SEP_2DP = new DecimalFormat("#,###.00");
        return NUM_FORMAT_WITH_COMMA_SEP_2DP.format(amount);
    }

    /**
     * format currency based on symbol
     */
    public static String formatAmount(BigDecimal amount, String currencyCode) {
        return String.format("%s %s", currencyCode, formatAmount(amount));
    }

    public static String getHumanReadableDateTime(LocalDateTime date){
        return DateTimeFormatter.ofPattern("E dd MMM yyyy, hh:mm a").format(date);
    }

    public static String toPercentage(Object number){
        final DecimalFormat PERCENTAGE_FORMAT = new DecimalFormat("#.00%");
        return PERCENTAGE_FORMAT.format(number);
    }

    @Nullable
    public static String internationalizePhoneNumber(String msisdn, String region){
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(msisdn, region);
            String formattedPhoneNumber = phoneUtil.format(phoneNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            return formattedPhoneNumber.replaceAll("\\s+", "");
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    public static String getFormattedDuration(Integer days) {
        if(days == null){
            return "NA";
        }

        if(days <= 0){
            return "0 d";
        }

        String output = "";
        int years = days / 365;
        if (years >= 1) {
            days -= years * 365;
        }
        int months = days / 30;

        if (months >= 1) {
            days -= months * 30;
        }

        int weeks = days / 7;
        if (weeks >= 1) {
            days -= weeks * 7;
        }

        if (years >= 1) {
            output += String.format(Locale.ENGLISH, "%d yr", years);
        }
        if (months >= 1 && months < 12) {
            output += " " + String.format(Locale.ENGLISH, "%d m", months);
        }
        if (weeks >= 1 && weeks < 4) {
            output += " " + String.format(Locale.ENGLISH, "%d wk", weeks);
        }
        if (days >= 1 && days < 7) {
            output += " " + String.format(Locale.ENGLISH, "%d d", days);
        }

        if(output.isBlank()){
            output = "0 d";
        }
        return output.trim();
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return email;
        }

        String localPart = email.substring(0, atIndex);
        if (localPart.length() <= 2) {
            return email; // Not enough characters to mask meaningfully
        }

        char firstChar = localPart.charAt(0);
        char lastChar = localPart.charAt(localPart.length() - 1);

        String maskedPart = StringUtils.repeat("*", 3);

        String maskedLocalPart = firstChar + maskedPart + lastChar;

        return maskedLocalPart + email.substring(atIndex);
    }

    public static int calculateNights(LocalDate startDate, LocalDate endDate) {
        long nights = ChronoUnit.DAYS.between(startDate, endDate);
        return (int) Math.max(nights, 1);
    }
}

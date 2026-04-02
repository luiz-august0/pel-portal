package com.almeja.pel.gestao.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class for numeric operations and conversions
 */
public class NumericUtil {

    /**
     * Converts a price value to its written form in Portuguese
     *
     * @param value the price value to convert
     * @return the written form of the price (e.g., "um real e cinquenta centavos")
     */
    public static String convertPriceToWords(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "Zero reais";
        }

        int reais = value.intValue();
        int centavos = value.remainder(BigDecimal.ONE).multiply(new BigDecimal("100")).intValue();

        String reaisInWords = convertNumberToWords(reais);
        String centavosInWords = centavos > 0 ? convertNumberToWords(centavos) : "";

        StringBuilder result = new StringBuilder(reaisInWords + " reais");
        if (centavos > 0) {
            result.append(" e ").append(centavosInWords).append(" centavos");
        }

        return result.toString();
    }

    /**
     * Converts a number to its written form in Portuguese
     *
     * @param value the number to convert
     * @return the written form of the number (e.g., "vinte e três")
     */
    public static String convertNumberToWords(Integer value) {
        if (value == 0) {
            return "zero";
        }

        String[] units = {"", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove"};
        String[] tens = {"", "dez", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"};
        String[] teens = {"", "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"};
        String[] hundreds = {"", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"};

        StringBuilder wordsResult = new StringBuilder();

        if (value >= 100) {
            int hundred = value / 100;
            wordsResult.append(hundreds[hundred]);
            value %= 100;
            if (value > 0) {
                wordsResult.append(" e ");
            }
        }

        if (value >= 10 && value < 20) {
            wordsResult.append(teens[value - 10]);
        } else {
            int ten = value / 10;
            if (ten > 0) {
                wordsResult.append(tens[ten]);
                value %= 10;
                if (value > 0) {
                    wordsResult.append(" e ");
                }
            }

            if (value > 0) {
                wordsResult.append(units[value]);
            }
        }

        return wordsResult.toString();
    }

    public static String format(BigDecimal value) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Locale.of("pt", "BR")));
        return formatter.format(value);
    }

}

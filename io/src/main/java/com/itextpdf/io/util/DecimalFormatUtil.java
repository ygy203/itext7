package com.itextpdf.io.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class DecimalFormatUtil {

    private static final DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);

    private DecimalFormatUtil() {
    }

    public static String formatNumber(double d, String pattern) {
        DecimalFormat dn = new DecimalFormat(pattern, dfs);
        return dn.format(d);
    }
}
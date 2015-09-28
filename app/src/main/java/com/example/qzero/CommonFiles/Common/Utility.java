package com.example.qzero.CommonFiles.Common;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Braintech on 8/17/2015.
 */
public class Utility {

    // Formats the string to US currency ($0.00) format
    public static String formatCurrency(String amount) {
        double doubleAmount = Double.valueOf(amount);
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        String s = n.format(doubleAmount);
        return s;
    }

    public static String formatDecimalByString(String dub) {

        Double data = Double.parseDouble(dub);
        String myNum = String.format("%.2f", data);
        return myNum;
    }
}

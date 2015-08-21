package com.example.qzero.CommonFiles.Common;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Braintech on 8/17/2015.
 */
public class Utility {


    public static String formateCurrency(String amount)
    {
        double doubleAmount = Double.valueOf(amount);
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        String s = n.format(doubleAmount);
        return s;
    }
}

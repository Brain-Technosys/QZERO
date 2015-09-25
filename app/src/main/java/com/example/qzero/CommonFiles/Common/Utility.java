package com.example.qzero.CommonFiles.Common;

import java.text.DecimalFormat;
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

    public static Double formatDecimal(double num){
        DecimalFormat df = new DecimalFormat("#.00");
        String numberFormated = df.format(num);
        Double myNumber=Double.parseDouble(numberFormated);
        return myNumber;
    }

    public static String formatDecimalByString(String dub) {

        Double data=Double.parseDouble(dub);

        String myNum=String.format("%.2f", data);

        return myNum;
    }
}

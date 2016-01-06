package com.example.qzero.CommonFiles.Common;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Braintech on 8/17/2015.
 */
public class Utility {

    // Formats the string to US currency ($0.00) format
    public static String formatCurrency(String amount) {

        String s = null;
        try {


            double doubleAmount = Double.valueOf(amount);
            NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
            s = n.format(doubleAmount);

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return s;
    }

    public static String formatDecimalByString(String dub) {
        String myNum = null;
        try {
            Double data = Double.parseDouble(dub);
            myNum = String.format("%.2f", data);

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return myNum;
    }

    public static String removeCurrencySymbol(String text) {
        int priceLen = text.length();

        String substrPrice = text.substring(1, priceLen);

        return substrPrice;
    }

    // Method to hide soft keyboard
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

package com.example.qzero.CommonFiles.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class GetCartCountHelper {


    public static int getTotalQty(Context context) {
        int quantity = 0;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor modCountcursor = databaseHelper.selectDistinctMod();

        if (modCountcursor != null) {
            while (modCountcursor.moveToNext()) {
                String mod_id = modCountcursor.getString(0);

                Log.e("modid", mod_id);
                Cursor modQtyCursor = databaseHelper.getModifiersQty(mod_id);
                if (modQtyCursor != null) {
                    if (modQtyCursor.moveToFirst()) {
                        String qty = modQtyCursor.getString(0);

                        Log.e("qty", qty);

                        quantity = quantity + Integer.parseInt(qty);

                        Log.e("quantity", "" + quantity);
                    }
                }

            }
        }

        return quantity;
    }
}

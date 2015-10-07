package com.example.qzero.CommonFiles.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Braintech on 10/7/2015.
 */
public class DatabaseHandler {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;


    public static final String ITEM_TABLE = "itemDetails";
    public static final String MODIFIER_TABLE = "modifiers";

    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "item_name";
    public static final String ITEM_PRICE = "item_price";
    public static final String ITEM_DISCOUNT= "item_discount";

    public static final String MODIFIER_ID= "item_id";
    public static final String QUANTITY= "quantity";

    //methods for all table

    public DatabaseHandler(Context context) {

        dbHelper = new DatabaseHelper(context);

    }

    public void open() throws SQLException {
        Log.i("inside dbhandler","yes1");

    }

    public void close() {
        dbHelper.close();
    }



}

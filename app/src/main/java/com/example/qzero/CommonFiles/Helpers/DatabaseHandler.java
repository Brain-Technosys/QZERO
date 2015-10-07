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
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public void insertIntoItem(String item_name,String item_price,String discount_price)
    {
        ContentValues cv = new ContentValues ();
        cv.put (NAME_COLUMN,item_name);
        cv.put (ITEM_PRICE,item_price);
        cv.put(ITEM_DISCOUNT, discount_price);
        database.insert(ITEM_TABLE, null, cv);


    }

    public void insertIntoModifiers(String item_name,String item_price,String quantity)
    {
        ContentValues cv = new ContentValues ();
        cv.put(NAME_COLUMN, item_name);
        cv.put (ITEM_PRICE,item_price);
        cv.put(QUANTITY, quantity);
        database.insert(MODIFIER_TABLE, null, cv);

    }

}

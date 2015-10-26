package com.example.qzero.CommonFiles.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Braintech on 10/6/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "qzeronew1";
    private static final int DATABASE_VERSION = 1;

    public static final String ITEM_TABLE = "itemDetails";
    public static final String MODIFIER_TABLE = "modifiers";
    public static final String CHECKOUT_ITEM = "Checkoutitem";

    public static final String ID_COLUMN = "id";

    public static final String ITEM_CODE = "item_code";
    public static final String NAME_COLUMN = "item_name";
    public static final String ITEM_PRICE = "item_price";
    public static final String ITEM_DISCOUNT = "item_discount";
    public static final String ITEM_IMAGE = "item_image";
    public static final String COUNT = "item_count";

   //Checkout table variables
    public static final String OUTLET_ID = "outlet_id";
    public static final String VENUE_ID = "venue_id";
    public static final String ITEM_ID = "item_id";
    public static final String DISC_AMT = "discount_amt";
    public static final String AFTER_DISC = "after_discount";

    public static final String MODIFIER_ID = "mod_id";
    public static final String MOD_ACTUAL_ID = "actual_id";
    public static final String QUANTITY = "quantity";
    public static final String MOD_COLUMN = "mod_name";
    public static final String MOD_PRICE = "mod_price";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + MODIFIER_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT ," + MODIFIER_ID + " TEXT ," +
                MOD_COLUMN + " TEXT ," + NAME_COLUMN + " TEXT ," + MOD_PRICE + " TEXT ," + QUANTITY + " TEXT ," + MOD_ACTUAL_ID + " TEXT ," + " FOREIGN KEY(" + MODIFIER_ID + ") REFERENCES "
                + ITEM_TABLE + "(" + ID_COLUMN + "));");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ITEM_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                NAME_COLUMN + " TEXT ," + ITEM_PRICE + " TEXT ," + ITEM_IMAGE + " TEXT ," + ITEM_DISCOUNT + " TEXT ,"+ ITEM_CODE + " TEXT );");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + CHECKOUT_ITEM + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                ITEM_ID + " TEXT ," + OUTLET_ID + " TEXT ," + DISC_AMT + " TEXT ," + AFTER_DISC + " TEXT ," + COUNT + " TEXT ," + VENUE_ID + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertIntoCheckout(String item_id, String outlet_id, String item_count,String discount_amt,String after_disc,String venue_id) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_ID, item_id);
        cv.put(OUTLET_ID, outlet_id);
        cv.put(COUNT, item_count);
        cv.put(DISC_AMT,discount_amt);
        cv.put(AFTER_DISC,after_disc);
        cv.put(VENUE_ID,venue_id);
        database.insert(CHECKOUT_ITEM, null, cv);
    }


    public long insertIntoItem(String itemCode,String item_name, String item_price, String discount_price, String item_image) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME_COLUMN, item_name);
        cv.put(ITEM_CODE, itemCode);
        cv.put(ITEM_PRICE, item_price);
        cv.put(ITEM_DISCOUNT, discount_price);
        cv.put(ITEM_IMAGE, item_image);
        long value = database.insert(ITEM_TABLE, null, cv);

        return value;
    }

    public void insertIntoModifiers(String modId, String mod_name, String item_price, String quantity, String item_id, String item_name) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MOD_COLUMN, mod_name);
        cv.put(NAME_COLUMN, item_name);
        cv.put(MOD_PRICE, item_price);
        cv.put(QUANTITY, quantity);
        cv.put(MODIFIER_ID, item_id);
        cv.put(MOD_ACTUAL_ID, modId);
        database.insert(MODIFIER_TABLE, null, cv);

    }

    public Cursor getItems(String item_id) {
        SQLiteDatabase database = getReadableDatabase();
        String selectItems = "select * from " + ITEM_TABLE + " where " + ID_COLUMN + " = '" + item_id + "'";
        Cursor valueItems = database.rawQuery(selectItems, null);
        return valueItems;
    }

    public Cursor getCheckoutItems() {
        SQLiteDatabase database = getReadableDatabase();
        String selectItems = "select * from " + CHECKOUT_ITEM;
        Cursor valueItems = database.rawQuery(selectItems, null);
        return valueItems;
    }

    public Cursor getAllModifiers() {
        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select * from " + MODIFIER_TABLE;
        Cursor valueItems = database.rawQuery(selectMod, null);
        return valueItems;
    }

    public Cursor getModItems() {
        SQLiteDatabase database = getReadableDatabase();
        String selectItems = "select * from " + ITEM_TABLE;
        Cursor valueItems = database.rawQuery(selectItems, null);
        return valueItems;
    }


    public Cursor getDistinctItems() {
        SQLiteDatabase database = getReadableDatabase();
        String selectItems = "select DISTINCT " + NAME_COLUMN + " from " + ITEM_TABLE;
        Cursor valueItems = database.rawQuery(selectItems, null);
        return valueItems;
    }


    public Cursor selectDistinctMod() {
        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select DISTINCT " + MODIFIER_ID + " from " + MODIFIER_TABLE;
        Cursor valueMod = database.rawQuery(selectMod, null);

        return valueMod;
    }


    public Cursor getModifiers(String item_id) {

        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select * from " + MODIFIER_TABLE + " where " + MODIFIER_ID + " = '" + item_id + "'";
        Log.e("command", selectMod);
        Cursor valueMod = database.rawQuery(selectMod, null);
        return valueMod;
    }

    public Cursor getModifiersQty(String item_id) {

        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select " + QUANTITY + " from " + MODIFIER_TABLE + " where " + MODIFIER_ID + " = '" + item_id + "'";

        Cursor valueMod = database.rawQuery(selectMod, null);
        return valueMod;
    }

    public Cursor selectItems(String item_name) {
        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select * from " + ITEM_TABLE + " where " + NAME_COLUMN + " = '" + item_name + "'";
        Cursor valueItem = database.rawQuery(selectMod, null);
        return valueItem;
    }

    public Cursor selectOutletId() {
        SQLiteDatabase database = getReadableDatabase();
        String selectOutletId = "select " + OUTLET_ID + " from " + CHECKOUT_ITEM ;
        Cursor valueOutletId = database.rawQuery(selectOutletId, null);
        return valueOutletId;
    }

    public Cursor getNullModifiers(String item_name, String mod_name) {

        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select * from " + MODIFIER_TABLE + " where " + NAME_COLUMN + " = '" + item_name + "' and " + MOD_COLUMN + "='" + mod_name + "'";
        Cursor valueMod = database.rawQuery(selectMod, null);
        return valueMod;
    }

    public void updateModifiers(String item_id, String quantity) {

        Log.e("update", item_id);

        SQLiteDatabase database = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(QUANTITY, quantity);
        database.update(MODIFIER_TABLE, cv, MODIFIER_ID + " =? ", new String[]{item_id});
    }

    public void updateNullModifiers(String item_name, String mod_name, String quantity) {

        SQLiteDatabase database = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(QUANTITY, quantity);
        database.update(MODIFIER_TABLE, cv, NAME_COLUMN + " =? and " + MOD_COLUMN + " = ? ", new String[]{item_name, mod_name});
    }

    public void deleteValuesItem(String item_id) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(MODIFIER_TABLE, new String(MODIFIER_ID + " =? "), new String[]{item_id});
        database.delete(ITEM_TABLE, new String(ID_COLUMN + " =? "), new String[]{item_id});
    }

    public void deleteItemTable() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(ITEM_TABLE, null, null);
    }

    public void deleteModifierTable() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(MODIFIER_TABLE, null, null);
    }

    public void deleteCheckOutTable() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(CHECKOUT_ITEM, null, null);
    }


}

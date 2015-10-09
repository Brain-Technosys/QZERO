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

    private static final String DATABASE_NAME = "qzerodb13";
    private static final int DATABASE_VERSION = 1;

    public static final String ITEM_TABLE = "itemDetails";
    public static final String MODIFIER_TABLE = "modifiers";

    public static final String ID_COLUMN = "id";

    public static final String NAME_COLUMN = "item_name";
    public static final String ITEM_PRICE = "item_price";
    public static final String ITEM_DISCOUNT = "item_discount";
    public static final String ITEM_IMAGE = "item_image";

    public static final String MODIFIER_ID = "mod_id";
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

        db.execSQL("CREATE TABLE IF NOT EXISTS " + MODIFIER_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT ," + MODIFIER_ID + " INTEGER ," +
                MOD_COLUMN + " TEXT ," + NAME_COLUMN + " TEXT ," + MOD_PRICE + " TEXT ," + QUANTITY + " TEXT ," + " FOREIGN KEY(" + MODIFIER_ID + ") REFERENCES "
                + ITEM_TABLE + "(" + ID_COLUMN + "));");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + ITEM_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                NAME_COLUMN + " TEXT ," + ITEM_PRICE + " TEXT ," + ITEM_IMAGE + " TEXT ," + ITEM_DISCOUNT + " TEXT );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertIntoItem(String item_name, String item_price, String discount_price, String item_image) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME_COLUMN, item_name);
        cv.put(ITEM_PRICE, item_price);
        cv.put(ITEM_DISCOUNT, discount_price);
        cv.put(ITEM_IMAGE, item_image);
        long value = database.insert(ITEM_TABLE, null, cv);

        return value;
    }

    public void insertIntoModifiers(String mod_name, String item_price, String quantity, String item_id, String item_name) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MOD_COLUMN, mod_name);
        cv.put(NAME_COLUMN, item_name);
        cv.put(MOD_PRICE, item_price);
        cv.put(QUANTITY, quantity);
        cv.put(MODIFIER_ID, item_id);
        database.insert(MODIFIER_TABLE, null, cv);

    }

    public Cursor getItems(String item_id) {
        SQLiteDatabase database = getReadableDatabase();
        String selectItems = "select * from " + ITEM_TABLE + " where " + ID_COLUMN + " = '" + item_id + "'";
        ;
        Cursor valueItems = database.rawQuery(selectItems, null);
        return valueItems;
    }

    public Cursor getModItems() {
        SQLiteDatabase database = getReadableDatabase();
        String selectItems = "select * from " + ITEM_TABLE ;
        Cursor valueItems = database.rawQuery(selectItems, null);
        return valueItems;
    }


    public Cursor getDistinctItems() {
        SQLiteDatabase database = getReadableDatabase();
        String selectItems = "select DISTINCT " + NAME_COLUMN + " from " + ITEM_TABLE;
        Cursor valueItems = database.rawQuery(selectItems, null);
        return valueItems;
    }

    public int selectDistinctMod() {
        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select DISTINCT " + ID_COLUMN + " from " + MODIFIER_TABLE;
        Cursor valueMod = database.rawQuery(selectMod, null);

        int length = valueMod.getCount();
        return length;
    }


    public Cursor getModifiers(String item_id) {

        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select * from " + MODIFIER_TABLE + " where " + MODIFIER_ID + " = '" + item_id + "'";
        Cursor valueMod = database.rawQuery(selectMod, null);
        return valueMod;
    }

    public Cursor selectItems(String item_name) {
        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select " + ID_COLUMN + " from " + ITEM_TABLE + " where " + NAME_COLUMN + " = '" + item_name + "'";
        Cursor valueItem = database.rawQuery(selectMod, null);
        return valueItem;
    }

    public Cursor selectDistinctItems(String item_name) {
        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select " + ID_COLUMN + " from " + ITEM_TABLE + " where " + NAME_COLUMN + " = '" + item_name + "'";
        Cursor valueItem = database.rawQuery(selectMod, null);
        return valueItem;
    }

    public int getNullModifiers(String item_name, String mod_name) {

        SQLiteDatabase database = getReadableDatabase();
        String selectMod = "select * from " + MODIFIER_TABLE + " where " + NAME_COLUMN + " = '" + item_name + "' and " + MOD_COLUMN + "='" + mod_name + "'";
        Cursor valueMod = database.rawQuery(selectMod, null);
        int len = valueMod.getCount();
        return len;
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


}
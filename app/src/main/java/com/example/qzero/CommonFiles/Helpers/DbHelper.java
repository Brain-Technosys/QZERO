package com.example.qzero.CommonFiles.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Braintech on 9/29/2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    Context context;
    SQLiteDatabase database;

    private static final String DATABASE_NAME = "qzero_db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

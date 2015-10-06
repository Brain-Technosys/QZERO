package com.example.qzero.CommonFiles.Helpers;


import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qzero.Outlet.DatabseTable.ItemDetails;
import com.example.qzero.Outlet.DatabseTable.ModifierDetails;
import com.example.qzero.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 *
 *
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "qzeodemo.db";
    private static final int DATABASE_VERSION = 1;


    private Dao<ItemDetails, Integer> itemsDao;
    private Dao<ModifierDetails, Integer> modifiersDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource,ItemDetails.class);
            TableUtils.createTable(connectionSource,ModifierDetails.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, ItemDetails.class, true);
            TableUtils.dropTable(connectionSource, ModifierDetails.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<ItemDetails,Integer> getItemDao() throws SQLException {
        if (itemsDao == null) {

            itemsDao = getDao(ItemDetails.class);
        }
        return itemsDao;
    }

    public Dao<ModifierDetails, Integer> getMofifierDao() throws SQLException {
        if (modifiersDao == null) {
            modifiersDao = getDao(ModifierDetails.class);
        }
        return modifiersDao;
    }
}

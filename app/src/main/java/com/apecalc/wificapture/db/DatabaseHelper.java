package com.apecalc.wificapture.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.apecalc.wificapture.models.Capture;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String databaseName = "wifi_captures.db";
    private static final int databaseVersion = 4;

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Capture.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            TableUtils.dropTable(connectionSource, Capture.class, true);
            onCreate(sqLiteDatabase, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void close() {
        super.close();
    }
}

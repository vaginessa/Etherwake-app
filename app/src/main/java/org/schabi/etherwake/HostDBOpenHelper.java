package org.schabi.etherwake;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Christian Schabesberger on 26.07.15.
 *
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * HostDBOpenHelper.java is part of Etherwake App.
 *
 * Etherwake App is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Etherwake App is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Etherwake App.  If not, see <http://www.gnu.org/licenses/>.
 */

public class HostDBOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = HostDBOpenHelper.class.getSimpleName();

    //db
    public static final String DB_NAME = "etherHosts.db";
    public static final int DB_VERSION = 1;

    //keys
    public static final String _ID = "_id";
    public static final String HOST = "host";
    public static final String MAC = "mac";
    public static final String PWD = "pwd";
    public static final String BROADCAST = "broadcast";

    //table
    public static final String HOSTS_TABEL = "hosts";
    private static final String TABLE_HOSTS_CREATE = "CREATE TABLE " + HOSTS_TABEL
            + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HOST + " TEXT, " + MAC + " TEXT, " + PWD + " TEXT, " + BROADCAST + " INTEGER);";
    private static final String TABLE_HOSTS_DROP = "DROP TABLE IF EXISTS " + HOSTS_TABEL + ";";

    HostDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_HOSTS_CREATE);
        Log.v(TAG, TABLE_HOSTS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_HOSTS_DROP);
        onCreate(db);
    }

    public void insert(String host, String mac, String password, boolean broadcast) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(HOST, host);
            values.put(MAC, mac);
            values.put(PWD, password);
            if(broadcast)
                values.put(BROADCAST, 1);
            else
                values.put(BROADCAST, 0);
            db.insert(HOSTS_TABEL, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cursor query() {
        SQLiteDatabase db = getWritableDatabase();
        return db.query(HOSTS_TABEL,
                null, null, null,
                null, null, null,
                null);
    }

    public void update(long id, String host, String mac, String pwd, boolean broadcast) {
        ContentValues newValues = new ContentValues();
        newValues.put(HOST, host);
        newValues.put(MAC, mac);
        newValues.put(PWD, pwd);
        newValues.put(BROADCAST, broadcast);
        getWritableDatabase().update(HOSTS_TABEL, newValues, "_id = ?", new String[]{Long.toString(id)});
    }

    public String getStringValueOf(long id, String col) {
        Cursor cursor = getWritableDatabase()
                .query(HOSTS_TABEL, new String[]{col}, "_id = ?", new String[]{Long.toString(id)},
                        null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public boolean getBoolValueOf(long id, String col) {
        Cursor cursor = getWritableDatabase()
                .query(HOSTS_TABEL, new String[]{col}, "_id = ?", new String[]{Long.toString(id)},
                        null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(0) != 0;
    }

    public void delete(long id) {
        getWritableDatabase()
                .delete(HOSTS_TABEL, "_id = ?", new String[]{Long.toString(id)});
    }
}

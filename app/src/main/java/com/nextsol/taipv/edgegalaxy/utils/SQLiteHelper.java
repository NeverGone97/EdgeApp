package com.nextsol.taipv.edgegalaxy.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nextsol.taipv.edgegalaxy.model.ListApp;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ListApps_db";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create ListApps table
        db.execSQL(ListApp.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ListApp.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertListApp(int id,String listApp,byte[]image) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(ListApp.COLUMN_ID,id);
        values.put(ListApp.COLUMN_NAME, listApp);
        values.put(ListApp.COLUMN_IMAGE,image);
        // insert row
        long listInsert = db.insert(ListApp.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return listInsert;
    }

    public ListApp getListApp(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ListApp.TABLE_NAME,
                new String[]{ListApp.COLUMN_ID, ListApp.COLUMN_NAME, ListApp.COLUMN_IMAGE},
                ListApp.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare ListApp object
        ListApp listApp = new ListApp(
                cursor.getInt(cursor.getColumnIndex(ListApp.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ListApp.COLUMN_NAME)),
                cursor.getBlob(cursor.getColumnIndex(ListApp.COLUMN_IMAGE)));

        // close the db connection
        cursor.close();

        return listApp;
    }

    public List<ListApp> getAllListApps() {
        List<ListApp> ListApps = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + ListApp.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ListApp listApp = new ListApp();
                listApp.setId(cursor.getInt(cursor.getColumnIndex(ListApp.COLUMN_ID)));
                listApp.setNameApp(cursor.getString(cursor.getColumnIndex(ListApp.COLUMN_NAME)));
                listApp.setImageApp(cursor.getBlob(cursor.getColumnIndex(ListApp.COLUMN_IMAGE)));

                ListApps.add(listApp);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return ListApps list
        return ListApps;
    }

    public int getListAppsCount() {
        String countQuery = "SELECT  * FROM " + ListApp.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateListApp(ListApp ListApp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ListApp.COLUMN_NAME, ListApp.getNameApp());

        // updating row
        return db.update(ListApp.TABLE_NAME, values, ListApp.COLUMN_ID + " = ?",
                new String[]{String.valueOf(ListApp.getId())});
    }

    public void deleteListApp(ListApp ListApp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ListApp.TABLE_NAME, ListApp.COLUMN_ID + " = ?",
                new String[]{String.valueOf(ListApp.getId())});
        db.close();
    }}

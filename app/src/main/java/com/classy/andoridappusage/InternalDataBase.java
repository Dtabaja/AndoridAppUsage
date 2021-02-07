package com.classy.andoridappusage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class InternalDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AndroidAppUsage.db";
    private static final String TABLE_NAME = "TRACKED_APPS";
    private static final String PACKAGE_NAME = "PACKAGE_NAME";
    private static final String TIME_ALLOWED = "TIME_ALLOWED";
    private static final String IS_USAGE_EXCEEDED = "IS_USAGE_EXCEEDED";
    private static final int VERSION = 1;

   public InternalDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(PACKAGE_NAME TEXT PRIMARY KEY," +
                "TIME_ALLOWED INTEGER, IS_USAGE_EXCEEDED INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void insert(String packageName, int timeAllowed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PACKAGE_NAME, packageName);
        contentValues.put(TIME_ALLOWED, timeAllowed);
        contentValues.put(IS_USAGE_EXCEEDED, 0);
        long i = db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    void delete(String packageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PACKAGE_NAME + " = ?", new String[]{packageName});
        db.close();
    }

    TrackedInfo getRow(String packageName) {
        TrackedInfo trackedInfo = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "
                +TABLE_NAME+ " WHERE " + PACKAGE_NAME + " = ?", new String[]{packageName});
        if(cursor.moveToFirst()) {
            int timeAllowed = cursor.getInt(cursor.getColumnIndex(TIME_ALLOWED));
            int isUsageExceeded = cursor.getInt(cursor.getColumnIndex(IS_USAGE_EXCEEDED));
            trackedInfo = new TrackedInfo(packageName, timeAllowed, isUsageExceeded);
        }
        cursor.close();
        db.close();
        return trackedInfo;
    }

    List<TrackedInfo> getAllRows() {
        List<TrackedInfo> trackedAppInfos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NAME, null);
        while(cursor.moveToNext()) {
            String packageName = cursor.getString(cursor.getColumnIndex(PACKAGE_NAME));
            int timeAllowed = cursor.getInt(cursor.getColumnIndex(TIME_ALLOWED));
            int isUsageExceeded = cursor.getInt(cursor.getColumnIndex(IS_USAGE_EXCEEDED));
            trackedAppInfos.add(new TrackedInfo(packageName, timeAllowed, isUsageExceeded));
        }
        cursor.close();
        db.close();
        return trackedAppInfos;
    }

    void setTimeAllowed(String packageName, int timeAllowed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME_ALLOWED, timeAllowed);
        db.update(TABLE_NAME, contentValues, PACKAGE_NAME + " = ?", new String[]{packageName});
        db.close();
    }

    void resetAllIsUsageExceeded() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_USAGE_EXCEEDED, 0);
        db.update(TABLE_NAME, contentValues, null, null);
        db.close();
    }

    void resetIsUsageExceeded(String packageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_USAGE_EXCEEDED, 0);
        db.update(TABLE_NAME, contentValues,
                PACKAGE_NAME + " = ?", new String[]{packageName});
        db.close();
    }

    void setIsUsageExceeded(String packageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IS_USAGE_EXCEEDED, 1);
        db.update(TABLE_NAME, contentValues,
                PACKAGE_NAME + " = ?", new String[]{packageName});
        db.close();
    }

}

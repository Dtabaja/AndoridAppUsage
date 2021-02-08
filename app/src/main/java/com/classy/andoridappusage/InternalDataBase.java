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
            trackedInfo = new TrackedInfo(packageName);
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
            trackedAppInfos.add(new TrackedInfo(packageName));
        }
        cursor.close();
        db.close();
        return trackedAppInfos;
    }


}

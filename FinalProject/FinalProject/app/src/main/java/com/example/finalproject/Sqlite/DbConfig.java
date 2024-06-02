package com.example.finalproject.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbConfig extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database-hadiths-final";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "hadiths_final";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IS_LOGGED_IN = "isLoggedIn";

    public static final String FAVORITES_TABLE_NAME = "favorite";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_HADIST_ID = "hadist_id";

    public DbConfig(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + FAVORITES_TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_HADIST_ID + " TEXT)");
    }

    public void insertData(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_IS_LOGGED_IN, 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void insertFavorite(int userId, String hadistId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_HADIST_ID, hadistId);
        db.insert(FAVORITES_TABLE_NAME, null, values);
        db.close();
    }

    public boolean isFavorite(int userId, String hadistId) {
        if (hadistId == null) {
            return false;
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(FAVORITES_TABLE_NAME, new String[]{COLUMN_ID}, COLUMN_USER_ID + " = ? AND " + COLUMN_HADIST_ID + " = ?", new String[]{String.valueOf(userId), hadistId}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }


    public Cursor getFavoritemHadistByUserId(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(FAVORITES_TABLE_NAME, null, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    public void deleteFavorite(int userId, String hadistId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FAVORITES_TABLE_NAME, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        Log.d("kkk", "hapus");
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}

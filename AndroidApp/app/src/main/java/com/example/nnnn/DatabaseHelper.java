package com.example.nnnn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "SignLog.db";
    private static final String DATABASE_NAME = "appDatabase";
    private static final int DATABASE_VERSION = 1;





    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "email TEXT PRIMARY KEY, " +
                "password TEXT)";
        db.execSQL(createUsersTable);

    }





    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS users");
        onCreate(MyDB);
    }



    public Boolean insertData(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);

        long result = MyDatabase.insert("users", null, contentValues);
        return result != -1;
    }


    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        return cursor.getCount() > 0;
    }


    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        return cursor.getCount() > 0;
    }
    public Cursor getPasswordByEmail(String email) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        return MyDatabase.rawQuery("SELECT password FROM users WHERE email = ?", new String[]{email});
    }



}

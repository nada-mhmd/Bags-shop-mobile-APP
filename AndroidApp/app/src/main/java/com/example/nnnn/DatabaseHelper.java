package com.example.nnnn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "SignLog.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table with added fields for security question and answer
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "email TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "security_question TEXT, " +
                "security_answer TEXT)";
        db.execSQL(createUsersTable);


    }


    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("ALTER TABLE users ADD COLUMN security_question TEXT;");
        MyDB.execSQL("DROP TABLE IF EXISTS users");
        onCreate(MyDB);
    }


    public Boolean insertData(String email, String password, String securityQuestion, String securityAnswer){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("security_question", securityQuestion);
        contentValues.put("security_answer", securityAnswer);

        Log.d("Database", "Attempting to insert data for email: " + email);

        long result = MyDatabase.insert("users", null, contentValues);
        if (result == -1) {
            Log.e("Database", "Insert failed for email: " + email);
            return false;
        }

        Log.d("Database", "Insert successful for email: " + email);
        return true;
    }


    public Boolean checkEmail(String email) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            Log.d("Database", "Email already exists: " + email);
        } else {
            Log.d("Database", "Email does not exist: " + email);
        }

        return cursor.getCount() > 0;
    }


    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});
        boolean valid = cursor.getCount() > 0;
        cursor.close(); // Always close the cursor
        MyDatabase.close(); // Always close the database
        return valid;
    }

    public Cursor getPasswordByEmail(String email) {
        SQLiteDatabase MyDatabase = this.getReadableDatabase();
        return MyDatabase.rawQuery("SELECT password FROM users WHERE email = ?", new String[]{email});
    }

    public String getPassword(String email, String securityQuestion, String securityAnswer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM users WHERE email = ? AND security_question = ? AND security_answer = ?",
                new String[]{email, securityQuestion, securityAnswer});

        if (cursor != null && cursor.moveToFirst()) {
            String password = cursor.getString(0);
            cursor.close();
            return password;
        } else {
            return null;
        }
    }
    public boolean validateUser(String email, String securityQuestion, String securityAnswer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email = ? AND security_question = ? AND security_answer = ?",
                new String[]{email, securityQuestion, securityAnswer});

        boolean isValid = cursor != null && cursor.moveToFirst();
        if (cursor != null) cursor.close();
        return isValid;
    }


    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);

        int rowsAffected = db.update("users", values, "email = ?", new String[]{email});
        return rowsAffected > 0;
    }

}

package com.example.nnnn;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // استعلامات إنشاء الجداول
        db.execSQL("CREATE TABLE IF NOT EXISTS FOOD(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, category VARCHAR, quantity INTEGER, image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS FOOD");
        onCreate(db);
    }

    // دالة لتنفيذ استعلامات التعديل مثل INSERT و UPDATE
    public void queryData(String query) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(query);
    }

    // دالة لإدخال البيانات
    public void insertData(String name, String price, String category, int quantity, byte[] image) {
        SQLiteDatabase database = getWritableDatabase();
        String query = "INSERT INTO FOOD (name, price, category, quantity, image) VALUES ('" + name + "', '" + price + "', '" + category + "', " + quantity + ", ?)";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindBlob(1, image);
        statement.executeInsert();
    }

    // دالة لحذف البيانات
    public void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();
        String query = "DELETE FROM FOOD WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindLong(1, id);
        statement.executeUpdateDelete();
    }

    // دالة لتحديث البيانات
    public void updateData(String name, String price, String category, int quantity, byte[] image, int id) {
        SQLiteDatabase database = getWritableDatabase();
        String query = "UPDATE FOOD SET name = ?, price = ?, category = ?, quantity = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, name);
        statement.bindString(2, price);
        statement.bindString(3, category);
        statement.bindLong(4, quantity);
        statement.bindBlob(5, image);
        statement.bindLong(6, id);
        statement.executeUpdateDelete();
    }

    // دالة لاسترجاع البيانات
    public Cursor getData(String query) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(query, null);
    }

    public Cursor getData(String query, String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (category == null) {
            return db.rawQuery(query, null);
        } else {
            return db.rawQuery(query, new String[]{category});
        }
    }

}


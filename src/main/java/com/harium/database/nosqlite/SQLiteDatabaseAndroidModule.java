package com.harium.database.nosqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteDatabaseAndroidModule extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public final static String DATABASE = "database.sqlite";

    private static final String[] ALL_COLUMNS = {Data.COLUMN_KEY, Data.COLUMN_VALUE,
            Data.COLUMN_CREATED_AT, Data.COLUMN_UPDATED_AT};

    private static final String WHERE = Data.COLUMN_KEY + " = ?";

    public SQLiteDatabaseAndroidModule(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE " + Data.TABLE_NAME + " (");
        query.append(Data.COLUMN_KEY);
        query.append("  TEXT PRIMARY KEY,");
        query.append(Data.COLUMN_VALUE);
        query.append(" TEXT,");
        query.append(Data.COLUMN_CREATED_AT);
        query.append(" INTEGER,");
        query.append(Data.COLUMN_UPDATED_AT);
        query.append(" INTEGER);");

        db.execSQL(query.toString());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Data.TABLE_NAME + ";");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Data.TABLE_NAME + ";");
        onCreate(db);
    }

    public int create(Data item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Data.COLUMN_KEY, item.getKey());
        values.put(Data.COLUMN_VALUE, item.getValue());

        return (int) db.insert(Data.TABLE_NAME, null, values);
    }

    public int update(Data item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Data.COLUMN_KEY, item.getKey());
        values.put(Data.COLUMN_VALUE, item.getValue());

        String[] whereArgs = new String[]{item.getKey()};

        return db.update(Data.TABLE_NAME, values, WHERE, whereArgs);
    }

    public List<Data> queryAll() {
        List<Data> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(Data.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            Data data = new Data();
            data.setKey(cursor.getString(0));
            data.setValue(cursor.getString(1));
            data.setCreatedAt(cursor.getLong(2));
            data.setUpdatedAt(cursor.getLong(2));
            list.add(data);
        }
        cursor.close();

        return list;
    }

    public int delete(Data item) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = new String[]{item.getKey()};
        return db.delete(Data.TABLE_NAME, WHERE, whereArgs);
    }

    public void createOrUpdate(Data item) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Data.COLUMN_KEY, item.getKey());
        values.put(Data.COLUMN_VALUE, item.getValue());
        values.put(Data.COLUMN_UPDATED_AT, new Date().getTime());

        db.insertWithOnConflict(Data.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Data queryData(String key) {
        SQLiteDatabase db = getReadableDatabase();
        String[] whereArgs = new String[]{key};
        Cursor cursor = db.query(Data.TABLE_NAME, ALL_COLUMNS, WHERE, whereArgs, null, null, null, "1");

        Data data = new Data();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                data.setKey(cursor.getString(0));
                data.setValue(cursor.getString(1));
                data.setCreatedAt(cursor.getLong(2));
                data.setUpdatedAt(cursor.getLong(2));
            }
        }
        cursor.close();

        return data;
    }

    public long count() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + Data.TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        return count;
    }
}

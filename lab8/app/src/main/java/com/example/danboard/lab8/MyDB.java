package com.example.danboard.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Danboard on 17-12-10.
 */

public class MyDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "Contacts.db"; // 数据库名字
    private static final String TABLE_NAME = "Contacts"; // TABLE名字
    private static final int DB_VERSION = 1;             // 版本号

    public MyDB(Context context) {  // 构造函数，方便创建，只传入context
        super(context, DB_NAME, null, DB_VERSION);
    }

    /*
     * 新建TABLE
     * 第一次调用getWirtableDatabase()或getReadableDatabase()的时候调用
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME
                + "(_id integer primary key, "
                + "name text not null, "
                + "birth text, "
                + "gift text);";
        db.execSQL(CREATE_TABLE);
    }

    /* DB_BERSION 变化时调用此函数 */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    //增
    public void insert(Member item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", item.getName());
        values.put("birth", item.getBirth());
        values.put("gift", item.getGift());

        db.insert(TABLE_NAME, null, values);
        Log.d("TAG", "Add Successfully");
    }
    //删
    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = {name};

        db.delete(TABLE_NAME, whereClause, whereArgs);
        Log.d("TAG", "Delete Successfully");
    }
    //查
    public Cursor query(String info, String arg) {
        SQLiteDatabase db = getReadableDatabase();
        String selection;
        String[] selectionArgs;
        if(info == null){
            selection = null;
            selectionArgs = null;
        }else {
            selection = info + " = ? ";
            selectionArgs = new String[]{arg};
        }
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        Log.d("TAG", "Query Successfully");
        return c;
    }
    //改
    public void update(Member item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        String whereClause = "name = ?";
        String[] whereArgs = {item.getName()};

        values.put("name", item.getName());
        values.put("birth", item.getBirth());
        values.put("gift", item.getGift());

        db.update(TABLE_NAME, values, whereClause, whereArgs);
        Log.d("TAG", "Update Successfully");
    }
}

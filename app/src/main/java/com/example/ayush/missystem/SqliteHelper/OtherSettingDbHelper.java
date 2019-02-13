package com.example.ayush.missystem.SqliteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class OtherSettingDbHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "setting.sqlite";
    private static String LOCAL_UNIT_TABEL_NAME = "local_unit";
    private static String WARD_ID_TABEL_NAME = "ward_id";
    public static final int version = 1;
    private SQLiteDatabase db;
    private String DB_PATH;

    public OtherSettingDbHelper(Context context) {
        super(context, DB_NAME, null, version);
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + LOCAL_UNIT_TABEL_NAME + "( local_unit_id integer) ");
        db.execSQL("create table ward_id ( ward_id integer) ");
        ContentValues values = new ContentValues();
        values.put("local_unit_id", 68);
        db.insert("local_unit", null, values);
        ContentValues va = new ContentValues();
        va.put("ward_id", 1);
        db.insert("ward_id", null, va);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + LOCAL_UNIT_TABEL_NAME + "");
        db.execSQL("drop table if exists " + WARD_ID_TABEL_NAME + "");
        onCreate(db);
    }

    public Cursor getData(String Query) {
        String myPath = DB_PATH + DB_NAME;
        Cursor c = null;
        try {
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            c = db.rawQuery(Query, null);
        } catch (Exception e) {
            Log.e("Err", e.toString());
        }

        return c;
    }

    public void ChangeLocalUnitID(Integer value) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("update " + LOCAL_UNIT_TABEL_NAME + " set local_unit_id ='" + value + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Log.e("updated", "successfully");
            cursor.close();
        }
        db.close();
    }

    public String GetLocalUnitId() {
        String local_unit_id = null;
        Cursor cursor = getData("select * from " + LOCAL_UNIT_TABEL_NAME + "");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            local_unit_id = cursor.getString(cursor.getColumnIndex("local_unit_id"));

            cursor.close();
        }
        return local_unit_id;
    }

    public void ChangeWardID(Integer value) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("update " + WARD_ID_TABEL_NAME + " set ward_id ='" + value + "'", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Log.e("updated", "successfully");
            cursor.close();
        }
        db.close();
    }

    public String GetWardId() {
        String local_unit_id = null;
        Cursor cursor = getData("select * from " + WARD_ID_TABEL_NAME + "");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            local_unit_id = cursor.getString(cursor.getColumnIndex("ward_id"));

            Log.e("updated", "get");
            cursor.close();
        }
        return local_unit_id;
    }
}

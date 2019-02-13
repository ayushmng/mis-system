package com.example.ayush.missystem.SqliteHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ayush.missystem.Cell;
import com.example.ayush.missystem.Constants;
import com.example.ayush.missystem.DeathData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "data.sqlite";
    private SQLiteDatabase db;
    private Context context;
    private String DB_PATH;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String currentDateandTime = sdf.format(new Date());
    OtherSettingDbHelper odhelper;
    private String odhvalue;


    @SuppressLint("SdCardPath")
    public DbHelper(Context context) {
        super(context, DB_NAME, null, 3);
        this.context = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        odhelper = new OtherSettingDbHelper(context);
        odhvalue = odhelper.GetLocalUnitId();
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if (!dbExist) {
            getWritableDatabase();
            copyDataBase();

        } else {
            this.getWritableDatabase();
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        Log.d("database", "created" + dbFile);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
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

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public ArrayList<String> getWard() {
        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = getData("select * from wards where localunit_id='" + odhvalue + "'");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String cid = cursor.getString(cursor.getColumnIndex("id"));
                String ward_name_en = cursor.getString(cursor.getColumnIndex("ward_name_en"));
                String ward_name_np = cursor.getString(cursor.getColumnIndex("ward_name_np"));
                String localunit_id = cursor.getString(cursor.getColumnIndex("localunit_id"));
                String code = cursor.getString(cursor.getColumnIndex("code"));

                arrayList.add(ward_name_np);

                cursor.moveToNext();
            }
            cursor.close();
        }
        return arrayList;
    }

    public ArrayList<String> getDeathCauses() {
        ArrayList<String> deathcaausearraylist = new ArrayList<>();  //create your array model if you want more than one value from tabel
        Cursor cursor = getData("select * from death_causes");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String name_np = cursor.getString(cursor.getColumnIndex("name"));
                String name_en = cursor.getString(cursor.getColumnIndex("name_en"));
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                deathcaausearraylist.add(name_np);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return deathcaausearraylist;
    }

    public ArrayList<String> getHousenumber(int ward_id) {
        ArrayList<String> housesnolist = new ArrayList<>();
        Cursor cursor = getData("select * from houses,wards on wards.id=houses.ward_id where ward_id='" + ward_id + "' and wards.localunit_id='" + odhvalue + "'");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                housesnolist.add(name);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return housesnolist;
    }

    public void AddDeathData(String family_id, String death_one_year, String no_of_deaths, String cause, String cause_other, String ward_id, String update_by) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("family_id", family_id);
        values.put("death_one_year", death_one_year);
        values.put("no_of_death", no_of_deaths);
        values.put("cause", cause);
        values.put("cause_other", cause_other);
        values.put("ward_id", Constants.ward_id);
        values.put("created_at", currentDateandTime);
        values.put("updated_by", update_by);
        db.insert("deaths", null, values);
        db.close();
        Log.e("uploaded", ":yes");
    }

    public void AddFamilyData(String family_id, String death_one_year, String no_of_deaths, String cause, String cause_other, String ward_id, String update_by) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("family_id", family_id);
        values.put("death_one_year", death_one_year);
        values.put("no_of_death", no_of_deaths);
        values.put("cause", cause);
        values.put("cause_other", cause_other);
        values.put("ward_id", ward_id);
        values.put("created_at", currentDateandTime);
        values.put("updated_by", update_by);
        db.insert("deaths", null, values);
        db.close();
        Log.e("uploaded", ":yes");
    }//change this

    public void AddIndividualsData(String family_id, String death_one_year, String no_of_deaths, String cause, String cause_other, String ward_id, String update_by) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("family_id", family_id);
        values.put("death_one_year", death_one_year);
        values.put("no_of_death", no_of_deaths);
        values.put("cause", cause);
        values.put("cause_other", cause_other);
        values.put("ward_id", ward_id);
        values.put("created_at", currentDateandTime);
        values.put("updated_by", update_by);
        db.insert("deaths", null, values);
        db.close();
        Log.e("uploaded", ":yes");
    }//change this

    public void AddHouseData(String family_id, String death_one_year, String no_of_deaths, String cause, String cause_other, String ward_id, String update_by) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("family_id", family_id);
        values.put("death_one_year", death_one_year);
        values.put("no_of_death", no_of_deaths);
        values.put("cause", cause);
        values.put("cause_other", cause_other);
        values.put("ward_id", ward_id);
        values.put("created_at", currentDateandTime);
        values.put("updated_by", update_by);
        db.insert("deaths", null, values);
        db.close();
        Log.e("uploaded", ":yes");
    }//change this

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("aaa", "upgrade");
        Log.e("aaa -oldVersion", "" + oldVersion);
        Log.e("aaa -newVersion", "" + newVersion);
        try {
            if (db == null) {
                String myPath = DB_PATH + DB_NAME;
                db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }
            switch (oldVersion) {
                case 1:

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  public  List<List<Cell>> getDeathCellList(){
      List<List<Cell>> list = new ArrayList<>();
      SQLiteDatabase sampleDB = this.getReadableDatabase();
      Cursor c = sampleDB.rawQuery("select  DISTINCT deaths.id,deaths.family_id,hh_owner_name,death_causes.name from deaths,families,wards,death_causes on families.house_id=deaths.family_id and wards.id=deaths.ward_id and death_causes.id=deaths.cause where wards.localunit_id='" + odhvalue + "' and wards.id='" + Constants.ward_id + "'", null);
      int i ;

      if (c != null) {
          if (c.moveToFirst()) {
              do {
                      List<Cell> cellList = new ArrayList<>();
                      list.add(cellList);

                          String id = c.getString(c.getColumnIndex("id"));
                          Cell cell, cell1, cell2, cell3;
                          String house_number = c.getString(c.getColumnIndex("family_id"));
                          String house_name = c.getString(c.getColumnIndex("hh_owner_name"));
                          String cause = c.getString(c.getColumnIndex("name"));
                          cell = new Cell(id, house_number);
                          cell1 = new Cell(id, house_name);
                          cell2 = new Cell(id, cause);

                  cellList.add(cell);
                  cellList.add(cell1);
                          cellList.add(cell2);

              } while (c.moveToNext());
          }
          c.close();
      }
      return list;
  }

    public  List<List<Cell>> getFamilyCellList(){
        List<List<Cell>> list = new ArrayList<>();
        SQLiteDatabase sampleDB = this.getReadableDatabase();
        Cursor c = sampleDB.rawQuery("select DISTINCT families.id,families.house_id,hh_owner_name,has_error from families,wards on wards.id=families.ward_id  where wards.localunit_id='" + odhvalue + "' and wards.id='" + Constants.ward_id + "'", null);
        int i ;

        if (c != null) {
            if (c.moveToFirst()) {

                do {

                    List<Cell> cellList = new ArrayList<>();
                    list.add(cellList);

                    String id = c.getString(c.getColumnIndex("id"));
                    Cell cell, cell1, cell2;
                    String house_number = c.getString(c.getColumnIndex("house_id"));
                    String has_error = c.getString(c.getColumnIndex("has_error"));
                    String house_name = c.getString(c.getColumnIndex("hh_owner_name"));
                    cell = new Cell(id, house_number);
                    cell1 = new Cell(id, has_error);
                    cell2 = new Cell(id, house_name);

                    cellList.add(cell);
                    cellList.add(cell2);
                    cellList.add(cell1);

                } while (c.moveToNext());
            }
            c.close();
        }
        return list;
    }

    public  List<List<Cell>> getHousesCellList(){
        List<List<Cell>> list = new ArrayList<>();
        SQLiteDatabase sampleDB = this.getReadableDatabase();
        Cursor c = sampleDB.rawQuery("select DISTINCT houses.id,name,has_error from houses,wards on wards.id=houses.ward_id where wards.localunit_id='" + odhvalue + "' and wards.id='" + Constants.ward_id + "' ORDER BY houses.id", null);
        if (c != null) {
            if (c.moveToFirst()) {

                do {

                    List<Cell> cellList = new ArrayList<>();
                    list.add(cellList);

                    String id = c.getString(c.getColumnIndex("id"));

                    Cell  cell1, cell2;
                    String has_error = c.getString(c.getColumnIndex("has_error"));
                    String house_number = c.getString(c.getColumnIndex("name"));
                    cell1 = new Cell(id, has_error);
                    cell2 = new Cell(id, house_number);
                    cellList.add(cell2);
                    cellList.add(cell1);

                } while (c.moveToNext());
            }
            c.close();
        }

        return list;
    }

    public  List<List<Cell>> getIndividualsCellList(){
        List<List<Cell>> list = new ArrayList<>();
        SQLiteDatabase sampleDB = this.getReadableDatabase();
        Cursor c = sampleDB.rawQuery("select DISTINCT individuals.id,family_index,name,has_error,age from individuals,wards on wards.id=individuals.ward_number where wards.localunit_id='" + odhvalue + "' and wards.id='" + Constants.ward_id + "' ORDER BY individuals.id", null);
        int i ;

        if (c != null) {
            if (c.moveToFirst()) {

                do {

                    List<Cell> cellList = new ArrayList<>();
                    list.add(cellList);

                    String id = c.getString(c.getColumnIndex("id"));

                    Cell cell, cell1,cell3, cell2;
                    String house_number = c.getString(c.getColumnIndex("family_index"));
                    String has_error = c.getString(c.getColumnIndex("has_error"));
                    String age = c.getString(c.getColumnIndex("age"));
                    String house_name = c.getString(c.getColumnIndex("name"));
                    cell = new Cell(id, house_number);
                    cell1 = new Cell(id, has_error);
                    cell2 = new Cell(id, house_name);
                    cell3=new Cell(id,age);

                    cellList.add(cell);
                    cellList.add(cell2);
                    cellList.add(cell3);
                    cellList.add(cell1);

                } while (c.moveToNext());
            }
            c.close();

        }

        return list;
    }


    public ArrayList<DeathData> getDeathFormData(int id) {
        ArrayList<DeathData> Deathformdata = new ArrayList<>();

        Cursor c = getData("select * from deaths where id='" + id + "' ");

        if (c.moveToFirst()) {
            do {
                String iid = c.getString(c.getColumnIndex("id"));
                String family_id = c.getString(c.getColumnIndex("family_id"));
                String death_one_year = c.getString(c.getColumnIndex("death_one_year"));
                String no_of_death = c.getString(c.getColumnIndex("no_of_death"));
                String cause = c.getString(c.getColumnIndex("cause"));
                String cause_other = c.getString(c.getColumnIndex("cause_other"));
                String ward_id = c.getString(c.getColumnIndex("ward_id"));
                Deathformdata.add(new DeathData(iid, family_id, death_one_year, no_of_death, cause, cause_other, ward_id));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return Deathformdata;
    }

    public ArrayList<DeathData> getIndividualsFormData(int id) {
        ArrayList<DeathData> Deathformdata = new ArrayList<>();

        Cursor c = getData("select * from deaths where id='" + id + "' ");

        if (c.moveToFirst()) {
            do {
                String iid = c.getString(c.getColumnIndex("id"));
                String family_id = c.getString(c.getColumnIndex("family_id"));
                String death_one_year = c.getString(c.getColumnIndex("death_one_year"));
                String no_of_death = c.getString(c.getColumnIndex("no_of_death"));
                String cause = c.getString(c.getColumnIndex("cause"));
                String cause_other = c.getString(c.getColumnIndex("cause_other"));
                String ward_id = c.getString(c.getColumnIndex("ward_id"));
                Deathformdata.add(new DeathData(iid, family_id, death_one_year, no_of_death, cause, cause_other, ward_id));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return Deathformdata;
    }//change this

    public ArrayList<DeathData> getFamiliesFormData(int id) {
        ArrayList<DeathData> Deathformdata = new ArrayList<>();

        Cursor c = getData("select * from deaths where id='" + id + "' ");

        if (c.moveToFirst()) {
            do {
                String iid = c.getString(c.getColumnIndex("id"));
                String family_id = c.getString(c.getColumnIndex("family_id"));
                String death_one_year = c.getString(c.getColumnIndex("death_one_year"));
                String no_of_death = c.getString(c.getColumnIndex("no_of_death"));
                String cause = c.getString(c.getColumnIndex("cause"));
                String cause_other = c.getString(c.getColumnIndex("cause_other"));
                String ward_id = c.getString(c.getColumnIndex("ward_id"));
                Deathformdata.add(new DeathData(iid, family_id, death_one_year, no_of_death, cause, cause_other, ward_id));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return Deathformdata;
    }//change this

    public ArrayList<DeathData> getHouseFormData(int id) {
        ArrayList<DeathData> Deathformdata = new ArrayList<>();

        Cursor c = getData("select * from deaths where id='" + id + "' ");

        if (c.moveToFirst()) {
            do {
                String iid = c.getString(c.getColumnIndex("id"));
                String family_id = c.getString(c.getColumnIndex("family_id"));
                String death_one_year = c.getString(c.getColumnIndex("death_one_year"));
                String no_of_death = c.getString(c.getColumnIndex("no_of_death"));
                String cause = c.getString(c.getColumnIndex("cause"));
                String cause_other = c.getString(c.getColumnIndex("cause_other"));
                String ward_id = c.getString(c.getColumnIndex("ward_id"));
                Deathformdata.add(new DeathData(iid, family_id, death_one_year, no_of_death, cause, cause_other, ward_id));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return Deathformdata;
    }//change this


    public int countData(String tabel, int ward_id) {
        Cursor mCount = db.rawQuery("select count(*) from " + tabel + ",wards on wards.id=" + tabel + ".ward_id where ward_id ='" + ward_id + "' " +
                "and wards.localunit_id='" + odhvalue + "' ", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;

    }

    public int countIndividuals(int ward_id) {

        Cursor mCount = db.rawQuery("select count(*) from individuals,wards on wards.id=individuals.ward_number where ward_number ='" + ward_id + "' " +
                "and wards.localunit_id='" + odhvalue + "' ", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        return count;
    }

    public void deleteItem(String tabel, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tabel + " WHERE id='" + id + "'");
        db.close();

    }

    public void UpdateHouseData(Integer id, String family_id, String death_one_year, String no_of_deaths, String cause, String cause_other, String ward_id
            , String update_by) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor update = db.rawQuery("update deaths set family_id='" + family_id + "',death_one_year='" + death_one_year + "',updated_at='" + currentDateandTime + "',no_of_death='" + no_of_deaths + "',cause='" + cause + "'" +
                ",cause_other='" + cause_other + "' where id='" + id + "' ", null);

        update.moveToFirst();
        update.close();
        db.close();
        Log.d("valuesupdated", ":" + id + ":" + family_id + ":" + death_one_year + no_of_deaths + cause + "co" + cause_other + ":" + ward_id);
    }//change this

    public void UpdateFamilyData(Integer id, String family_id, String death_one_year, String no_of_deaths, String cause, String cause_other, String ward_id
            , String update_by) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor update = db.rawQuery("update deaths set family_id='" + family_id + "',death_one_year='" + death_one_year + "',updated_at='" + currentDateandTime + "',no_of_death='" + no_of_deaths + "',cause='" + cause + "'" +
                ",cause_other='" + cause_other + "' where id='" + id + "' ", null);

        update.moveToFirst();
        update.close();
        db.close();
        Log.d("valuesupdated", ":" + id + ":" + family_id + ":" + death_one_year + no_of_deaths + cause + "co" + cause_other + ":" + ward_id);
    }//change this

    public void UpdateDeathsData(Integer id, String family_id, String death_one_year, String no_of_deaths, String cause, String cause_other, String ward_id
            , String update_by) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor update = db.rawQuery("update deaths set family_id='" + family_id + "',death_one_year='" + death_one_year + "',updated_at='" + currentDateandTime + "',no_of_death='" + no_of_deaths + "',cause='" + cause + "'" +
                ",cause_other='" + cause_other + "' where id='" + id + "' ", null);

        update.moveToFirst();
        update.close();
        db.close();
        Log.d("valuesupdated", ":" + id + ":" + family_id + ":" + death_one_year + no_of_deaths + cause + "co" + cause_other + ":" + Constants.ward_id);
    }

    public void UpdateIndividualsData(Integer id, String family_id, String death_one_year, String no_of_deaths, String cause, String cause_other, String ward_id
            , String update_by) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor update = db.rawQuery("update deaths set family_id='" + family_id + "',death_one_year='" + death_one_year + "',updated_at='" + currentDateandTime + "',no_of_death='" + no_of_deaths + "',cause='" + cause + "'" +
                ",cause_other='" + cause_other + "' where id='" + id + "' ", null);

        update.moveToFirst();
        update.close();
        db.close();
        Log.d("valuesupdated", ":" + id + ":" + family_id + ":" + death_one_year + no_of_deaths + cause + "co" + cause_other + ":" + ward_id);
    }//change this

}
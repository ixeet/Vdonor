package com.bluecepheid.admin.vdonor;

/**
 * Created by Admin on 30-07-2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 29-07-2015.
 */

/**
 * Created by PRIYANKA GUPTA on 3/21/2015.
 */

public class SqliteHelper4 extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "districdata";
    private static final int DATABASE_VERSION = 1;
    public SqliteHelper4(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE distdata " +
                "(id TEXT PRIMARY KEY,name TEXT)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS submit");
        //deleteAllData();
        onCreate(db);
    }

    public boolean saveUser (String id,String name)
    {
        Cursor cursor = getUser(id);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);



        long result;
        if (cursor.getCount() == 0) { // Record does not exist
            contentValues.put("id", id);
            result = db.insert("distdata", null, contentValues);
        } else { // Record exists
            result = db.update("distdata", contentValues, "id=?", new String[] { id });
        }

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public ArrayList<HashMap<String, Object>> getAllUsers() {
        ArrayList<HashMap<String, Object>> wordList;
        wordList = new ArrayList<HashMap<String, Object>>();
        String selectQuery = "SELECT  * FROM distdata";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToNext()) {
            do {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("id", cursor.getString(0));
                //   map.put("button", cursor.getInt(1));

                map.put("name",cursor.getString(1));

                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
    }
    public Cursor getUser(String id){

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM distdata WHERE id=?";

        return db.rawQuery(sql, new String[] { id } );
    }
    public Cursor getUser1(String name){

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM distdata WHERE name=?";

        return db.rawQuery(sql, new String[] { name } );
    }
    void deleteAllData()
    {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete("flg", null, null);

    }


    /**
     * Compose JSON out of SQLite records
     * @return
     */
  /*  public String composeJSONfromSQLite(){
        ArrayList<HashMap<String, Object>> wordList;
        wordList = new ArrayList<HashMap<String, Object>>();
        String selectQuery = "SELECT  * FROM submit";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> map = new HashMap<String, Object>();
                 map.put("quest_id", cursor.getString(0));
            //   map.put("button", cursor.getInt(1));
                 map.put("selection",cursor.getString(2));
                 map.put("ccorrect",cursor.getString(3));
                 map.put("exam_id",cursor.getString(4));
                map.put("user_id",cursor.getString(5));
                 wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }*/

    /**
     * Get Sync status of SQLite
     * @return
     */
    public String getSyncStatus(){
        String msg = null;
        if(this.dbSyncCount() == 0){
            msg = "SQLite and Remote MySQL DBs are in Sync!";
        }else{
            msg = "DB Sync needed\n";
        }
        return msg;
    }

    /**
     * Get SQLite records that are yet to be Synced
     * @return
     */
    public int dbSyncCount(){
        int count = 0;
        String selectQuery = "SELECT * FROM distdata";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }



}




package edu.kvcc.cis298.cis298assignment4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Created by user on 12/11/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DATABASE_HELPER";

    private static final String DATABASE_NAME = "Beverages.db";
    private static final String TABLE_NAME = "Beverages_table";
    private static final String mID_Col = "BeverageID";
    private static final String mName_Col = "BeverageName";
    private static final String mPack_Col = "BeveragePack";
    private static final String mPrice_Col = "BeveragePrice";
    private static final String mIsActive_Col = "BeveragesActive";

    private SQLiteDatabase db = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table if not exists " + TABLE_NAME + " (" +
                mID_Col + " TEXT PRIMARY KEY," +
                mName_Col + " TEXT," +
                mPack_Col + " TEXT," +
                mPrice_Col + " TEXT," +
                mIsActive_Col + " TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertBeverage(String id, String name, String pack, String price, String active){

        Log.i(TAG,"ID: " + id + " inserted into database");
        ContentValues contentValues = new ContentValues();

        Log.i(TAG,"ID: " + id + "\n" +
                "Name: " + name + "\n" +
                "Pack: " + pack + "\n" +
                "Price: " + price + "\n" +
                "Active: " + String.valueOf(active) + "\n");

        contentValues.put(mID_Col, id);
        contentValues.put(mName_Col, name);
        contentValues.put(mPack_Col, pack);
        contentValues.put(mPrice_Col, price);
        contentValues.put(mIsActive_Col, active);

        long response = db.insert(TABLE_NAME, null, contentValues);
        if(response == -1){
            Log.i(TAG,"Failed to insert");
        } else {
            Log.i(TAG,"Insert successful");
        }
    }

    public Cursor getAllData(){
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return c;
    }

    public boolean exists(){
        try{
            SQLiteDatabase.openDatabase(db.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
            return true;
        } catch(SQLiteException e){
            Log.e(TAG,e.getMessage());
            return false;
        }
    }

}

package com.mstc.student.aitpstockideas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Ben on 3/9/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "stockideaDB2.db";
    private static final String TABLE_IDEAS = "ideas";


    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_IDEA_SYMBOL = "ideasymbol";
    private static final String COLUMN_IDEA_DATE = "ideadate";
    private static final String COLUMN_IDEA_LATITUDE = "idealatitude";
    private static final String COLUMN_IDEA_LONGITUDE = "idealongitude";
    private static final String COLUMN_IDEA_STARTING_PRICE = "ideastartingprice";





    SQLiteDatabase db;

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TOURNEYS_TABLE = "CREATE TABLE " + TABLE_IDEAS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_IDEA_SYMBOL + " TEXT," + COLUMN_IDEA_DATE + " TEXT," +
                COLUMN_IDEA_LATITUDE + " DECIMAL(7,8), " +  COLUMN_IDEA_LONGITUDE + " DECIMAL(7,8),"+ COLUMN_IDEA_STARTING_PRICE + " DECIMAL(8,2))";

        db.execSQL(CREATE_TOURNEYS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IDEAS);
                onCreate(db);
    }
    public void addStockIdea (StockIdea idea){
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDEA_SYMBOL, idea.getSymbol());
            values.put(COLUMN_IDEA_DATE, idea.getDateTime());
            values.put(COLUMN_IDEA_LATITUDE, idea.getGPS_Lat());
            values.put(COLUMN_IDEA_LONGITUDE, idea.getGPS_Lon());
            values.put(COLUMN_IDEA_STARTING_PRICE, idea.get_startingPrice());

            db = this.getWritableDatabase();

            db.insert(TABLE_IDEAS, null, values);
            db.close();
        } catch(SQLiteException e) {
        Log.d("My App", "caught");
        }
    }
    //*****************************************************************************************************************************************************************
    //this method gets all ideas from the database
    //***************************************************************************************************************************************************************
    public Cursor queueAllTournaments() {
        String[] columns = new String[]{COLUMN_ID, COLUMN_IDEA_SYMBOL, COLUMN_IDEA_DATE, COLUMN_IDEA_LATITUDE, COLUMN_IDEA_LONGITUDE,COLUMN_IDEA_STARTING_PRICE};
        db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_IDEAS, columns,
        null, null, null, null, null);
        return cursor;
        }

}

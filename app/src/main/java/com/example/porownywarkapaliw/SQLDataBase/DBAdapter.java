package com.example.porownywarkapaliw.SQLDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.porownywarkapaliw.ShowLogs;

//manage database creation and version management.
public class DBAdapter {
    private SQLiteDatabase sqLiteDatabase;
    private DataBaseHelper dataBaseHelper;


    public DBAdapter(Context context){
        dataBaseHelper = new DataBaseHelper(context);
    }

    private  String gasStationName,gasStationLocation;
    private int gasPrice,petrolPrice,servicePoints;
    public void GetDataToSaveInDB(String gasStationName, int gasPrice, int petrolPrice,int servicePoints,
                                     String gasStationLocation){
        this.gasStationName = gasStationName;
        this.gasPrice = gasPrice;
        this.petrolPrice = petrolPrice;
        this.servicePoints = servicePoints;
        this.gasStationLocation = gasStationLocation;
    }


    private static class DataBaseHelper extends SQLiteOpenHelper{
        private DataBaseHelper(Context context) {
            super(context, DBValues.DATABASE_NAME, null, DBValues.DATA_BASE_VERSION);
        }
        //run when the database file did not exist and was just created
        @Override
        public void onCreate(SQLiteDatabase db) {
            String DB_CREATE_TABLE = "CREATE TABLE " + DBValues.TABLE_NAME +
                    " ( " + DBValues._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DBValues.COLUMN_GAS_PRICE + " INTEGER, " +
                    DBValues.COLUMN_PETROL_PRICE + " INTEGER, "+
                    DBValues.COLUMN_SERVICE_POINTS + " INTEGER, "+
                    DBValues.COLUMN_GAS_STATION_NAME + " TEXT, "+
                    DBValues.COLUMN_GAS_STATION_LOCATION + " TEXT );";
            db.execSQL(DB_CREATE_TABLE);
        }

        //Called when the database file exists but the stored version number is different tan requested in constructor
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            ShowLogs.i(" DBAdapter OnUpgrade");
            String DB_DROP_TABLE = "DROP TABLE IF EXISTS " + DBValues.TABLE_NAME;

            db.execSQL(DB_DROP_TABLE);
            //recreate new database (if version changed)
            onCreate(db);
        }
    }

    public DBAdapter OpenAlarmDB(){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"OpenAlarmDB");
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public long InsertRowAlarmDB(){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"InsertRowAlarmDB");
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBValues.COLUMN_GAS_PRICE, gasPrice);
        contentValues.put(DBValues.COLUMN_PETROL_PRICE,petrolPrice);
        contentValues.put(DBValues.COLUMN_SERVICE_POINTS,servicePoints);
        contentValues.put(DBValues.COLUMN_GAS_STATION_NAME,gasStationName);
        contentValues.put(DBValues.COLUMN_GAS_STATION_LOCATION,gasStationLocation);

        //Return the row ID of the newly inserted row, or -1 if an error occurred
        return sqLiteDatabase.insert(DBValues.TABLE_NAME,null,contentValues);
    }

    private String where = null;
    public boolean DeleteRowAlarmDB(long rowIDtoDelete){
        if(ShowLogs.LOG_STATUS) ShowLogs.i("DBAdapter "+"DeleteAllRowsAlarmDB");
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        where = DBValues._ID + "=" + rowIDtoDelete;
        return sqLiteDatabase.delete(DBValues.TABLE_NAME, where,null) !=0;
    }

    public void DeleteAllRowsAlarmDB(){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"DeleteAllRowsAlarmDB");
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        sqLiteDatabase.delete(DBValues.TABLE_NAME, null,null);
    }

    public Cursor GetAllRowsAlarmDB(){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"GetAllRowsAlarmDB");
        sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(true, DBValues.TABLE_NAME, DBValues.ALL_COLUMNS_KEYS,
                null,null,null,null,null,null);

        if(cursor!= null)
            cursor.moveToFirst();

        return cursor;
    }
    public Cursor GetRowAlarmDB(long rowIdToGet){
        sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        where = DBValues._ID + "=" + rowIdToGet;
        Cursor cursor = sqLiteDatabase.query(true, DBValues.TABLE_NAME, DBValues.ALL_COLUMNS_KEYS,
                where,null,null,null,null,null);
        if(cursor !=null)
            cursor.moveToFirst();

        return cursor;
    }

    public boolean UpdateRowAlarmDB(long rowIdToUpdate){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"UpdateRowAlarmDB");
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        where = DBValues._ID + "=" + rowIdToUpdate;

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBValues.COLUMN_GAS_PRICE, gasPrice);
        contentValues.put(DBValues.COLUMN_PETROL_PRICE,petrolPrice);
        contentValues.put(DBValues.COLUMN_SERVICE_POINTS,servicePoints);
        contentValues.put(DBValues.COLUMN_GAS_STATION_NAME,gasStationName);
        contentValues.put(DBValues.COLUMN_GAS_STATION_LOCATION,gasStationLocation);

        return sqLiteDatabase.update(DBValues.TABLE_NAME,contentValues,where,null) != 0;
    }




    public void closeAlarmDB(){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"closeAlarmDB");
        dataBaseHelper.close();
    }

}
